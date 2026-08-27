package com.easy.interviewweb.service;

import jakarta.annotation.Resource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.concurrent.Executor;

@Service
public class BizService {

    @Autowired
    KafkaService kafkaService;
    @Autowired
    FacadeService facadeService;
    //注入原生线程池
    @Resource(name = "bizThreadPool")
    private Executor bizThreadPool;

    @Resource(name = "businessAsyncExecutor")
    private Executor businessAsyncExecutor;

    public void doBiz() {
        try{
            bizThreadPool.execute(() -> {
                System.out.println("业务异步任务 >>>> ");
            });
        }finally {
            System.out.println("bizThreadPool end <<<");
        }
    }

    public void doAsyncTask(){
        System.out.println("doAsyncTask开始，线程：" + Thread.currentThread().getName());
        System.out.println("Step 1: exec doAsyncTask ");
        //方式一
        kafkaService.sendToKafka();
        System.out.println("Step 4: exec doAsyncTask ");
    }

    public void execOrder(){
        System.out.println("Step 2: exec order start :");
        businessAsyncExecutor.execute(()->{
            facadeService.createOrder();
        });
        System.out.println("Step 2: exec order end :");
    }
}
