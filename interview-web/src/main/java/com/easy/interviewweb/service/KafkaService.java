package com.easy.interviewweb.service;

import jakarta.annotation.Resource;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import java.util.concurrent.Executor;

@Service
public class KafkaService {

    @Resource(name = "businessAsyncExecutor")
    private Executor businessAsyncExecutor;

    //指定使用我们的线程池
    @Async("businessAsyncExecutor")
    public void sendToKafka()  {
        System.out.println("sendToKafka开始，线程：" + Thread.currentThread().getName());
        //异步执行 asyncTaskExecutor
        System.out.println("Step 2: exec doAsyncTask ");
        long start = System.currentTimeMillis();
        try{
            Thread.sleep(30000);
        }catch (InterruptedException ex){
            ex.printStackTrace();
        }
        System.out.println("Step 3: asyncTaskExecutor exec >>>>>> :"+ (System.currentTimeMillis() - start));
    }
}
