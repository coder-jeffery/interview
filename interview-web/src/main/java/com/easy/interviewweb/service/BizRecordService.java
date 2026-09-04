package com.easy.interviewweb.service;

import com.easy.interviewweb.entity.BizRecordEntity;
import com.easy.interviewweb.entity.camel.BizRecord;
import com.easy.interviewweb.repository.BizRecordRepository;
import org.apache.camel.Exchange;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Service
public class BizRecordService {

    private final BizRecordRepository bizRecordRepository;

    public BizRecordService(BizRecordRepository bizRecordRepository) {
        this.bizRecordRepository = bizRecordRepository;
    }

    /**
     * 按 biz_no 幂等写入：已存在则更新，不存在则插入。
     */
    @Transactional
    public BizRecordEntity upsert(Exchange exchange) {
        BizRecord record = exchange.getMessage().getBody(BizRecord.class);
        String source = exchange.getMessage().getHeader("source", String.class);
        if (record == null || !StringUtils.hasText(record.getBizNo())) {
            throw new IllegalArgumentException("bizNo must not be blank");
        }
        String resolvedSource = StringUtils.hasText(source) ? source : "unknown";
        BigDecimal amount = record.getAmount() == null ? BigDecimal.ZERO : record.getAmount();
        LocalDateTime now = LocalDateTime.now();

        BizRecordEntity entity = bizRecordRepository.findByBizNo(record.getBizNo())
                .map(existing -> {
                    existing.setUserName(record.getUserName());
                    existing.setPhone(record.getPhone());
                    existing.setAmount(amount);
                    existing.setSource(resolvedSource);
                    existing.setUpdateTime(now);
                    return existing;
                })
                .orElseGet(() -> BizRecordEntity.builder()
                        .bizNo(record.getBizNo())
                        .userName(record.getUserName())
                        .phone(record.getPhone())
                        .amount(amount)
                        .source(resolvedSource)
                        .createTime(now)
                        .updateTime(now)
                        .build());
        return bizRecordRepository.save(entity);
    }

    public List<BizRecordEntity> listAll() {
        return bizRecordRepository.findAll();
    }
}
