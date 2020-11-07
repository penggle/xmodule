package com.penglecode.xmodule.master4j.integrates.example.mybatis.config;

import com.penglecode.xmodule.common.boot.config.AbstractSpringConfiguration;
import com.penglecode.xmodule.common.support.BaseModel;
import com.penglecode.xmodule.common.support.DefaultDatabase;
import org.apache.ibatis.session.SqlSessionFactory;
import org.mybatis.spring.SqlSessionFactoryBean;
import org.mybatis.spring.boot.autoconfigure.SpringBootVFS;
import org.mybatis.spring.mapper.MapperScannerConfigurer;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.core.io.support.ResourcePatternResolver;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import javax.sql.DataSource;

/**
 * @author pengpeng
 * @version 1.0
 * @date 2020/9/27 11:18
 */
@Configuration
@ConditionalOnClass(SqlSessionFactory.class)
@ConditionalOnBean(name="defaultDataSource")
@EnableTransactionManagement(proxyTargetClass=true)
@ConditionalOnProperty(name="spring.example.mybatis.enabled", havingValue="true", matchIfMissing=false)
public class MybatisExampleConfiguration extends AbstractSpringConfiguration {

    private final ResourcePatternResolver resourcePatternResolver = new PathMatchingResourcePatternResolver();

    @Bean
    public PlatformTransactionManager defaultDataSourceTransactionManager(DataSource defaultDataSource) {
        return new DataSourceTransactionManager(defaultDataSource);
    }

    @Bean
    public SqlSessionFactoryBean defaultSqlSessionFactory(DataSource defaultDataSource) throws Exception {
        SqlSessionFactoryBean sqlSessionFactoryBean = new SqlSessionFactoryBean();
        sqlSessionFactoryBean.setDataSource(defaultDataSource);
        sqlSessionFactoryBean.setConfigLocation(resourcePatternResolver.getResource(getEnvironment().getProperty("spring.example.mybatis.configLocation")));
        sqlSessionFactoryBean.setTypeAliasesPackage(getEnvironment().getProperty("spring.example.mybatis.typeAliasesPackage"));
        sqlSessionFactoryBean.setTypeAliasesSuperType(BaseModel.class);
        sqlSessionFactoryBean.setVfs(SpringBootVFS.class);
        sqlSessionFactoryBean.setMapperLocations(resourcePatternResolver.getResources(getEnvironment().getProperty("spring.example.mybatis.mapperLocations")));
        return sqlSessionFactoryBean;
    }

    @Bean
    public MapperScannerConfigurer defaultMapperScannerConfigurer() throws Exception {
        MapperScannerConfigurer mapperScannerConfigurer = new MapperScannerConfigurer();
        mapperScannerConfigurer.setBasePackage(getEnvironment().getProperty("spring.example.mybatis.mapperScanPackage"));
        mapperScannerConfigurer.setAnnotationClass(DefaultDatabase.class);
        mapperScannerConfigurer.setSqlSessionFactoryBeanName("defaultSqlSessionFactory");
        return mapperScannerConfigurer;
    }

}
