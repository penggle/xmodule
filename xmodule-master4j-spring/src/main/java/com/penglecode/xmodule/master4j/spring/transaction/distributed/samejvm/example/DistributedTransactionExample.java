package com.penglecode.xmodule.master4j.spring.transaction.distributed.samejvm.example;

import com.penglecode.xmodule.common.util.DateTimeUtils;
import com.penglecode.xmodule.master4j.spring.common.datasource.HikariDataSourceUtils;
import com.penglecode.xmodule.master4j.spring.transaction.distributed.model.Order;
import com.penglecode.xmodule.master4j.spring.transaction.distributed.model.OrderDetail;
import com.penglecode.xmodule.master4j.spring.transaction.distributed.model.Product;
import com.penglecode.xmodule.master4j.spring.transaction.distributed.samejvm.repository.BaseRepository;
import com.penglecode.xmodule.master4j.spring.transaction.distributed.samejvm.service.BaseService;
import com.penglecode.xmodule.master4j.spring.transaction.distributed.samejvm.service.OrderService;
import com.penglecode.xmodule.master4j.spring.transaction.distributed.samejvm.service.ProductService;
import org.springframework.context.annotation.*;
import org.springframework.core.env.ConfigurableEnvironment;
import org.springframework.data.transaction.ChainedTransactionManager;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import javax.sql.DataSource;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * @author pengpeng
 * @version 1.0
 * @date 2020/9/25 22:04
 */
public class DistributedTransactionExample {

    @Configuration
    @ComponentScan(basePackageClasses={BaseRepository.class, BaseService.class})
    @EnableTransactionManagement
    @PropertySource("classpath:jdbc.properties")
    public static class ExampleConfiguration {

        @Bean
        public DataSource productDataSource(ConfigurableEnvironment environment) {
            return HikariDataSourceUtils.createHikariDataSource(environment, "dtproduct");
        }

        @Bean
        public DataSource orderDataSource(ConfigurableEnvironment environment) {
            return HikariDataSourceUtils.createHikariDataSource(environment, "dtorder");
        }

        @Bean
        public JdbcTemplate productJdbcTemplate(DataSource productDataSource) {
            return new JdbcTemplate(productDataSource);
        }

        @Bean
        public JdbcTemplate orderJdbcTemplate(DataSource orderDataSource) {
            return new JdbcTemplate(orderDataSource);
        }

        @Bean
        public DataSourceTransactionManager productTransactionManager(DataSource productDataSource) {
            return new DataSourceTransactionManager(productDataSource);
        }

        @Bean
        public DataSourceTransactionManager orderTransactionManager(DataSource orderDataSource) {
            return new DataSourceTransactionManager(orderDataSource);
        }

        @Bean
        public PlatformTransactionManager multiTransactionManager(DataSourceTransactionManager productTransactionManager,
                                                                  DataSourceTransactionManager orderTransactionManager) {
            return new ChainedTransactionManager(productTransactionManager, orderTransactionManager);
        }

    }

    public static void createProduct() {
        AnnotationConfigApplicationContext applicationContext = new AnnotationConfigApplicationContext(ExampleConfiguration.class);
        ProductService productService = applicationContext.getBean(ProductService.class);

        Product product = new Product();
        product.setProductId(null);
        product.setProductName("【限时下单立减】Apple/苹果AirPods 2代无线蓝牙耳机");
        product.setUnitPrice(1099.00);
        product.setProductUrl("https://detail.tmall.com/item.htm?spm=a230r.1.14.28.20926aceKPsK89&id=589880871893&ns=1&abbucket=11");
        product.setInventory(1000);
        product.setCreateTime(DateTimeUtils.formatNow());
        productService.createProduct(product);

        product = new Product();
        product.setProductId(null);
        product.setProductName("【官方正品】华为FreeBuds3无线蓝牙耳机有线充版");
        product.setUnitPrice(749.00);
        product.setProductUrl("https://detail.tmall.com/item.htm?spm=a230r.1.14.168.20926aceKPsK89&id=616956141474&ns=1&abbucket=11&sku_properties=5919063:6536025");
        product.setInventory(1000);
        product.setCreateTime(DateTimeUtils.formatNow());
        productService.createProduct(product);

    }

    public static void getProduct() {
        AnnotationConfigApplicationContext applicationContext = new AnnotationConfigApplicationContext(ExampleConfiguration.class);
        ProductService productService = applicationContext.getBean(ProductService.class);
        OrderService orderService = applicationContext.getBean(OrderService.class);

        Product product1 = productService.getProductById(27L);
        System.out.println(">>> product1 = " + product1);
        Product product2 = productService.getProductById(28L);
        System.out.println(">>> product2 = " + product2);
        Product product3 = productService.getProductById(29L);
        System.out.println(">>> product3 = " + product3);
    }

    public static void createOrder() {
        AnnotationConfigApplicationContext applicationContext = new AnnotationConfigApplicationContext(ExampleConfiguration.class);
        ProductService productService = applicationContext.getBean(ProductService.class);
        OrderService orderService = applicationContext.getBean(OrderService.class);

        Product product1 = productService.getProductById(27L);
        System.out.println(">>> product1 = " + product1);
        Product product2 = productService.getProductById(28L);
        System.out.println(">>> product2 = " + product2);
        Product product3 = productService.getProductById(29L);
        System.out.println(">>> product3 = " + product3);

        System.out.println();
        System.out.println();
        System.out.println();

        List<Product> productList = Arrays.asList(product1, product2, product3);

        Order order = new Order();
        order.setOrderId(System.currentTimeMillis());
        order.setCustomerId(1L);
        order.setOrderTime(DateTimeUtils.formatNow());
        order.setStatus(0);
        order.setFreightAmount(0.0);
        order.setTotalAmount(0.0);
        order.setOrderDetails(new ArrayList<>());
        for(int i = 0; i < productList.size(); i++) {
            Product product = productList.get(i);
            OrderDetail detail = new OrderDetail();
            detail.setOrderId(order.getOrderId());
            detail.setProductId(product.getProductId());
            detail.setFreightAmount(5.0);
            detail.setProductName(product.getProductName());
            detail.setProductUrl(product.getProductUrl());
            detail.setUnitPrice(product.getUnitPrice());
            detail.setQuantity(i + 1);
            detail.setOrderTime(order.getOrderTime());
            detail.setSubTotalAmount(detail.getFreightAmount() + (detail.getUnitPrice() * detail.getQuantity()));

            order.setFreightAmount(order.getFreightAmount() + detail.getFreightAmount());
            order.setTotalAmount(order.getTotalAmount() + detail.getSubTotalAmount());
            order.getOrderDetails().add(detail);
        }

        orderService.createOrder(order);
    }

    public static void main(String[] args) {
        //createProduct();
        //getProduct();
        createOrder();
    }

}
