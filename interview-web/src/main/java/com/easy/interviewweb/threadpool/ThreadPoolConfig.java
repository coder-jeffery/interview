package com.easy.interviewweb.threadpool;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.concurrent.*;

@Configuration
public class ThreadPoolConfig {

    @Bean("bizThreadPool")
    public ThreadPoolExecutor bizThreadPool(){
        ThreadFactory factory = r ->{
            Thread t = new Thread(r);
            t.setName("biz-thread-pool");
            return t;
        };

        return new ThreadPoolExecutor(
                4,
                8,
                30,
                TimeUnit.SECONDS,
                new ArrayBlockingQueue<>(100),
                factory,
                new ThreadPoolExecutor.AbortPolicy()
        );
    }
}
