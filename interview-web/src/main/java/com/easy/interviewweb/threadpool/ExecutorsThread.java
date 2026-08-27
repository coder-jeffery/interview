package com.easy.interviewweb.threadpool;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;

public class ExecutorsThread {
    public static void main(String[] args) {
        try (ExecutorService fixed = Executors.newFixedThreadPool(5)) {
            Runnable runnable = () -> {
                System.out.println("runnable exec >>>>>>");
            };
            fixed.execute(runnable);
//            fixed.submit(runnable);
        }

        System.out.println("*****************************************************************");
        //单线程，无界队列
        try(ExecutorService single = Executors.newSingleThreadExecutor()){

            Runnable runnable = ()->{
                System.out.println("single exec >>>");
            };
            single.execute(runnable);
        }

        System.out.println("*****************************************************************");
        //缓存线程池，最大线程 Integer.MAX_VALUE，线程爆炸OOM
        try(ExecutorService cached = Executors.newCachedThreadPool()){
            Runnable runnable = ()->{
                System.out.println("cached exec >>>");
            };
            cached.execute(runnable);
        }

        System.out.println("*****************************************************************");
        //定时任务线程池
        try(ScheduledExecutorService scheduled = Executors.newScheduledThreadPool(3)){
            Runnable runnable = ()->{
                System.out.println("scheduled exec >>>");
            };
            scheduled.execute(runnable);
        }
    }
}
