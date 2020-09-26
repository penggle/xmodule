package com.penglecode.xmodule.master4j.spring.transaction.distributed.samejvm.service;

import com.penglecode.xmodule.master4j.spring.transaction.distributed.model.Order;

/**
 * @author pengpeng
 * @version 1.0
 * @date 2020/9/25 21:42
 */
public interface OrderService {

    public void createOrder(Order order);

    public Order getOrderById(Long orderId);

}
