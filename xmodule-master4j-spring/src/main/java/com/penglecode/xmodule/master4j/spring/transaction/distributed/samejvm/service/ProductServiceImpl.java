package com.penglecode.xmodule.master4j.spring.transaction.distributed.samejvm.service;

import com.penglecode.xmodule.master4j.spring.transaction.distributed.model.Product;
import com.penglecode.xmodule.master4j.spring.transaction.distributed.samejvm.repository.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

/**
 * @author pengpeng
 * @version 1.0
 * @date 2020/9/25 21:43
 */
@Service
public class ProductServiceImpl extends BaseService implements ProductService {

    @Autowired
    private ProductRepository productRepository;

    @Override
    @Transactional(transactionManager="productTransactionManager", propagation=Propagation.REQUIRED, rollbackFor=Exception.class)
    public void createProduct(Product product) {
        productRepository.insertProduct(product);
    }

    @Override
    public Product getProductById(Long productId) {
        return productRepository.getProductById(productId);
    }

}
