package com.easy.interviewweb.threadpool;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

public class CustomizedThreadPool {
    public static void main(String[] args) {
        //创建线程池 CustomizedThreadPool
        ThreadFactory threadFactory = r ->{
            Thread customizedThread = new Thread(r);
            customizedThread.setName("customizedThread-pool"+customizedThread.getThreadGroup());
            return  customizedThread;
        };

        try(ThreadPoolExecutor pool = new ThreadPoolExecutor(
                4, //核心线程数
                8, //最大线程数
                30L, TimeUnit.SECONDS,//空闲线程存活时间
                new ArrayBlockingQueue<>(100),//阻塞队列
                threadFactory,
                new ThreadPoolExecutor.CallerRunsPolicy()//拒绝策略 | 线程池忙不过来，**不再开新线程，由提交任务的那个线程自己执行这个任务**
        )){
            pool.execute(() -> System.out.println("执行xxx任务" + "thread name: "+pool.getThreadFactory()));
            pool.shutdown();
        }
    }
}
