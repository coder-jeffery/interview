package com.easy.interviewweb.service;

import org.springframework.stereotype.Service;

@Service
public class FacadeService {

    public void createOrder(){
        System.out.println("createOrder：" + Thread.currentThread().getName());
        System.out.println("create order start :"+System.currentTimeMillis());
        createOrderDetails();
        System.out.println("create order end :"+System.currentTimeMillis());
    }

    public void createOrderDetails(){
        System.out.println("createOrderDetails：" + Thread.currentThread().getName());
        System.out.println("create order details start :"+System.currentTimeMillis());
        try{
            Thread.sleep(30000000);
        }catch (InterruptedException ex){
            ex.printStackTrace();
        }
        System.out.println("create order details end :"+System.currentTimeMillis());
    }
}
