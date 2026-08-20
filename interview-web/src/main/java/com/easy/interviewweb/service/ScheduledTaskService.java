package com.easy.interviewweb.service;

import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

@Service
public class ScheduledTaskService {

    /**
     * cron表达式：每10秒执行一次 ; cron格式：秒 分 时 日 月 周
     */
    @Scheduled(cron = "${task.cron}")  // 0/10 * * * * ?
    public void notifySms(){
        System.out.println("【 corn 定时 】每10sec执行一下 线程：" + Thread.currentThread().getName());
    }

    @Scheduled(fixedRate = 5000)
    public void fixedRateTask() {
        System.out.println("【fixedRate】每5s，线程：" + Thread.currentThread().getName());
        try {
            Thread.sleep(3000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    /**
     * fixedDelay：上一次执行完毕之后，再等待5000ms执行下一次 ; 不会并发执行同一个任务，生产最常用
     */
    @Scheduled(fixedDelay = 5000)
    @Async("businessAsyncExecutor")  //`@Async`，定时调度线程只负责触发，真实业务交给业务线程池执行。
    public void fixedDelayTask() {
        System.out.println("【fixedDelay】执行结束后等待5s，线程：" + Thread.currentThread().getName());
    }
}
