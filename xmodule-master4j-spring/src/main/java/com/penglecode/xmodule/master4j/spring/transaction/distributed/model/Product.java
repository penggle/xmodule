package com.penglecode.xmodule.master4j.spring.transaction.distributed.model;

import com.penglecode.xmodule.common.support.BaseModel;

/**
 * 分布式事务示例之商品信息表
 *
 *
 * @author pengpeng
 * @version 1.0
 * @date 2020/9/25 20:29
 */
public class Product implements BaseModel<Product> {

    private static final long serialVersionUID = 1L;

    private Long productId;

    private String productName;

    private String productUrl;

    private Double unitPrice;

    private Integer inventory;

    private String createTime;

    private String updateTime;

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

    public Integer getInventory() {
        return inventory;
    }

    public void setInventory(Integer inventory) {
        this.inventory = inventory;
    }

    public String getCreateTime() {
        return createTime;
    }

    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }

    public String getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(String updateTime) {
        this.updateTime = updateTime;
    }

    @Override
    public String toString() {
        return "Product{" +
                "productId=" + productId +
                ", productName='" + productName + '\'' +
                ", productUrl='" + productUrl + '\'' +
                ", unitPrice=" + unitPrice +
                ", inventory=" + inventory +
                ", createTime='" + createTime + '\'' +
                ", updateTime='" + updateTime + '\'' +
                '}';
    }
}
