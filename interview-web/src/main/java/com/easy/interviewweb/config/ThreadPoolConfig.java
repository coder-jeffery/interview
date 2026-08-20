package com.easy.interviewweb.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import java.util.concurrent.*;

@Configuration
@EnableAsync
public class ThreadPoolConfig {

    @Bean("bizThreadPool")
    public Executor bizThreadPool(){
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

    @Bean("businessAsyncExecutor")
    public Executor businessAsyncExecutor(){
        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
        executor.setCorePoolSize(4);
        executor.setMaxPoolSize(8);
        executor.setKeepAliveSeconds(30);//
        executor.setQueueCapacity(100);//队列容量
        executor.setThreadNamePrefix("customized-task-async-thread-");//线程名前缀
        executor.setRejectedExecutionHandler(new ThreadPoolExecutor.CallerRunsPolicy());//拒绝策略
        executor.initialize();//初始化
        return executor;
    }
}
