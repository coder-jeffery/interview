package com.easy.interviewweb.config;

import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.ScheduledAnnotationBeanPostProcessor;

@Configuration
public class ScheduleBootConfig {
    /**
     * 替换掉在启动类添加//@EnableScheduling 注解
     * ：配合`@ConditionalOnProperty`实现**配置开关控制定时任务全局开启关闭**
     * */
    @Bean
//    @ConditionalOnProperty(prefix = "app.schedule", name = "enable", havingValue = "true")
    public ScheduledAnnotationBeanPostProcessor scheduledAnnotationBeanPostProcessor(){
        System.out.println("exec scheduledAnnotationBeanPostProcessor method >>>>");
        return new ScheduledAnnotationBeanPostProcessor();
    }
}
