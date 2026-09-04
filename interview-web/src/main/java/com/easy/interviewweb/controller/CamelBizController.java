package com.easy.interviewweb.controller;

import com.easy.interviewweb.camel.BizJsonUnmarshaller;
import com.easy.interviewweb.entity.BizRecordEntity;
import com.easy.interviewweb.service.BizRecordService;
import org.apache.camel.ProducerTemplate;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/camel/biz")
public class CamelBizController {

    private final ProducerTemplate producerTemplate;
    private final BizJsonUnmarshaller bizJsonUnmarshaller;
    private final BizRecordService bizRecordService;

    public CamelBizController(ProducerTemplate producerTemplate,
                              BizJsonUnmarshaller bizJsonUnmarshaller,
                              BizRecordService bizRecordService) {
        this.producerTemplate = producerTemplate;
        this.bizJsonUnmarshaller = bizJsonUnmarshaller;
        this.bizRecordService = bizRecordService;
    }

    @GetMapping
    public List<BizRecordEntity> list() {
        return bizRecordService.listAll();
    }

    /**
     * 接收单条对象或数组，走 Camel JSON 入站路由后统一写入 MySQL。
     */
    @PostMapping(value = "/json", consumes = MediaType.APPLICATION_JSON_VALUE)
    public Map<String, Object> ingestJson(@RequestBody String body) throws Exception {
        Object payload = bizJsonUnmarshaller.unmarshal(body);
        producerTemplate.sendBodyAndHeader("direct:jsonIngest", payload, "source", "json");
        int count = payload instanceof List<?> list ? list.size() : 1;
        return Map.of("ok", true, "count", count);
    }
}
