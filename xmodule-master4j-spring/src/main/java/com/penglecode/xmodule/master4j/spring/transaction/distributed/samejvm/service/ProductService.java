package com.penglecode.xmodule.master4j.spring.transaction.distributed.samejvm.service;

import com.penglecode.xmodule.master4j.spring.transaction.distributed.model.Product;

/**
 * @author pengpeng
 * @version 1.0
 * @date 2020/9/25 21:42
 */
public interface ProductService {

    public void createProduct(Product product);

    public Product getProductById(Long productId);

}
