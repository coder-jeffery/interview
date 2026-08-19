package com.easy.interviewweb.threadpool;

import javax.annotation.processing.Generated;
import java.util.UUID;
import java.util.concurrent.Callable;
import java.util.concurrent.Future;
import java.util.concurrent.FutureTask;

public class CustomizedThread {
    public static void main(String[] args) throws InterruptedException {
        System.out.println("**********************方式一***********************************");
        //CustomizedThread
        CustomizedSubThread customizedSubThread = new CustomizedSubThread();
        /**
         * `start()` 是 native 本地方法，调用操作系统 API，**向操作系统申请创建真正的 OS 线程**
         * */
        customizedSubThread.setName("CustomizedSubThread-"+ System.currentTimeMillis());
        customizedSubThread.start();
        Thread.sleep(5000);
        customizedSubThread.interrupt();
        /**
         * run() 不会创建新线程；**不会创建新操作系统线程**，直接调用就是在**当前调用线程执行代码**。
         * */
//        customizedSubThread.run();

        System.out.println("**********************方式二***********************************");

        // 方式二
        Runnable taks = () ->{
            System.out.println("runable runnable exec ");
        };

        Thread customizedRunnableThread  = new Thread(taks);
        customizedRunnableThread.setName("runnable-thread-001");
        customizedRunnableThread.start();
        System.out.println("runnable thread name:" + customizedRunnableThread.getName()+"\nrunnable thread state:"+customizedRunnableThread.getState()+
                "\nrunnable thread id:"+customizedRunnableThread.threadId());


        System.out.println("*********************方式三************************************");
        //方式三
        Callable<Integer> callable =()-> {
            Thread.sleep(100);
            System.out.println("callable thread");
            return 100;
        };

        FutureTask<Integer> futureTask = new FutureTask<>(callable);
        Thread callableThread = new Thread(futureTask);
        callableThread.start();
        callableThread.isDaemon();
        System.out.println("Callable thread name:" + callableThread.getName()+"\nCallable thread state:"+callableThread.getState()+
                "\nCallable thread id:"+callableThread.threadId()
                +
                "\nCallable thread daemon value:"+callableThread.isDaemon());
    }

    //GC 垃圾回收是守护线程  守护线程和用户线程 JVM退出守护线程会被粗暴阻止

    /***
     * 守护线程：
     *      日志监控，巡检，定时巡检，日志辅助，JVM退出 暴力杀死守护线程
     *用户线程：
     *      业务工作 JVM必须等用户线程执行完毕退出
     *
     * */

}


// 方式一：
class CustomizedSubThread extends Thread{

    @Override
    public void run(){
        System.out.println("线程：thread exec: " + Thread.currentThread().getName() + "\n线程： thread state: " + Thread.currentThread().getState() + "\n线程：thread threadId: "+ Thread.currentThread().threadId());
    }
}

