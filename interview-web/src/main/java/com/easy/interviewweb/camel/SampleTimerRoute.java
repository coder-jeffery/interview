package com.easy.interviewweb.camel;


import org.apache.camel.builder.RouteBuilder;
import org.springframework.stereotype.Component;

@Component
public class SampleTimerRoute extends RouteBuilder {
/**
 * 使用场景：
 *  1. 多系统数据同步、异构系统对接
 * 2. 文件批量处理、FTP/SFTP 文件交换
 * 3. 消息中间件复杂流转、消息过滤、转换、聚合
 * 4. 老旧系统协议对接（SOAP、FTP、Socket）
 * 5. ETL 轻量数据搬运，不需要上笨重 Flink
 *
 * */
    @Override
    public void configure() throws Exception {
        from("timer:java21Demo?period=5000")
                .routeId("java21‑virtual‑thread‑route")
                .setBody(simple("Java21 VirtualThread Camel demo ${date:now}"))
                .log("body = ${body}");

        // direct 用于代码调用
        from("direct:bizInput")
                .routeId("biz‑direct‑route")
                .log("receive message: ${body}");
    }
}

