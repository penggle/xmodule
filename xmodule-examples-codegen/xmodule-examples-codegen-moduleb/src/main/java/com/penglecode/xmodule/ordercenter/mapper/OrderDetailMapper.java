package com.penglecode.xmodule.ordercenter.mapper;

import com.penglecode.xmodule.common.mybatis.mapper.BaseMybatisMapper;
import com.penglecode.xmodule.common.support.DefaultDatabase;
import com.penglecode.xmodule.ordercenter.model.OrderDetail;

@DefaultDatabase
public interface OrderDetailMapper extends BaseMybatisMapper<OrderDetail> {
}