package com.penglecode.xmodule.common.boot.config;

import javax.sql.DataSource;

import org.apache.ibatis.session.SqlSessionFactory;
import org.mybatis.spring.SqlSessionFactoryBean;
import org.mybatis.spring.boot.autoconfigure.SpringBootVFS;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.support.ResourcePatternResolver;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import com.penglecode.xmodule.common.consts.ApplicationConstants;
import com.penglecode.xmodule.common.support.BaseModel;

/**
 * springboot-mybatis 集成配置
 * 
 * @author 	pengpeng
 * @date	2018年2月3日 下午1:55:15
 */
@Configuration
@ConditionalOnBean(DataSource.class)
@ConditionalOnClass(SqlSessionFactory.class)
@EnableTransactionManagement(proxyTargetClass=true)
@ConditionalOnProperty(name="spring.mybatis.enabled", havingValue="true", matchIfMissing=false)
public class DefaultMybatisBaseConfiguration extends AbstractSpringConfiguration {

	@Bean(name="defaultSqlSessionFactory")
	@ConditionalOnMissingBean(SqlSessionFactory.class)
	public SqlSessionFactoryBean defaultSqlSessionFactory(@Qualifier("defaultDataSource") DataSource dataSource) throws Exception {
		ResourcePatternResolver defaultResourcePatternResolver = ApplicationConstants.DEFAULT_RESOURCE_PATTERN_RESOLVER.get();
		SqlSessionFactoryBean sqlSessionFactoryBean = new SqlSessionFactoryBean();
		sqlSessionFactoryBean.setDataSource(dataSource);
		sqlSessionFactoryBean.setConfigLocation(defaultResourcePatternResolver.getResource(getEnvironment().getProperty("spring.mybatis.configLocation")));
		sqlSessionFactoryBean.setTypeAliasesPackage(getEnvironment().getProperty("spring.mybatis.typeAliasesPackage"));
		sqlSessionFactoryBean.setTypeAliasesSuperType(BaseModel.class);
		sqlSessionFactoryBean.setVfs(SpringBootVFS.class);
		sqlSessionFactoryBean.setMapperLocations(defaultResourcePatternResolver.getResources(getEnvironment().getProperty("spring.mybatis.mapperLocations")));
		return sqlSessionFactoryBean;
	}
	
	@Bean(name="defaultDataSourceTransactionManager")
	public PlatformTransactionManager defaultDataSourceTransactionManager(@Qualifier("defaultDataSource") DataSource dataSource) {
		return new DataSourceTransactionManager(dataSource);
	}

}
