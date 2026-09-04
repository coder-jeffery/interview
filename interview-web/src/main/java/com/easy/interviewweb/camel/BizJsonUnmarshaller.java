package com.easy.interviewweb.camel;

import com.easy.interviewweb.entity.camel.BizRecord;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.util.List;

/**
 * JSON 对象 / 数组都转成 Camel 后续可 split 的载荷。
 */
@Component
public class BizJsonUnmarshaller {

    private final ObjectMapper objectMapper = new ObjectMapper();

    public Object unmarshal(String json) throws Exception {
        if (!StringUtils.hasText(json)) {
            throw new IllegalArgumentException("JSON body is empty");
        }
        JsonNode node = objectMapper.readTree(json);
        if (node.isArray()) {
            return objectMapper.convertValue(node, new TypeReference<List<BizRecord>>() {});
        }
        if (node.isObject()) {
            return objectMapper.treeToValue(node, BizRecord.class);
        }
        throw new IllegalArgumentException("JSON must be an object or array");
    }
}
