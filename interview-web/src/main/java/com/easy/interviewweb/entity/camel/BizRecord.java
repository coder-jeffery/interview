package com.easy.interviewweb.entity.camel;

import com.fasterxml.jackson.annotation.JsonAlias;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.apache.camel.dataformat.bindy.annotation.CsvRecord;
import org.apache.camel.dataformat.bindy.annotation.DataField;

import java.math.BigDecimal;

/**
 * CSV / JSON 统一入站模型。
 * Bindy 按列位置解析 CSV，Jackson 按字段名解析 JSON（同时兼容 snake_case 与 camelCase）。
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
@CsvRecord(separator = ",", skipFirstLine = true)
public class BizRecord {

    @DataField(pos = 1, columnName = "biz_no")
    @JsonProperty("biz_no")
    @JsonAlias("bizNo")
    private String bizNo;

    @DataField(pos = 2, columnName = "user_name")
    @JsonProperty("user_name")
    @JsonAlias("userName")
    private String userName;

    @DataField(pos = 3, columnName = "phone")
    @JsonProperty("phone")
    private String phone;

    @DataField(pos = 4, columnName = "amount")
    @JsonProperty("amount")
    private BigDecimal amount;
}
