package com.penglecode.xmodule.master4j.java.lang.thread.classscanning;

/**
 * @author pengpeng
 * @version 1.0
 * @date 2020/8/24 17:25
 */
@Mapper
public interface UserMapper {

    public Long insertUser(User user);

    public User selectUserById(Long userId);

}
