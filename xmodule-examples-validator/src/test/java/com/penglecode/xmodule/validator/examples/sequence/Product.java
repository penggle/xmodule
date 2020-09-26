package com.penglecode.xmodule.validator.examples.sequence;

import com.penglecode.xmodule.common.validation.validator.Enums;
import com.penglecode.xmodule.common.validation.validator.ValidationGroups.*;

import org.hibernate.validator.constraints.URL;
//import org.hibernate.validator.custom.PropertyValidateOrder;

import javax.validation.constraints.*;

/**
 * hibernate-validator验证示例模型
 *
 * @author pengpeng
 * @version 1.0
 * @date 2020/8/6 10:33
 */
public class Product {

    @Null(message="商品ID必须为空!", groups={Create.class})
    @NotNull(message = "商品ID不能为空!", groups={Update.class})
    private Long productId;

    //@PropertyValidateOrder(value = 1)
    @NotBlank(message = "商品名称不能为空!", groups={Create.class, Update.class})
    @Pattern(regexp = "[\\u4e00-\\u9fa5]+", message = "商品名称只能为中文!", groups={Create.class, Update.class})
    @Size(min = 5, max = 100, message = "商品名称最少{min}个、最多{max}个字符!", groups={Create.class, Update.class})
    private String productName;

    //@PropertyValidateOrder(value = 5)
    @NotNull(message = "商品类型不能为空!", groups={Create.class, Update.class})
    @Enums(values={"0", "1"}, message = "商品类型必须是{values}中的一个!", groups={Create.class, Update.class})
    private Integer productType;

    //@PropertyValidateOrder(value = 7)
    @NotNull(message = "商品URL不能为空!", groups={Create.class, Update.class})
    @URL(message = "商品URL不合法", groups={Create.class, Update.class})
    private String productUrl;

    @NotNull(message = "商品价格不能为空!", groups={Create.class, Update.class})
    @Min(value = 0, message = "商品价格不能小于{min}!", groups={Create.class, Update.class})
    @Max(value = 9999, message = "商品价格不能大于{max}!", groups={Create.class, Update.class})
    private Double unitPrice;

    @NotNull(message = "商品库存不能为空!", groups={Create.class, Update.class})
    @Min(value = 0, message = "商品库存不能小于{min}!", groups={Create.class, Update.class})
    @Max(value = 9999, message = "商品库存不能大于{max}!", groups={Create.class, Update.class})
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
