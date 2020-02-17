package com.penglecode.xmodule.keycloak.storage.user.provider;

import java.util.List;

import javax.naming.InitialContext;
import javax.sql.DataSource;

import org.jboss.logging.Logger;
import org.keycloak.Config.Scope;
import org.keycloak.component.ComponentModel;
import org.keycloak.models.KeycloakSession;
import org.keycloak.provider.ProviderConfigProperty;
import org.keycloak.provider.ProviderConfigurationBuilder;
import org.keycloak.storage.UserStorageProviderFactory;
import org.springframework.jdbc.core.JdbcTemplate;

import com.penglecode.xmodule.keycloak.storage.user.exception.KeycloakStorageException;
import com.penglecode.xmodule.keycloak.storage.user.repository.DefaultUpmsUserRepository;

/**
 * 外部基于RBAC权限体系的只读UserStorageProvider工厂
 * 
 * @author 	pengpeng
 * @date	2019年12月20日 下午3:26:21
 */
public class UpmsUserStorageProviderFactory implements UserStorageProviderFactory<UpmsUserStorageProvider> {

	private static final Logger LOGGER = Logger.getLogger(UpmsUserStorageProviderFactory.class);
	
	private static final String CONFIG_KEY_JNDI_DATASOURCE_NAME = "jndiDataSourceName";
	
	private static final String CONFIG_KEY_DEFAULT_JNDI_DATASOURCE_NAME = "defaultJndiDataSourceName";
	
	private InitialContext javaNamingContext;
	
	private String defaultJndiDataSourceName;
	
	@Override
	public UpmsUserStorageProvider create(KeycloakSession session, ComponentModel model) {
		String jndiDataSourceName = model.get(CONFIG_KEY_JNDI_DATASOURCE_NAME);
		LOGGER.info(String.format(">>> Creating %s, using jndiDataSourceName = %s", UpmsUserStorageProvider.class.getSimpleName(), jndiDataSourceName));
		DataSource dataSource = getJndiDataSource(jndiDataSourceName);
		LOGGER.info(String.format(">>> DataSource[%s] = %s", dataSource.getClass(), dataSource));
		return new UpmsUserStorageProvider(session, model, new DefaultUpmsUserRepository(new JdbcTemplate(dataSource)));
	}

	/**
	 * ID应该与<spi name="upms"/>保持一致
	 */
	@Override
	public String getId() {
		return "upms";
	}

	@Override
    public String getHelpText() {
        return "Local RBAC System (UPMS) User Storage Provider";
    }
	
	@Override
    public List<ProviderConfigProperty> getConfigProperties() {
        return ProviderConfigurationBuilder.create()
                .property(CONFIG_KEY_JNDI_DATASOURCE_NAME, CONFIG_KEY_JNDI_DATASOURCE_NAME, "The JNDI data source name in use", ProviderConfigProperty.STRING_TYPE, defaultJndiDataSourceName, null)
                .build();
    }
	
	@Override
    public void init(Scope config) {
		try {
			this.javaNamingContext = new InitialContext();
	        this.defaultJndiDataSourceName = config.get(CONFIG_KEY_DEFAULT_JNDI_DATASOURCE_NAME);
	        LOGGER.info(String.format(">>> Initializing %s, defaultJndiDataSourceName = %s", UpmsUserStorageProviderFactory.class.getSimpleName(), this.defaultJndiDataSourceName));
		} catch (Exception e) {
			throw new KeycloakStorageException(e.getMessage(), e);
		}
	}
	
	@Override
    public void close() {
		LOGGER.info(String.format("<<< Closing %s", UpmsUserStorageProviderFactory.class.getSimpleName()));
    }
	
	protected DataSource getJndiDataSource(String name) {
		try {
			return (DataSource) javaNamingContext.lookup(name);
		} catch (Exception e) {
			throw new KeycloakStorageException(e.getMessage(), e);
		}
	}
	
}