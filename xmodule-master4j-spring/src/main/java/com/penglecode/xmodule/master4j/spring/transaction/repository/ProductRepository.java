package com.penglecode.xmodule.master4j.spring.transaction.repository;

import com.penglecode.xmodule.common.support.Page;
import com.penglecode.xmodule.master4j.spring.transaction.model.Product;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;

/**
 * @author pengpeng
 * @version 1.0
 * @date 2020/9/22 8:27
 */
@Repository
public class ProductRepository {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    public void createProduct(Product product) {
        String sql = "INSERT INTO t_product (product_id, product_name, unit_price, product_url, product_tag, product_type, product_detail, main_category_id, create_time, update_time) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
        int effectRows = jdbcTemplate.update(sql, new Object[] {product.getProductId(), product.getProductName(), product.getUnitPrice(), product.getProductUrl(), product.getProductTag(), product.getProductType(), product.getProductDetail(), product.getMainCategoryId(), product.getCreateTime(), product.getUpdateTime()});
        //fireException(effectRows);
    }

    protected void fireException(int effectRows) {
        if(effectRows == 1) {
            throw new IllegalStateException("Intentional Exception!");
        }
    }

    public void updateProduct(Product product) {
        String sql = "UPDATE t_product SET product_name=?, unit_price=?, product_url=?, product_tag=?, product_type=?, product_detail=?, main_category_id=?, update_time=? WHERE product_id=?";
        jdbcTemplate.update(sql, new Object[] {product.getProductName(), product.getUnitPrice(), product.getProductUrl(), product.getProductTag(), product.getProductType(), product.getProductDetail(), product.getMainCategoryId(), product.getUpdateTime(), product.getProductId()});
    }

    public void deleteProductById(Long productId) {
        String sql = "DELETE FROM t_product WHERE product_id = ?";
        jdbcTemplate.update(sql, new Object[] { productId });
    }

    public Product getProductById(Long productId) {
        String sql = "SELECT a.product_id productId, a.product_name productName, a.unit_price unitPrice, a.product_url productUrl, a.product_tag productTag, a.product_type productType, a.product_detail productDetail, a.main_category_id mainCategoryId, a.create_time createTime, a.update_time updateTime FROM t_product a WHERE a.product_id = ?";
        return jdbcTemplate.queryForObject(sql, new Object[] {productId}, new BeanPropertyRowMapper<>(Product.class));
    }

    public List<Product> getProductListByPage(Integer productType, Page page) {
        List<Object> queryParams = new ArrayList<>();
        List<Object> countParams = new ArrayList<>();
        StringBuilder querySql = new StringBuilder("SELECT a.product_id productId, a.product_name productName, a.unit_price unitPrice, a.product_url productUrl, a.product_tag productTag, a.product_type productType, a.product_detail productDetail, a.main_category_id mainCategoryId, a.create_time createTime, a.update_time updateTime FROM t_product a WHERE 1=1");
        StringBuilder countSql = new StringBuilder("SELECT count(*) FROM t_product a WHERE 1=1");
        if(productType != null) {
            querySql.append(" AND a.product_type = ?");
            countSql.append(" AND a.product_type = ?");
            queryParams.add(productType);
            countParams.add(productType);
        }
        querySql.append(" ORDER BY a.create_time ASC LIMIT ? OFFSET ?");
        queryParams.add(page.getLimit(), page.getOffset());

        page.setTotalRowCount(jdbcTemplate.queryForObject(countSql.toString(), countParams.toArray(), Integer.class));
        return jdbcTemplate.query(querySql.toString(), queryParams.toArray(), new BeanPropertyRowMapper<>(Product.class));
    }

}
