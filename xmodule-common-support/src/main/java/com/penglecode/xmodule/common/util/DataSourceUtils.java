package com.penglecode.xmodule.common.util;

import org.springframework.boot.autoconfigure.jdbc.DataSourceProperties;

import javax.sql.DataSource;

/**
 * 数据源工具类
 *
 * @author pengpeng
 * @version 1.0
 * @date 2018/1/8 20:22
 */
public class DataSourceUtils {

    /**
     * 创建遵守SpringBoot自动配置参数的数据源
     * @param properties
     * @param type
     * @param <T>
     * @return
     */
    public static <T> T createDataSource(DataSourceProperties properties, Class<? extends DataSource> type) {
        return (T) properties.initializeDataSourceBuilder().type(type).build();
    }

}
