package com.penglecode.xmodule.ordercenter.mapper;

import com.penglecode.xmodule.common.mybatis.mapper.BaseMybatisMapper;
import com.penglecode.xmodule.common.support.DefaultDatabase;
import com.penglecode.xmodule.ordercenter.model.Order;

@DefaultDatabase
public interface OrderMapper extends BaseMybatisMapper<Order> {
}