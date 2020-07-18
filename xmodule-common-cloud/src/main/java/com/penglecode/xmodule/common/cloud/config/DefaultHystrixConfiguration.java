package com.penglecode.xmodule.common.cloud.config;

import java.util.function.Function;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.boot.autoconfigure.condition.AllNestedConditions;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.autoconfigure.condition.ConditionalOnWebApplication;
import org.springframework.boot.autoconfigure.condition.ConditionalOnWebApplication.Type;
import org.springframework.context.annotation.Conditional;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.core.context.SecurityContext;

import com.netflix.hystrix.Hystrix;
import com.netflix.hystrix.strategy.HystrixPlugins;
import com.netflix.hystrix.strategy.concurrency.HystrixConcurrencyStrategy;
import com.netflix.hystrix.strategy.concurrency.HystrixConcurrencyStrategyDefault;
import com.netflix.hystrix.strategy.eventnotifier.HystrixEventNotifier;
import com.netflix.hystrix.strategy.executionhook.HystrixCommandExecutionHook;
import com.netflix.hystrix.strategy.metrics.HystrixMetricsPublisher;
import com.netflix.hystrix.strategy.properties.HystrixPropertiesStrategy;
import com.penglecode.xmodule.common.boot.config.AbstractSpringConfiguration;
import com.penglecode.xmodule.common.cloud.config.DefaultHystrixConfiguration.HystrixConcurrencyCondition;
import com.penglecode.xmodule.common.cloud.hystrix.ReactiveDefaultHystrixConcurrencyStrategy;
import com.penglecode.xmodule.common.cloud.hystrix.ReactiveSecurityHystrixConcurrencyStrategy;
import com.penglecode.xmodule.common.cloud.hystrix.ServletDefaultHystrixConcurrencyStrategy;
import com.penglecode.xmodule.common.cloud.hystrix.ServletSecurityHystrixConcurrencyStrategy;

/**
 * 默认的springcloud-hystrix配置
 * 
 * 在Servlet环境下，将HttpServletRequest、HttpServletResponse、Authentication(如果引入了Spring-Security)绑定到HystrixConcurrencyContext中去，供在Hystrix的执行上下文中获取
 * 在Reactive环境下，将ServerWebExchange、Authentication(如果引入了Spring-Security)绑定到HystrixConcurrencyContext中去，供在Hystrix的执行上下文中获取
 * 
 * @author pengpeng
 * @date 2020年2月12日 下午9:12:38
 */
@Configuration
@Conditional(HystrixConcurrencyCondition.class)
@ConditionalOnClass({ Hystrix.class })
public class DefaultHystrixConfiguration extends AbstractSpringConfiguration {

	private static final Log LOGGER = LogFactory.getLog(DefaultHystrixConfiguration.class);

	@Configuration
	@ConditionalOnWebApplication(type=Type.SERVLET)
	@ConditionalOnMissingClass({"org.springframework.security.core.context.SecurityContext"})
	public static class ServletDefaultHystrixConfiguration {
		
		public ServletDefaultHystrixConfiguration(ObjectProvider<HystrixConcurrencyStrategy> existingConcurrencyStrategy) {
			registerConcurrencyStrategy(existingConcurrencyStrategy.getIfAvailable(), ServletDefaultHystrixConcurrencyStrategy::new);
		}
		
	}
	
	@Configuration
	@ConditionalOnWebApplication(type=Type.SERVLET)
	@ConditionalOnClass({SecurityContext.class})
	public static class ServletSecurityHystrixConfiguration {
		
		public ServletSecurityHystrixConfiguration(ObjectProvider<HystrixConcurrencyStrategy> existingConcurrencyStrategy) {
			registerConcurrencyStrategy(existingConcurrencyStrategy.getIfAvailable(), ServletSecurityHystrixConcurrencyStrategy::new);
		}
		
	}
	
	@Configuration
	@ConditionalOnWebApplication(type=Type.REACTIVE)
	@ConditionalOnMissingClass({"org.springframework.security.core.context.SecurityContext"})
	public static class ReactiveDefaultHystrixConfiguration {
		
		public ReactiveDefaultHystrixConfiguration(ObjectProvider<HystrixConcurrencyStrategy> existingConcurrencyStrategy) {
			registerConcurrencyStrategy(existingConcurrencyStrategy.getIfAvailable(), ReactiveDefaultHystrixConcurrencyStrategy::new);
		}
		
	}
	
	@Configuration
	@ConditionalOnWebApplication(type=Type.REACTIVE)
	@ConditionalOnClass({SecurityContext.class})
	public static class ReactiveSecurityHystrixConfiguration {
		
		public ReactiveSecurityHystrixConfiguration(ObjectProvider<HystrixConcurrencyStrategy> existingConcurrencyStrategy) {
			registerConcurrencyStrategy(existingConcurrencyStrategy.getIfAvailable(), ReactiveSecurityHystrixConcurrencyStrategy::new);
		}
		
	}
	
	protected static void registerConcurrencyStrategy(HystrixConcurrencyStrategy existingConcurrencyStrategy, Function<HystrixConcurrencyStrategy,HystrixConcurrencyStrategy> concurrencyStrategyProvider) {
		// Keeps references of existing Hystrix plugins.
		HystrixEventNotifier eventNotifier = HystrixPlugins.getInstance()
				.getEventNotifier();
		HystrixMetricsPublisher metricsPublisher = HystrixPlugins.getInstance()
				.getMetricsPublisher();
		HystrixPropertiesStrategy propertiesStrategy = HystrixPlugins.getInstance()
				.getPropertiesStrategy();
		HystrixCommandExecutionHook commandExecutionHook = HystrixPlugins.getInstance()
				.getCommandExecutionHook();
		HystrixConcurrencyStrategy concurrencyStrategy = detectRegisteredConcurrencyStrategy(existingConcurrencyStrategy);
		
		concurrencyStrategy = concurrencyStrategyProvider.apply(concurrencyStrategy);
		
		System.out.println(">>> concurrencyStrategy = " + concurrencyStrategy);
		
		HystrixPlugins.reset();

		// Registers existing plugins excepts the Concurrent Strategy plugin.
		HystrixPlugins.getInstance().registerConcurrencyStrategy(concurrencyStrategy);
		HystrixPlugins.getInstance().registerEventNotifier(eventNotifier);
		HystrixPlugins.getInstance().registerMetricsPublisher(metricsPublisher);
		HystrixPlugins.getInstance().registerPropertiesStrategy(propertiesStrategy);
		HystrixPlugins.getInstance().registerCommandExecutionHook(commandExecutionHook);
	}
	
	protected static HystrixConcurrencyStrategy detectRegisteredConcurrencyStrategy(HystrixConcurrencyStrategy existingConcurrencyStrategy) {
		HystrixConcurrencyStrategy registeredStrategy = HystrixPlugins.getInstance()
				.getConcurrencyStrategy();
		if (existingConcurrencyStrategy == null) {
			return registeredStrategy;
		}
		// Hystrix registered a default Strategy.
		if (registeredStrategy instanceof HystrixConcurrencyStrategyDefault) {
			return existingConcurrencyStrategy;
		}
		// If registeredStrategy not the default and not some use bean of
		// existingConcurrencyStrategy.
		if (!existingConcurrencyStrategy.equals(registeredStrategy)) {
			LOGGER.warn(
					"Multiple HystrixConcurrencyStrategy detected. Bean of HystrixConcurrencyStrategy was used.");
		}
		return existingConcurrencyStrategy;
	}
	
	static class HystrixConcurrencyCondition extends AllNestedConditions {

		HystrixConcurrencyCondition() {
			super(ConfigurationPhase.REGISTER_BEAN);
		}

		@ConditionalOnProperty(name = "hystrix.shareConcurrencyContext", havingValue = "true", matchIfMissing = true)
		static class ShareConcurrencyContext {

		}

	}

}
