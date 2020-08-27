package com.penglecode.xmodule.master4j.java.io.object;

import java.io.Serializable;

/**
 * @author pengpeng
 * @version 1.0
 * @date 2020/8/17 22:22
 */
public class Person implements Serializable {

    private Long id;

    private String name;

    private Integer age;

    private Character sex;

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

    public Integer getAge() {
        return age;
    }

    public void setAge(Integer age) {
        this.age = age;
    }

    public Character getSex() {
        return sex;
    }

    public void setSex(Character sex) {
        this.sex = sex;
    }

    @Override
    public String toString() {
        return "Person{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", age=" + age +
                ", sex=" + sex +
                '}';
    }
}
