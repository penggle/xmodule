package com.penglecode.xmodule.master4j.spring.transaction.distributed.samejvm.service;

import com.penglecode.xmodule.master4j.spring.transaction.distributed.model.Order;
import com.penglecode.xmodule.master4j.spring.transaction.distributed.model.OrderDetail;
import com.penglecode.xmodule.master4j.spring.transaction.distributed.samejvm.repository.OrderRepository;
import com.penglecode.xmodule.master4j.spring.transaction.distributed.samejvm.repository.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

/**
 * @author pengpeng
 * @version 1.0
 * @date 2020/9/25 21:44
 */
@Service
public class OrderServiceImpl extends BaseService implements OrderService {

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private OrderRepository orderRepository;

    /**
     * 同一个JVM下的分布式事务
     * 其中product和order不在同一个库中
     */
    @Override
    @Transactional(transactionManager="multiTransactionManager", propagation=Propagation.REQUIRED, rollbackFor=Exception.class)
    public void createOrder(Order order) {
        //1、向order库中写入订单数据
        orderRepository.insertOrder(order);
        for(int i = 0; i < order.getOrderDetails().size(); i++) {
            OrderDetail orderDetail = order.getOrderDetails().get(i);
            orderRepository.insertOrderDetail(orderDetail);
            if(i == 1) {
                throw new IllegalStateException("测试跨库事务回滚!");
            }
            //2、更新product库中商品的库存
            productRepository.updateInventory(orderDetail.getProductId(), -orderDetail.getQuantity());
        }
    }

    @Override
    public Order getOrderById(Long orderId) {
        Order order = orderRepository.getOrderById(orderId);
        Assert.notNull(order, "No order found for orderId: " + orderId);
        order.setOrderDetails(orderRepository.getOrderDetailsByOrderId(orderId));
        return order;
    }

}
