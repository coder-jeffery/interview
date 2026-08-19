package com.easy.interviewweb.knowledge;

import java.util.HashMap;
import java.util.HashSet;

public class Code02 {
    public static void main(String[] args) {
        HashMap h1 = new HashMap();
        h1.put(1,"Person.Student");
        h1.put(2,"Person.Teacher");
        h1.put(3,"Person.Doctor");
        h1.put(4,"Person.Master");

        HashSet hashSet = new HashSet();
        hashSet.add("Father.Son");
        hashSet.add("Father.Dauctor");

        Student student = new Student(1L);
        System.out.println(student);

        String str1  = "to be or not to be , this is a question";
        System.out.println(str1);

        // 非线程安全
        StringBuilder stringBuilder  = new StringBuilder();
        stringBuilder.append(100);

        //synchronized 线程安全
        StringBuffer stringBuffer = new StringBuffer();
        stringBuffer.append(200);

        System.out.println("stringbuilder value " + stringBuffer);
        System.out.println("stringbuffer value  " + stringBuffer);
    }

    public record Student(Long id){
    }
}





