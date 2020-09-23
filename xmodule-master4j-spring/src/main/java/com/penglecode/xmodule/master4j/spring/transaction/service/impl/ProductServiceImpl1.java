package com.penglecode.xmodule.master4j.spring.transaction.service.impl;

import com.penglecode.xmodule.common.support.Page;
import com.penglecode.xmodule.common.util.DateTimeUtils;
import com.penglecode.xmodule.master4j.spring.transaction.model.AccessLog;
import com.penglecode.xmodule.master4j.spring.transaction.model.Product;
import com.penglecode.xmodule.master4j.spring.transaction.repository.ProductRepository;
import com.penglecode.xmodule.master4j.spring.transaction.service.AccessLogService;
import com.penglecode.xmodule.master4j.spring.transaction.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.List;

/**
 * @author pengpeng
 * @version 1.0
 * @date 2020/9/21 23:27
 */
@Service("productService1")
public class ProductServiceImpl1 implements ProductService {

    @Autowired
    private ProductRepository productRepository;

    @Resource(name="accessLogService1")
    private AccessLogService accessLogService;

    @Override
    @Transactional(propagation= Propagation.REQUIRED, rollbackFor=Exception.class)
    public void createProduct(Product product) {
        productRepository.createProduct(product);
        accessLogService.recordAccessLog1(new AccessLog("新增商品", "createProduct()", DateTimeUtils.formatNow()));
    }

    @Override
    @Transactional(propagation=Propagation.REQUIRED, rollbackFor=Exception.class)
    public void updateProduct(Product product) {
        productRepository.updateProduct(product);
        accessLogService.recordAccessLog2(new AccessLog("更新商品", "updateProduct()", DateTimeUtils.formatNow()));
    }

    @Override
    @Transactional(propagation=Propagation.REQUIRED, rollbackFor=Exception.class)
    public void deleteProductById(Long productId) {
        productRepository.deleteProductById(productId);
        accessLogService.recordAccessLog1(new AccessLog("删除商品", "deleteProductById()", DateTimeUtils.formatNow()));
    }

    @Override
    public Product getProductById(Long productId) {
        return productRepository.getProductById(productId);
    }

    @Override
    @Transactional(propagation=Propagation.NOT_SUPPORTED, readOnly=true, rollbackFor=Exception.class)
    public List<Product> getProductListByPage(Integer productType, Page page) {
        return productRepository.getProductListByPage(productType, page);
    }

}
