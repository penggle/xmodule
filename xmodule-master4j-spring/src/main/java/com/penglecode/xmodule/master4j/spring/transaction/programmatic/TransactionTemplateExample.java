package com.penglecode.xmodule.master4j.spring.transaction.programmatic;

import com.penglecode.xmodule.common.util.DateTimeUtils;
import com.penglecode.xmodule.master4j.spring.common.datasource.DataSourceConfiguration;
import com.penglecode.xmodule.master4j.spring.common.logger.LoggerUtils;
import com.penglecode.xmodule.master4j.spring.transaction.model.Product;
import com.penglecode.xmodule.master4j.spring.transaction.repository.Repositories;
import com.penglecode.xmodule.master4j.spring.transaction.service.ProductService;
import com.penglecode.xmodule.master4j.spring.transaction.service.Services;
import org.springframework.context.annotation.*;
import org.springframework.core.env.PropertySourcesPropertyResolver;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;

import javax.sql.DataSource;

/**
 * 使用TransactionTemplate进行编程式事务的示例
 *
 * @author pengpeng
 * @version 1.0
 * @date 2020/9/22 8:13
 */
public class TransactionTemplateExample {

    static {
        LoggerUtils.setLoggerLevel("org.springframework.beans", "DEBUG");
        LoggerUtils.setLoggerLevel(ClassPathScanningCandidateComponentProvider.class, "DEBUG");
        LoggerUtils.setLoggerLevel(PropertySourcesPropertyResolver.class, "DEBUG");
    }

    @Configuration
    @ComponentScan(basePackageClasses={Services.class, Repositories.class})
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
        ProductService productService = (ProductService) applicationContext.getBean("productService2");

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
        ProductService productService = (ProductService) applicationContext.getBean("productService2");

        System.out.println();
        System.out.println();
        System.out.println();

        Product product = new Product();
        product.setProductId(21L);
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
        //createProductTransactionTest();
        updateProductTransactionTest();
    }

}
