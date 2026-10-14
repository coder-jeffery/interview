package com.easy.interviewtest.service;

import io.github.coderjeffery.distributedlock.DistributedLock;
import io.github.coderjeffery.distributedlock.DistributedLockClient;
import java.util.concurrent.TimeUnit;
import org.springframework.stereotype.Service;

@Service
public class OrderService {

    private final DistributedLockClient lockClient;

    public OrderService(DistributedLockClient lockClient) {
        this.lockClient = lockClient;
    }

    public void pay(String orderId) throws InterruptedException {
        DistributedLock lock = lockClient.getLock("order:" + orderId);
        if (!lock.tryLock(3, 30, TimeUnit.SECONDS)) {
            throw new IllegalStateException("处理中，请稍后重试");
        }
        try {
            // 业务
        } finally {
//            lock.unlock();   // 必须由加锁的同一个线程释放
        }
    }

}