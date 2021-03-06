package com.penglecode.xmodule.common.boot.config;

import com.penglecode.xmodule.common.consts.ApplicationConstants;
import com.penglecode.xmodule.common.util.ArrayUtils;
import com.penglecode.xmodule.common.util.CollectionUtils;
import com.penglecode.xmodule.common.util.StringUtils;
import org.apache.catalina.Context;
import org.apache.catalina.Lifecycle;
import org.apache.catalina.LifecycleEvent;
import org.apache.catalina.LifecycleListener;
import org.apache.catalina.WebResourceRoot.ResourceSetType;
import org.apache.catalina.connector.Connector;
import org.apache.catalina.startup.Tomcat;
import org.apache.coyote.http11.Http11Nio2Protocol;
import org.apache.tomcat.util.scan.StandardJarScanner;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.autoconfigure.condition.ConditionalOnWebApplication;
import org.springframework.boot.autoconfigure.condition.ConditionalOnWebApplication.Type;
import org.springframework.boot.autoconfigure.web.ServerProperties;
import org.springframework.boot.web.embedded.tomcat.TomcatConnectorCustomizer;
import org.springframework.boot.web.embedded.tomcat.TomcatContextCustomizer;
import org.springframework.boot.web.embedded.tomcat.TomcatServletWebServerFactory;
import org.springframework.boot.web.servlet.server.AbstractServletWebServerFactory;
import org.springframework.boot.web.servlet.server.Session;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.Resource;

import java.io.IOException;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.*;

/**
 * Servlet容器普通配置(see web.xml)
 * 
 * @author pengpeng
 * @date 2018年2月3日 下午12:33:22
 */
@Configuration
@ConditionalOnClass({ Tomcat.class })
@ConditionalOnWebApplication(type = Type.SERVLET)
@ConditionalOnProperty(name=DefaultServletWebServerConfiguration.CONFIGURATION_ENABLED, havingValue="true", matchIfMissing=true)
public class DefaultServletWebServerConfiguration extends AbstractSpringConfiguration {

	private static final Logger LOGGER = LoggerFactory.getLogger(DefaultServletWebServerConfiguration.class);
	
	public static final String CONFIGURATION_ENABLED = "server.tomcat.customized.enabled";
	
	@Bean
	public ServerPropertiesBeanPostProcessor serverPropertiesBeanPostProcessor() {
		return new ServerPropertiesBeanPostProcessor();
	}
	
	@Bean
	public AbstractServletWebServerFactory tomcatServletWebServerFactory() {
		TomcatServletWebServerFactory tomcatContainerFactory = new TomcatServletWebServerFactory();
		tomcatContainerFactory.setProtocol("org.apache.coyote.http11.Http11Nio2Protocol");
		tomcatContainerFactory.setUriEncoding(StandardCharsets.UTF_8);
		tomcatContainerFactory.addConnectorCustomizers(new CustomTomcatConnectorCustomizer());
		tomcatContainerFactory.addContextCustomizers(new CustomTomcatContextCustomizer());
		return tomcatContainerFactory;
	}

	class CustomTomcatContextCustomizer implements TomcatContextCustomizer {

		public void customize(Context context) {
			context.addLifecycleListener(new TomcatStaticResourceConfigurer(context));
			((StandardJarScanner) context.getJarScanner()).setScanManifest(false);
		}

	}

	class CustomTomcatConnectorCustomizer implements TomcatConnectorCustomizer {

		@Override
		public void customize(Connector connector) {
			Http11Nio2Protocol protocol = (Http11Nio2Protocol) connector.getProtocolHandler();
			// 设置最大连接数
			protocol.setMaxConnections(2000);
			// 设置最大线程数
			protocol.setMaxThreads(1000);
			// 连接超时时间
			protocol.setConnectionTimeout(20000);
		}

	}

	class TomcatStaticResourceConfigurer implements LifecycleListener {

		private final Context context;

		private TomcatStaticResourceConfigurer(Context context) {
			this.context = context;
		}

		@Override
		public void lifecycleEvent(LifecycleEvent event) {
			if (event.getType().equals(Lifecycle.CONFIGURE_START_EVENT)) {
				try {
					Resource[] resources = ApplicationConstants.DEFAULT_RESOURCE_PATTERN_RESOLVER.get().getResources("classpath*:/META-INF/resources/**");
					Set<String> metaInfJarLocations = new LinkedHashSet<String>();
					if(!ArrayUtils.isEmpty(resources)) {
						for(Resource resource : resources) {
							metaInfJarLocations.addAll(getMetaInfJarLocations(resource));
						}
					}
					for(String location : metaInfJarLocations) {
						try {
							this.context.getResources().createWebResourceSet(ResourceSetType.RESOURCE_JAR, "/", new URL(location), "/META-INF/resources");
						} catch (Exception e) {
							LOGGER.error("Create web resource set for [{}] failed : {}", location, e.getMessage());
						}
					}
				} catch (IOException e) {
					LOGGER.error(e.getMessage(), e);
				}
			}
		}
		
		protected List<String> getMetaInfJarLocations(Resource resource) {
			List<String> locations = new ArrayList<String>();
			try {
				URL url = resource.getURL();
				String location = url.toString();
				String part = "/META-INF/resources";
				int index = location.lastIndexOf(part);
				if(index != -1) {
					location = location.substring(0, index);
					location = StringUtils.stripEnd(location, "!/");
					if(location.endsWith(".jar")) { //jar
						location = location + "!/";
					}
					locations.add(location); //add jar in fat jar's lib dir
				}
			} catch (IOException e) {
				LOGGER.error(e.getMessage(), e);
			}
			return locations;
		}
		
	}
	
	@SuppressWarnings("unchecked")
	class ServerPropertiesBeanPostProcessor implements BeanPostProcessor {

		@Override
		public Object postProcessAfterInitialization(Object bean, String beanName) throws BeansException {
			if(bean instanceof ServerProperties) {
				ServerProperties serverProperties = (ServerProperties) bean;
				Session session = serverProperties.getServlet().getSession();
				/**
				 * 在不显示指定Servlet会话跟踪模式(session.trackingModes)的情况下，将其设置为空集合，
				 * 以解决无状态应用中HTTP请求仍然出现基于URL重写的JSESSIONID或基于Cookie的JSESSIONID的问题
				 */
				if(CollectionUtils.isEmpty(session.getTrackingModes())) {
					session.setTrackingModes(Collections.EMPTY_SET);
				}
			}
			return BeanPostProcessor.super.postProcessAfterInitialization(bean, beanName);
		}
		
	}
	
}
