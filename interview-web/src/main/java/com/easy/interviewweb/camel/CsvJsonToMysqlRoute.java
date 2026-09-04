package com.easy.interviewweb.camel;

import com.easy.interviewweb.entity.camel.BizRecord;
import com.easy.interviewweb.service.BizRecordService;
import org.apache.camel.LoggingLevel;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.dataformat.csv.CsvDataFormat;
import org.springframework.stereotype.Component;

/**
 * CSV 文件、JSON 文件、HTTP JSON 三条入站，全部汇入 direct:ingestBiz，再统一写入 MySQL。
 */
@Component
public class CsvJsonToMysqlRoute extends RouteBuilder {

    private final BizRecordService bizRecordService;
    private final BizJsonUnmarshaller bizJsonUnmarshaller;
    private final BizCsvMapper bizCsvMapper;

    public CsvJsonToMysqlRoute(BizRecordService bizRecordService,
                               BizJsonUnmarshaller bizJsonUnmarshaller,
                               BizCsvMapper bizCsvMapper) {
        this.bizRecordService = bizRecordService;
        this.bizJsonUnmarshaller = bizJsonUnmarshaller;
        this.bizCsvMapper = bizCsvMapper;
    }

    @Override
    public void configure() {
        errorHandler(deadLetterChannel("direct:bizIngestDlq")
                .maximumRedeliveries(2)
                .redeliveryDelay(500)
                .retryAttemptedLogLevel(LoggingLevel.WARN)
                .logExhausted(true));

        from("direct:bizIngestDlq")
                .routeId("biz-ingest-dlq")
                .log(LoggingLevel.ERROR, "biz ingest failed: ${exception.message}, body=${body}");

        CsvDataFormat csv = new CsvDataFormat();
        csv.setUseMaps(true);
        csv.setSkipHeaderRecord(true);
        csv.setDelimiter(',');

        // 路由1：监听 CSV 目录（跳过表头，按列名映射）
        from("file:{{app.camel.csv-dir}}?noop=true&autoCreate=true&antInclude=*.csv&charset=UTF-8&delay=2000&readLock=changed")
                .routeId("csv-file-to-mysql")
                .log("CSV file received: ${header.CamelFileName}")
                .unmarshal(csv)
                .bean(bizCsvMapper, "fromRows")
                .setHeader("source", constant("csv"))
                .to("direct:ingestBiz");

        // 路由2：监听 JSON 目录，支持对象或数组
        from("file:{{app.camel.json-dir}}?noop=true&autoCreate=true&antInclude=*.json&charset=UTF-8&delay=2000&readLock=changed")
                .routeId("json-file-to-mysql")
                .log("JSON file received: ${header.CamelFileName}")
                .convertBodyTo(String.class)
                .bean(bizJsonUnmarshaller, "unmarshal")
                .setHeader("source", constant("json"))
                .to("direct:ingestBiz");

        // 路由3：HTTP / 代码触发 JSON
        from("direct:jsonIngest")
                .routeId("json-direct-to-mysql")
                .choice()
                    .when(body().isInstanceOf(String.class))
                        .bean(bizJsonUnmarshaller, "unmarshal")
                    .endChoice()
                .end()
                .setHeader("source", constant("json"))
                .to("direct:ingestBiz");

        // 统一拆分（单条 POJO 或 List 都可以）
        from("direct:ingestBiz")
                .routeId("ingest-biz-split")
                .split(body())
                .to("direct:persistBiz");

        // 统一写入 MySQL（按 biz_no 幂等 upsert）
        from("direct:persistBiz")
                .routeId("persist-biz-mysql")
                .filter(body().isInstanceOf(BizRecord.class))
                .log("persisting bizNo=${body.bizNo}, source=${header.source}")
                .bean(bizRecordService, "upsert")
                .log("saved to MySQL: bizNo=${body.bizNo}, source=${body.source}");
    }
}
