package com.easy.interviewweb.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "biz_record", uniqueConstraints = @UniqueConstraint(name = "uk_biz_no", columnNames = "biz_no"))
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class BizRecordEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "biz_no", nullable = false, length = 64)
    private String bizNo;

    @Column(name = "user_name", nullable = false, length = 50)
    private String userName;

    @Column(name = "phone", nullable = false, length = 20)
    private String phone;

    @Column(name = "amount", nullable = false, precision = 12, scale = 2)
    private BigDecimal amount;

    @Column(name = "source", nullable = false, length = 16)
    private String source;

    @Column(name = "create_time", nullable = false)
    private LocalDateTime createTime;

    @Column(name = "update_time", nullable = false)
    private LocalDateTime updateTime;
}
