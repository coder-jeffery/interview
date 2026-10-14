package com.easy.interviewtest.service;

import io.github.coderjeffery.distributedlock.annotation.DistributedLocked;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class PocService {

    @DistributedLocked(key = "'order:' + #orderId")
    @Transactional
    public void pocPay(String orderId) {
        long start = System.currentTimeMillis();
        System.out.println("pocPay testing");
    }
}
