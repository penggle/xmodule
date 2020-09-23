package com.penglecode.xmodule.master4j.spring.transaction.model;

import com.penglecode.xmodule.common.support.BaseModel;

/**
 * 商品信息表
 *
 * @author pengpeng
 * @version 1.0
 * @date 2020/9/21 23:07
 */
public class Product implements BaseModel<Product> {

    private static final long serialVersionUID = 1L;

    private Long productId;

    private String productName;

    private Double unitPrice;

    private String productUrl;

    private String productTag;

    private Integer productType;

    private String productDetail;

    private Long mainCategoryId;

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

    public Double getUnitPrice() {
        return unitPrice;
    }

    public void setUnitPrice(Double unitPrice) {
        this.unitPrice = unitPrice;
    }

    public String getProductUrl() {
        return productUrl;
    }

    public void setProductUrl(String productUrl) {
        this.productUrl = productUrl;
    }

    public String getProductTag() {
        return productTag;
    }

    public void setProductTag(String productTag) {
        this.productTag = productTag;
    }

    public Integer getProductType() {
        return productType;
    }

    public void setProductType(Integer productType) {
        this.productType = productType;
    }

    public String getProductDetail() {
        return productDetail;
    }

    public void setProductDetail(String productDetail) {
        this.productDetail = productDetail;
    }

    public Long getMainCategoryId() {
        return mainCategoryId;
    }

    public void setMainCategoryId(Long mainCategoryId) {
        this.mainCategoryId = mainCategoryId;
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
                ", unitPrice=" + unitPrice +
                ", productUrl='" + productUrl + '\'' +
                ", productTag='" + productTag + '\'' +
                ", productType=" + productType +
                ", productDetail='" + productDetail + '\'' +
                ", mainCategoryId=" + mainCategoryId +
                ", createTime='" + createTime + '\'' +
                ", updateTime='" + updateTime + '\'' +
                '}';
    }
}
