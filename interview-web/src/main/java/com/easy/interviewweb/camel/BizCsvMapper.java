package com.easy.interviewweb.camel;

import com.easy.interviewweb.entity.camel.BizRecord;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Component
public class BizCsvMapper {

    public List<BizRecord> fromRows(List<Map<String, Object>> rows) {
        List<BizRecord> records = new ArrayList<>();
        if (rows == null) {
            return records;
        }
        for (Map<String, Object> row : rows) {
            records.add(fromRow(row));
        }
        return records;
    }

    public BizRecord fromRow(Map<String, Object> row) {
        String amount = value(row, "amount");
        return BizRecord.builder()
                .bizNo(value(row, "biz_no", "bizNo"))
                .userName(value(row, "user_name", "userName"))
                .phone(value(row, "phone"))
                .amount(StringUtils.hasText(amount) ? new BigDecimal(amount) : BigDecimal.ZERO)
                .build();
    }

    private String value(Map<String, Object> row, String... keys) {
        for (String key : keys) {
            Object raw = row.get(key);
            if (raw != null && StringUtils.hasText(raw.toString())) {
                return raw.toString().trim();
            }
        }
        return null;
    }
}
