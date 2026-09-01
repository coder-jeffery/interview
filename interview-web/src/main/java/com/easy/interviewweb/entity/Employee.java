package com.easy.interviewweb.entity;

import lombok.Builder;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Objects;

@Builder
public class Employee implements Serializable {
    // ✅强烈建议手动声明序列化版本号
    private static final long serialVersionUID = 1L;
    private Long id;
    private String name;
    private float salary;
    private String nickname;
    private String address;
    private String email;
    private LocalDateTime birthday;
    private String job;

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        Employee employee = (Employee) o;
        return Float.compare(salary, employee.salary) == 0 && Objects.equals(id, employee.id) && Objects.equals(name, employee.name) && Objects.equals(nickname, employee.nickname) && Objects.equals(address, employee.address) && Objects.equals(email, employee.email) && Objects.equals(birthday, employee.birthday) && Objects.equals(job, employee.job);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, salary, nickname, address, email, birthday, job);
    }

    @Override
    public String toString() {
        return "Employee{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", salary=" + salary +
                ", nickname='" + nickname + '\'' +
                ", address='" + address + '\'' +
                ", email='" + email + '\'' +
                ", birthday=" + birthday +
                ", job='" + job + '\'' +
                '}';
    }

    public Employee(Long id, String name, float salary, String nickname, String address, String email, LocalDateTime birthday, String job) {
        this.id = id;
        this.name = name;
        this.salary = salary;
        this.nickname = nickname;
        this.address = address;
        this.email = email;
        this.birthday = birthday;
        this.job = job;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public float getSalary() {
        return salary;
    }

    public void setSalary(float salary) {
        this.salary = salary;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public LocalDateTime getBirthday() {
        return birthday;
    }

    public void setBirthday(LocalDateTime birthday) {
        this.birthday = birthday;
    }

    public String getJob() {
        return job;
    }

    public void setJob(String job) {
        this.job = job;
    }
}
