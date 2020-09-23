package com.penglecode.xmodule.master4j.spring.transaction.declarative;

import com.penglecode.xmodule.common.util.DateTimeUtils;
import com.penglecode.xmodule.master4j.spring.common.datasource.DataSourceConfiguration;
import com.penglecode.xmodule.master4j.spring.transaction.model.Product;
import com.penglecode.xmodule.master4j.spring.transaction.programmatic.TransactionTemplateFactory;
import com.penglecode.xmodule.master4j.spring.transaction.repository.Repositories;
import com.penglecode.xmodule.master4j.spring.transaction.service.ProductService;
import com.penglecode.xmodule.master4j.spring.transaction.service.Services;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.*;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import javax.sql.DataSource;

/**
 * 基于声明式事务的实例
 *
 * @author pengpeng
 * @version 1.0
 * @date 2020/9/22 0:15
 */
public class TransactionalAnnotationExample {

    @Configuration
    @ComponentScan(basePackageClasses={Services.class, Repositories.class})
    @EnableTransactionManagement
    @Import(DataSourceConfiguration.class)
    public static class ExampleConfiguration {

        private final DataSource dataSource;

        public ExampleConfiguration(DataSource defaultDataSource) {
            this.dataSource = defaultDataSource;
        }

        @Bean
        public JdbcTemplate jdbcTemplate() {
            return new JdbcTemplate(dataSource);
        }

        @Bean
        public DataSourceTransactionManager dataSourceTransactionManager() {
            return new DataSourceTransactionManager(dataSource);
        }

        @Bean
        public TransactionTemplateFactory transactionTemplateFactory() {
            return new TransactionTemplateFactory(dataSourceTransactionManager());
        }

    }

    protected static void createProductTransactionTest() {
        AnnotationConfigApplicationContext applicationContext = new AnnotationConfigApplicationContext(ExampleConfiguration.class);
        String[] beanNames = applicationContext.getBeanDefinitionNames();
        System.out.println("=============================================All BeanDefinitions(" + beanNames.length + ")=================================================");
        for (String beanName : beanNames) {
            BeanDefinition beanDefinition = applicationContext.getBeanFactory().getBeanDefinition(beanName);
            System.out.println(beanDefinition);
        }

        ProductService productService = (ProductService) applicationContext.getBean("productService1");

        System.out.println();
        System.out.println();
        System.out.println();

        Product product = new Product();
        product.setProductName("12期免息【减620元】Huawei/华为 P40 pro+ 5G手机全网通官方旗舰店正品mate30荣耀P30直降折叠P40");
        product.setUnitPrice(7988.00);
        product.setProductUrl("https://detail.tmall.com/item.htm?spm=a230r.1.14.84.5204cec2mS9AMN&id=619558893775&ns=1&abbucket=14&sku_properties=10004:7195672376;5919063:6536025");
        product.setProductTag("华为 P40");
        product.setProductType(1);
        product.setProductDetail("12期免息【减620元】Huawei/华为 P40 pro+ 5G手机全网通官方旗舰店正品mate30荣耀P30直降折叠P40");
        product.setMainCategoryId(1L);
        product.setCreateTime(DateTimeUtils.formatNow());
        productService.createProduct(product);
    }

    protected static void updateProductTransactionTest() {
        AnnotationConfigApplicationContext applicationContext = new AnnotationConfigApplicationContext(ExampleConfiguration.class);
        ProductService productService = (ProductService) applicationContext.getBean("productService1");

        System.out.println();
        System.out.println();
        System.out.println();

        Product product = new Product();
        product.setProductId(24L);
        product.setProductName("12期免息【减620元】Huawei/华为 P40 pro+ 5G手机全网通官方旗舰店正品mate30荣耀P30直降折叠P40");
        product.setUnitPrice(7988.00);
        product.setProductUrl("https://detail.tmall.com/item.htm?spm=a230r.1.14.84.5204cec2mS9AMN&id=619558893775&ns=1&abbucket=14&sku_properties=10004:7195672376;5919063:6536025");
        product.setProductTag("华为 P40");
        product.setProductType(1);
        product.setProductDetail("12期免息【减620元】Huawei/华为 P40 pro+ 5G手机全网通官方旗舰店正品mate30荣耀P30直降折叠P40");
        product.setMainCategoryId(1L);
        product.setUpdateTime(DateTimeUtils.formatNow());
        productService.updateProduct(product);
    }

    public static void main(String[] args) {
        createProductTransactionTest();
        //updateProductTransactionTest();
    }

}
