package com.easy.interviewweb.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.TaskScheduler;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.scheduling.concurrent.ThreadPoolTaskScheduler;

@Configuration
public class ScheduleConfig {

    /**
     * 自定义异步线程： 自定义调度线程池 TaskScheduler
     * Spring 内置调度器默认只有 1 个线程。
     * */
    @Bean
    public TaskScheduler taskScheduler(){
        ThreadPoolTaskScheduler scheduler = new ThreadPoolTaskScheduler();
        scheduler.setPoolSize(5);
        scheduler.setThreadNamePrefix("customized-scheduler-thread-");
        scheduler.setWaitForTasksToCompleteOnShutdown(true);// 关闭时等待任务执行完成
        scheduler.setAwaitTerminationMillis(30);
        return scheduler;
    }
}
