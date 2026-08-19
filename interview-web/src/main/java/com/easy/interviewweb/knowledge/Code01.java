package com.easy.interviewweb.knowledge;

import lombok.Builder;

import java.util.Objects;

/**
 * Java 基础、集合:
 *
 * */
public class Code01 {

    public static void main(String[] args) {

        Integer i1 = 1000000;
        Integer i2 = 1000000;

        System.out.println(i1 == i2);
        System.out.println(i1.equals(i2));

        Person p1 = Person.builder().age(18).height(1.71f).name("jeff").weight(130).build();
        Person p2 =Person.builder().age(18).height(1.71f).name("jeff").weight(130).build();

        System.out.println(p2 == p1);
        System.out.println(p1.equals(p2));
        System.out.println(p1.hashCode() == p2.hashCode());

        /**
         * 值类型：不可以为null
         * 引用类型：可以为null
         * */
        int a1 = 10;
        Integer a2 = 10;
        System.out.println(a2.equals(a1));
        System.out.println(a1 == a1);
    }
}
/**
 * 1.`==` 和 `equals` 差在哪？为什么重写 `equals` 必须重写 `hashCode`？
 * 1.装箱和拆箱：
 * 2.经典坑：Integer 缓存池 ｜ `Integer.valueOf()` 有缓存：**‑128 ~ 127** 的对象复用；超出范围新建对象
 * 3.
 *
 * */
@Builder
class Person{

    int age;
    String name;
    float height;
    double weight;

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public float getHeight() {
        return height;
    }

    public void setHeight(float height) {
        this.height = height;
    }

    public double getWeight() {
        return weight;
    }

    public void setWeight(double weight) {
        this.weight = weight;
    }



    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        Person person = (Person) o;
        return age == person.age && Float.compare(height, person.height) == 0 && Double.compare(weight, person.weight) == 0 && Objects.equals(name, person.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(age, name, height, weight);
    }
}
