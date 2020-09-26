package com.penglecode.xmodule.master4j.spring.transaction.distributed.model;

import com.penglecode.xmodule.common.support.BaseModel;

/**
 * 分布式事务示例之订单字表
 *
 * @author pengpeng
 * @version 1.0
 * @date 2020/9/25 20:37
 */
public class OrderDetail implements BaseModel<OrderDetail> {

    private static final long serialVersionUID = 1L;

    private Long orderId;

    private Long productId;

    private String productName;

    private String productUrl;

    private Double unitPrice;

    private Integer quantity;

    private Double freightAmount;

    private Double subTotalAmount;

    private String orderTime;

    public Long getOrderId() {
        return orderId;
    }

    public void setOrderId(Long orderId) {
        this.orderId = orderId;
    }

    public Long getProductId() {
        return productId;
    }

    public void setProductId(Long productId) {
        this.productId = productId;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public String getProductUrl() {
        return productUrl;
    }

    public void setProductUrl(String productUrl) {
        this.productUrl = productUrl;
    }

    public Double getUnitPrice() {
        return unitPrice;
    }

    public void setUnitPrice(Double unitPrice) {
        this.unitPrice = unitPrice;
    }

    public Integer getQuantity() {
        return quantity;
    }

    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }

    public Double getFreightAmount() {
        return freightAmount;
    }

    public void setFreightAmount(Double freightAmount) {
        this.freightAmount = freightAmount;
    }

    public Double getSubTotalAmount() {
        return subTotalAmount;
    }

    public void setSubTotalAmount(Double subTotalAmount) {
        this.subTotalAmount = subTotalAmount;
    }

    public String getOrderTime() {
        return orderTime;
    }

    public void setOrderTime(String orderTime) {
        this.orderTime = orderTime;
    }

    @Override
    public String toString() {
        return "OrderDetail{" +
                "orderId=" + orderId +
                ", productId=" + productId +
                ", productName='" + productName + '\'' +
                ", productUrl='" + productUrl + '\'' +
                ", unitPrice=" + unitPrice +
                ", quantity=" + quantity +
                ", freightAmount=" + freightAmount +
                ", subTotalAmount=" + subTotalAmount +
                ", orderTime='" + orderTime + '\'' +
                '}';
    }
}
