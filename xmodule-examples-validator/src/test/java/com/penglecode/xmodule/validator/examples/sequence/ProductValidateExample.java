package com.penglecode.xmodule.validator.examples.sequence;

import com.penglecode.xmodule.common.util.CollectionUtils;
import com.penglecode.xmodule.common.validation.validator.ValidationGroups.*;
import com.penglecode.xmodule.validator.examples.AbstractValidatorExample;
import org.junit.Test;

import javax.validation.ConstraintViolation;
import java.util.Set;

/**
 * Product数据模型验证示例
 *
 * @author pengpeng
 * @version 1.0
 * @date 2020/8/6 11:15
 */
public class ProductValidateExample extends AbstractValidatorExample {


    public ProductValidateExample() {
        super(true);
    }

    @Test
    public void validate4Create() {
        Product product = new Product();
        product.setProductId(null);
        product.setProductName("asd");
        product.setProductUrl("aaa");
        product.setProductType(2);
        product.setStocks(1);
        product.setUnitPrice(1234.0);

        Set<ConstraintViolation<Product>> results = getValidator().validate(product, Create.class);
        if(!CollectionUtils.isEmpty(results)) {
            results.forEach(System.out::println);
        }
    }

}
