package com.penglecode.xmodule.master4j.spring.transaction.service;

import com.penglecode.xmodule.common.support.Page;
import com.penglecode.xmodule.master4j.spring.transaction.model.Product;

import java.util.List;

/**
 * @author pengpeng
 * @version 1.0
 * @date 2020/9/21 23:15
 */
public interface ProductService {

    public void createProduct(Product product);

    public void updateProduct(Product product);

    public void deleteProductById(Long productId);

    public Product getProductById(Long productId);

    public List<Product> getProductListByPage(Integer productType, Page page);

}
