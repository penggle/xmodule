package com.penglecode.xmodule.master4j.spring.transaction.distributed.samejvm.repository;

import com.penglecode.xmodule.common.util.DateTimeUtils;
import com.penglecode.xmodule.master4j.spring.transaction.distributed.model.Product;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import javax.annotation.Resource;

/**
 * @author pengpeng
 * @version 1.0
 * @date 2020/9/25 21:03
 */
@Repository
public class ProductRepository extends BaseRepository {

    @Resource(name="productJdbcTemplate")
    private JdbcTemplate jdbcTemplate;

    public void insertProduct(Product product) {
        String sql = "INSERT INTO t_product (product_id, product_name, unit_price, product_url, inventory, create_time) VALUES (?, ?, ?, ?, ?, ?)";
        jdbcTemplate.update(sql, new Object[]{ product.getProductId(), product.getProductName(), product.getUnitPrice(), product.getProductUrl(), product.getInventory(), product.getCreateTime() });
    }

    public void updateProduct(Product product) {
        String sql = "UPDATE t_product SET product_name=?, unit_price=?, product_url=?, inventory=?, update_time=? WHERE product_id=?";
        jdbcTemplate.update(sql, new Object[]{ product.getProductName(), product.getUnitPrice(), product.getProductUrl(), product.getInventory(), product.getUpdateTime(), product.getProductId() });
    }

    public void updateInventory(Long productId, Integer delta) {
        String sql = "UPDATE t_product SET inventory=inventory + ?, update_time=? WHERE product_id=?";
        jdbcTemplate.update(sql, new Object[]{ delta, DateTimeUtils.formatNow(), productId });
    }

    public Product getProductById(Long productId) {
        String sql = "SELECT a.product_id productId, a.product_name productName, a.unit_price unitPrice, a.product_url productUrl, a.inventory, a.create_time createTime, a.update_time updateTime FROM t_product a WHERE a.product_id = ?";
        return jdbcTemplate.queryForObject(sql, new Object[] { productId }, new BeanPropertyRowMapper<>(Product.class));
    }

}
