package com.easy.interviewapi;

import java.lang.management.GarbageCollectorMXBean;
import java.lang.management.ManagementFactory;

public class GcCheck {
    public static void main(String[] args) {
        for(GarbageCollectorMXBean bean : ManagementFactory.getGarbageCollectorMXBeans()){
            System.out.println("GC名称：" + bean.getName());

        }
    }
}
