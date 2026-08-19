package com.easy.interviewweb.service;

import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;

import java.util.concurrent.Executor;

@Service
public class BizService {

    //注入原生线程池
    @Resource(name = "bizThreadPool")
    private Executor bizThreadPool;

    public void doBiz() {
        try{
            bizThreadPool.execute(() -> {
                System.out.println("业务异步任务 >>>> ");
            });
        }finally {
            System.out.println("bizThreadPool end <<<");
        }
    }
}
