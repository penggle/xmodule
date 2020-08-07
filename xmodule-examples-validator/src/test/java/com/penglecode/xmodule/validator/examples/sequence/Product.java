package com.penglecode.xmodule.validator.examples.sequence;

import org.hibernate.validator.constraints.URL;

import javax.validation.constraints.*;

/**
 * hibernate-validator验证示例模型
 *
 * @author pengpeng
 * @version 1.0
 * @date 2020/8/6 10:33
 */
public class Product {

    @NotNull(message = "商品ID不能为空!")
    private Long productId;

    @NotBlank(message = "商品名称不能为空!")
    @Size(max = 10, message = "商品名称最多{max}个字符!")
    @Pattern(regexp = "[\\u4e00-\\u9fa5]+", message = "商品名称只能为中文!")
    private String productName;

    @NotNull(message = "商品类型不能为空!")
    private Integer productType;

    @NotNull(message = "商品URL不能为空!")
    @URL(message = "商品URL不合法")
    private String productUrl;

    @NotNull(message = "商品价格不能为空!")
    @Min(value = 0, message = "商品价格不能小于{min}!")
    @Max(value = 9999, message = "商品价格不能大于{max}!")
    private Double unitPrice;

    @NotNull(message = "商品库存不能为空!")
    @Min(value = 0, message = "商品库存不能小于{min}!")
    @Max(value = 9999, message = "商品库存不能大于{max}!")
    private Integer stocks;

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

    public Integer getProductType() {
        return productType;
    }

    public void setProductType(Integer productType) {
        this.productType = productType;
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

    public Integer getStocks() {
        return stocks;
    }

    public void setStocks(Integer stocks) {
        this.stocks = stocks;
    }

}
