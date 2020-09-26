package com.penglecode.xmodule.master4j.spring.transaction.distributed.model;

import com.penglecode.xmodule.common.support.BaseModel;

import java.util.List;

/**
 * 分布式事务示例之订单主表表
 *
 *
 * @author pengpeng
 * @version 1.0
 * @date 2020/9/25 20:32
 */
public class Order implements BaseModel<Order> {

    private static final long serialVersionUID = 1L;

    private Long orderId;

    private Double totalAmount;

    private Double freightAmount;

    private Long customerId;

    private Integer status;

    private String remark;

    private String orderTime;

    private List<OrderDetail> orderDetails;

    public Long getOrderId() {
        return orderId;
    }

    public void setOrderId(Long orderId) {
        this.orderId = orderId;
    }

    public Double getTotalAmount() {
        return totalAmount;
    }

    public void setTotalAmount(Double totalAmount) {
        this.totalAmount = totalAmount;
    }

    public Double getFreightAmount() {
        return freightAmount;
    }

    public void setFreightAmount(Double freightAmount) {
        this.freightAmount = freightAmount;
    }

    public Long getCustomerId() {
        return customerId;
    }

    public void setCustomerId(Long customerId) {
        this.customerId = customerId;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public String getOrderTime() {
        return orderTime;
    }

    public void setOrderTime(String orderTime) {
        this.orderTime = orderTime;
    }

    public List<OrderDetail> getOrderDetails() {
        return orderDetails;
    }

    public void setOrderDetails(List<OrderDetail> orderDetails) {
        this.orderDetails = orderDetails;
    }

    @Override
    public String toString() {
        return "Order{" +
                "orderId=" + orderId +
                ", totalAmount=" + totalAmount +
                ", freightAmount=" + freightAmount +
                ", customerId=" + customerId +
                ", status=" + status +
                ", remark='" + remark + '\'' +
                ", orderTime='" + orderTime + '\'' +
                '}';
    }
}
