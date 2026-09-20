package com.easy.interviewweb.threadpool;

import io.lettuce.core.GeoArgs;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class TestCode {
    public static void main(String[] args) {

        List<Student> data  = new ArrayList<>();

        Student s1 = Student.builder().age(15).height(180.1f).name("jeff").build();
        Student s2 = Student.builder().age(16).height(170.1f).name("tim").build();
        Student s3 = Student.builder().age(11).height(170.1f).name("kimi").build();

        data.add(s1);
        data.add(s2);
        data.add(s3);

        var result  =  data.stream().sorted(Comparator.comparing(Student::getAge).reversed()).toList();
        System.out.println(result);
    }
}

@Getter
@Setter
@ToString
@Builder
class Student{
    String name;
    Integer age;
    float height;
}
