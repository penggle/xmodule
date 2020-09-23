package com.penglecode.xmodule.master4j.spring.beans.injection;

/**
 * @author pengpeng
 * @version 1.0
 * @date 2020/9/12 15:39
 */
public class UserRepository {

    public void save(User user) {
        System.out.println("UserRepository#save(" + user + ")");
    }

}
