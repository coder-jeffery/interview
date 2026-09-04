package com.easy.interviewweb.repository;

import com.easy.interviewweb.entity.BizRecordEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface BizRecordRepository extends JpaRepository<BizRecordEntity, Long> {

    Optional<BizRecordEntity> findByBizNo(String bizNo);
}
