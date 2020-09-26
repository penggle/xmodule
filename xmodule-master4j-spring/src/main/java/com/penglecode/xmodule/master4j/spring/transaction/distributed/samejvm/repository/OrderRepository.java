package com.penglecode.xmodule.master4j.spring.transaction.distributed.samejvm.repository;

import com.penglecode.xmodule.master4j.spring.transaction.distributed.model.Order;
import com.penglecode.xmodule.master4j.spring.transaction.distributed.model.OrderDetail;
import com.penglecode.xmodule.master4j.spring.transaction.distributed.model.Product;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import javax.annotation.Resource;
import java.util.List;

/**
 * @author pengpeng
 * @version 1.0
 * @date 2020/9/25 21:03
 */
@Repository
public class OrderRepository extends BaseRepository {

    @Resource(name="orderJdbcTemplate")
    private JdbcTemplate jdbcTemplate;

    public void insertOrder(Order order) {
        String sql = "INSERT INTO t_order (order_id, total_amount, freight_amount, customer_id, status, remark, order_time) VALUES (?, ?, ?, ?, ?, ?, ?)";
        jdbcTemplate.update(sql, new Object[] { order.getOrderId(), order.getTotalAmount(), order.getFreightAmount(), order.getCustomerId(), order.getStatus(), order.getRemark(), order.getOrderTime() });
    }

    public void insertOrderDetail(OrderDetail detail) {
        String sql = "INSERT INTO t_order_detail (order_id, product_id, product_name, product_url, unit_price, quantity, freight_amount, sub_total_amount, order_time) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";
        jdbcTemplate.update(sql, new Object[]{ detail.getOrderId(), detail.getProductId(), detail.getProductName(), detail.getProductUrl(), detail.getUnitPrice(), detail.getQuantity(), detail.getFreightAmount(), detail.getSubTotalAmount(), detail.getOrderTime() });
    }

    public Order getOrderById(Long orderId) {
        String sql = "SELECT a.order_id orderId, a.total_amount totalAmount, a.freight_amount freightAmount, a.customer_id customerId, a.status, a.remark, a.order_time orderTime FROM t_order a WHERE a.order_id = ?";
        return jdbcTemplate.queryForObject(sql, new Object[] { orderId }, new BeanPropertyRowMapper<>(Order.class));
    }

    public List<OrderDetail> getOrderDetailsByOrderId(Long orderId) {
        String sql = "SELECT a.order_id orderId, a.product_id productId, a.product_name productName, a.product_url productUrl, a.unit_price unitPrice, a.quantity, a.freight_amount freightAmount, a.sub_total_amount subTotalAmount, a.order_time orderTime FROM t_order a WHERE a.order_id = ?";
        return jdbcTemplate.query(sql, new Object[] { orderId }, new BeanPropertyRowMapper<>(OrderDetail.class));
    }

}
