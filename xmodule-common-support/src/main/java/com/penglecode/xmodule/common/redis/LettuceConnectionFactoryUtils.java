package com.penglecode.xmodule.common.redis;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.pool2.impl.GenericObjectPoolConfig;
import org.springframework.boot.autoconfigure.data.redis.RedisProperties;
import org.springframework.boot.autoconfigure.data.redis.RedisProperties.Pool;
import org.springframework.data.redis.connection.RedisClusterConfiguration;
import org.springframework.data.redis.connection.RedisNode;
import org.springframework.data.redis.connection.RedisPassword;
import org.springframework.data.redis.connection.RedisSentinelConfiguration;
import org.springframework.data.redis.connection.RedisStandaloneConfiguration;
import org.springframework.data.redis.connection.lettuce.LettuceClientConfiguration;
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory;
import org.springframework.data.redis.connection.lettuce.LettucePoolingClientConfiguration;
import org.springframework.data.redis.connection.lettuce.LettuceClientConfiguration.LettuceClientConfigurationBuilder;
import org.springframework.util.Assert;
import org.springframework.util.StringUtils;

import io.lettuce.core.resource.ClientResources;

/**
 * 创建LettuceConnectionFactory的工具类
 * 
 * @author 	pengpeng
 * @date	2019年1月25日 上午10:41:51
 */
public class LettuceConnectionFactoryUtils {

	public static LettuceConnectionFactory createLettuceConnectionFactory(RedisProperties properties, LettuceClientConfiguration clientConfiguration) {
		RedisSentinelConfiguration sentinelConfiguration = getSentinelConfig(properties);
		if (sentinelConfiguration != null) {
			return new LettuceConnectionFactory(sentinelConfiguration, clientConfiguration);
		}
		RedisClusterConfiguration clusterConfiguration = getClusterConfiguration(properties);
		if (clusterConfiguration != null) {
			return new LettuceConnectionFactory(clusterConfiguration, clientConfiguration);
		}
		return new LettuceConnectionFactory(getStandaloneConfig(properties), clientConfiguration);
	}

	public static LettuceClientConfiguration createLettuceClientConfiguration(RedisProperties properties, ClientResources clientResources, Pool pool) {
		LettuceClientConfigurationBuilder builder = createBuilder(pool);
		applyProperties(properties, builder);
		if (StringUtils.hasText(properties.getUrl())) {
			ConnectionInfo connectionInfo = parseUrl(properties.getUrl());
			if (connectionInfo.isUseSsl()) {
				builder.useSsl();
			}
		}
		builder.clientResources(clientResources);
		return builder.build();
	}

	protected static LettuceClientConfigurationBuilder createBuilder(Pool pool) {
		if (pool == null) {
			return LettuceClientConfiguration.builder();
		}
		return new PoolBuilderFactory().createBuilder(pool);
	}

	protected static LettuceClientConfigurationBuilder applyProperties(RedisProperties properties, LettuceClientConfiguration.LettuceClientConfigurationBuilder builder) {
		if (properties.isSsl()) {
			builder.useSsl();
		}
		if (properties.getTimeout() != null) {
			builder.commandTimeout(properties.getTimeout());
		}
		if (properties.getLettuce() != null) {
			RedisProperties.Lettuce lettuce = properties.getLettuce();
			if (lettuce.getShutdownTimeout() != null
					&& !lettuce.getShutdownTimeout().isZero()) {
				builder.shutdownTimeout(properties.getLettuce().getShutdownTimeout());
			}
		}
		return builder;
	}
	
	protected static RedisStandaloneConfiguration getStandaloneConfig(RedisProperties properties) {
		RedisStandaloneConfiguration config = new RedisStandaloneConfiguration();
		if (StringUtils.hasText(properties.getUrl())) {
			ConnectionInfo connectionInfo = parseUrl(properties.getUrl());
			config.setHostName(connectionInfo.getHostName());
			config.setPort(connectionInfo.getPort());
			config.setPassword(RedisPassword.of(connectionInfo.getPassword()));
		}
		else {
			config.setHostName(properties.getHost());
			config.setPort(properties.getPort());
			config.setPassword(RedisPassword.of(properties.getPassword()));
		}
		config.setDatabase(properties.getDatabase());
		return config;
	}

	protected static RedisSentinelConfiguration getSentinelConfig(RedisProperties properties) {
		RedisProperties.Sentinel sentinelProperties = properties.getSentinel();
		if (sentinelProperties != null) {
			RedisSentinelConfiguration config = new RedisSentinelConfiguration();
			config.master(sentinelProperties.getMaster());
			config.setSentinels(createSentinels(sentinelProperties));
			if (properties.getPassword() != null) {
				config.setPassword(RedisPassword.of(properties.getPassword()));
			}
			config.setDatabase(properties.getDatabase());
			return config;
		}
		return null;
	}

	protected static RedisClusterConfiguration getClusterConfiguration(RedisProperties properties) {
		RedisProperties.Cluster clusterProperties = properties.getCluster();
		if (clusterProperties != null) {
			RedisClusterConfiguration config = new RedisClusterConfiguration(
					clusterProperties.getNodes());
			if (clusterProperties.getMaxRedirects() != null) {
				config.setMaxRedirects(clusterProperties.getMaxRedirects());
			}
			if (properties.getPassword() != null) {
				config.setPassword(RedisPassword.of(properties.getPassword()));
			}
		}
		return null;
	}

	private static List<RedisNode> createSentinels(RedisProperties.Sentinel sentinel) {
		List<RedisNode> nodes = new ArrayList<>();
		for (String node : sentinel.getNodes()) {
			try {
				String[] parts = StringUtils.split(node, ":");
				Assert.state(parts.length == 2, "Must be defined as 'host:port'");
				nodes.add(new RedisNode(parts[0], Integer.valueOf(parts[1])));
			}
			catch (RuntimeException ex) {
				throw new IllegalStateException(
						"Invalid redis sentinel " + "property '" + node + "'", ex);
			}
		}
		return nodes;
	}

	protected static ConnectionInfo parseUrl(String url) {
		try {
			URI uri = new URI(url);
			boolean useSsl = (url.startsWith("rediss://"));
			String password = null;
			if (uri.getUserInfo() != null) {
				password = uri.getUserInfo();
				int index = password.indexOf(':');
				if (index >= 0) {
					password = password.substring(index + 1);
				}
			}
			return new ConnectionInfo(uri, useSsl, password);
		}
		catch (URISyntaxException ex) {
			throw new IllegalArgumentException("Malformed url '" + url + "'", ex);
		}
	}

	protected static class ConnectionInfo {

		private final URI uri;

		private final boolean useSsl;

		private final String password;

		public ConnectionInfo(URI uri, boolean useSsl, String password) {
			this.uri = uri;
			this.useSsl = useSsl;
			this.password = password;
		}

		public boolean isUseSsl() {
			return this.useSsl;
		}

		public String getHostName() {
			return this.uri.getHost();
		}

		public int getPort() {
			return this.uri.getPort();
		}

		public String getPassword() {
			return this.password;
		}

	}
	
	/**
	 * Inner class to allow optional commons-pool2 dependency.
	 */
	private static class PoolBuilderFactory {

		public LettuceClientConfigurationBuilder createBuilder(Pool properties) {
			return LettucePoolingClientConfiguration.builder()
					.poolConfig(getPoolConfig(properties));
		}

		private GenericObjectPoolConfig<?> getPoolConfig(Pool properties) {
			GenericObjectPoolConfig<?> config = new GenericObjectPoolConfig<>();
			config.setMaxTotal(properties.getMaxActive());
			config.setMaxIdle(properties.getMaxIdle());
			config.setMinIdle(properties.getMinIdle());
			if (properties.getMaxWait() != null) {
				config.setMaxWaitMillis(properties.getMaxWait().toMillis());
			}
			return config;
		}

	}
	
}
