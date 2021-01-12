package com.penglecode.xmodule.usercenter.mapper;

import com.penglecode.xmodule.common.mybatis.mapper.BaseMybatisMapper;
import com.penglecode.xmodule.common.support.DefaultDatabase;
import com.penglecode.xmodule.usercenter.model.User;

@DefaultDatabase
public interface UserMapper extends BaseMybatisMapper<User> {
}