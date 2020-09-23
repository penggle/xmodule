package com.penglecode.xmodule.master4j.spring.transaction.service.impl;

import com.penglecode.xmodule.common.support.Page;
import com.penglecode.xmodule.common.util.DateTimeUtils;
import com.penglecode.xmodule.master4j.spring.transaction.model.AccessLog;
import com.penglecode.xmodule.master4j.spring.transaction.model.Product;
import com.penglecode.xmodule.master4j.spring.transaction.programmatic.TransactionTemplateFactory;
import com.penglecode.xmodule.master4j.spring.transaction.repository.ProductRepository;
import com.penglecode.xmodule.master4j.spring.transaction.service.AccessLogService;
import com.penglecode.xmodule.master4j.spring.transaction.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.TransactionDefinition;
import org.springframework.transaction.support.TransactionTemplate;

import javax.annotation.Resource;
import java.util.List;

/**
 * @author pengpeng
 * @version 1.0
 * @date 2020/9/22 8:52
 */
@Service("productService2")
public class ProductServiceImpl2 implements ProductService {

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private TransactionTemplateFactory transactionTemplateFactory;

    @Resource(name="accessLogService2")
    private AccessLogService accessLogService;

    @Override
    public void createProduct(final Product product) {
        TransactionTemplate transactionTemplate = transactionTemplateFactory.createTransactionTemplate(TransactionDefinition.PROPAGATION_REQUIRED);
        transactionTemplate.executeWithoutResult(transactionStatus -> {
            productRepository.createProduct(product);
            accessLogService.recordAccessLog1(new AccessLog("新增商品", "createProduct()", DateTimeUtils.formatNow()));
        });
    }

    @Override
    public void updateProduct(final Product product) {
        TransactionTemplate transactionTemplate = transactionTemplateFactory.createTransactionTemplate(TransactionDefinition.PROPAGATION_REQUIRED);
        transactionTemplate.executeWithoutResult(transactionStatus -> {
            productRepository.updateProduct(product);
            accessLogService.recordAccessLog2(new AccessLog("更新商品", "updateProduct()", DateTimeUtils.formatNow()));
            //throw new IllegalStateException("Intentional Exception!"); //父事务因异常而回滚不会影响子事务
        });
    }

    @Override
    public void deleteProductById(final Long productId) {
        TransactionTemplate transactionTemplate = transactionTemplateFactory.createTransactionTemplate(TransactionDefinition.PROPAGATION_REQUIRED);
        transactionTemplate.executeWithoutResult(transactionStatus -> {
            productRepository.deleteProductById(productId);
            accessLogService.recordAccessLog1(new AccessLog("删除商品", "deleteProductById()", DateTimeUtils.formatNow()));
        });
    }

    @Override
    public Product getProductById(Long productId) {
        return productRepository.getProductById(productId);
    }

    @Override
    public List<Product> getProductListByPage(Integer productType, Page page) {
        TransactionTemplate transactionTemplate = transactionTemplateFactory.createTransactionTemplate(TransactionDefinition.PROPAGATION_NOT_SUPPORTED, true);
        return transactionTemplate.execute(transactionStatus -> productRepository.getProductListByPage(productType, page));
    }
}
