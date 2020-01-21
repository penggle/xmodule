package com.penglecode.xmodule.common.cloud.gateway.filter.factory;

import static org.springframework.cloud.gateway.support.GatewayToStringStyler.filterToStringCreator;

import java.util.function.Predicate;
import java.util.function.Supplier;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.factory.RetryGatewayFilterFactory;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ServerWebExchange;

import com.penglecode.xmodule.common.util.CollectionUtils;

import reactor.core.publisher.Mono;
import reactor.retry.Backoff;
import reactor.retry.Repeat;
import reactor.retry.RepeatContext;
import reactor.retry.Retry;
import reactor.retry.RetryContext;

/**
 * 自定义的RetryGatewayFilterFactory
 * 修改了处理statusCode与statusSeries的逻辑
 * 
 * 原来的逻辑无法满足当重试配置为series(SERVER_ERROR), statuses(BAD_GATEWAY,GATEWAY_TIMEOUT,SERVICE_UNAVAILABLE)时排除500(SERVER_INTERNAL_ERROR)的重试需求
 * 
 * @author 	pengpeng
 * @date	2019年7月16日 上午10:30:42
 */
@SuppressWarnings({"unchecked"})
public class CustomRetryGatewayFilterFactory extends RetryGatewayFilterFactory {

	protected static final Log log = LogFactory.getLog(RetryGatewayFilterFactory.class);
	
	@Override
	public GatewayFilter apply(RetryConfig retryConfig) {
		retryConfig.validate();

		Repeat<ServerWebExchange> statusCodeRepeat = null;
		if (!retryConfig.getStatuses().isEmpty() || !retryConfig.getSeries().isEmpty()) {
			Predicate<RepeatContext<ServerWebExchange>> repeatPredicate = context -> {
				ServerWebExchange exchange = context.applicationContext();
				if (exceedsMaxIterations(exchange, retryConfig)) {
					return false;
				}

				HttpStatus statusCode = exchange.getResponse().getStatusCode();

				boolean retryableStatusCode = retryConfig.getStatuses()
						.contains(statusCode);

				//修改处理statusCode与statusSeries的逻辑
				if(retryableStatusCode && !CollectionUtils.isEmpty(retryConfig.getSeries())) {
					// try the series
					retryableStatusCode = retryConfig.getSeries().stream()
							.anyMatch(series -> statusCode.series().equals(series));
				}
				/*if (!retryableStatusCode && statusCode != null) { // null status code might mean a network exception?
					// try the series
					retryableStatusCode = retryConfig.getSeries().stream()
							.anyMatch(series -> statusCode.series().equals(series));
				}*/

				final boolean finalRetryableStatusCode = retryableStatusCode;
				trace("retryableStatusCode: %b, statusCode %s, configured statuses %s, configured series %s",
						() -> finalRetryableStatusCode, () -> statusCode,
						retryConfig::getStatuses, retryConfig::getSeries);

				HttpMethod httpMethod = exchange.getRequest().getMethod();
				boolean retryableMethod = retryConfig.getMethods().contains(httpMethod);

				trace("retryableMethod: %b, httpMethod %s, configured methods %s",
						() -> retryableMethod, () -> httpMethod, retryConfig::getMethods);
				return retryableMethod && finalRetryableStatusCode;
			};

			statusCodeRepeat = Repeat.onlyIf(repeatPredicate)
					.doOnRepeat(context -> reset(context.applicationContext()));

			BackoffConfig backoff = retryConfig.getBackoff();
			if (backoff != null) {
				statusCodeRepeat = statusCodeRepeat.backoff(getBackoff(backoff));
			}
		}

		// TODO: support timeout, backoff, jitter, etc... in Builder

		Retry<ServerWebExchange> exceptionRetry = null;
		if (!retryConfig.getExceptions().isEmpty()) {
			Predicate<RetryContext<ServerWebExchange>> retryContextPredicate = context -> {
				if (exceedsMaxIterations(context.applicationContext(), retryConfig)) {
					return false;
				}

				Throwable exception = context.exception();
				for (Class<? extends Throwable> retryableClass : retryConfig
						.getExceptions()) {
					if (retryableClass.isInstance(exception) || (exception != null
							&& retryableClass.isInstance(exception.getCause()))) {
						trace("exception or its cause is retryable %s, configured exceptions %s",
								() -> getExceptionNameWithCause(exception),
								retryConfig::getExceptions);
						return true;
					}
				}
				trace("exception or its cause is not retryable %s, configured exceptions %s",
						() -> getExceptionNameWithCause(exception),
						retryConfig::getExceptions);
				return false;
			};
			exceptionRetry = Retry.onlyIf(retryContextPredicate)
					.doOnRetry(context -> reset(context.applicationContext()))
					.retryMax(retryConfig.getRetries());
			BackoffConfig backoff = retryConfig.getBackoff();
			if (backoff != null) {
				exceptionRetry = exceptionRetry.backoff(getBackoff(backoff));
			}
		}

		GatewayFilter gatewayFilter = apply(retryConfig.getRouteId(), statusCodeRepeat,
				exceptionRetry);
		return new GatewayFilter() {
			@Override
			public Mono<Void> filter(ServerWebExchange exchange,
					GatewayFilterChain chain) {
				return gatewayFilter.filter(exchange, chain);
			}

			@Override
			public String toString() {
				return filterToStringCreator(CustomRetryGatewayFilterFactory.this)
						.append("retries", retryConfig.getRetries())
						.append("series", retryConfig.getSeries())
						.append("statuses", retryConfig.getStatuses())
						.append("methods", retryConfig.getMethods())
						.append("exceptions", retryConfig.getExceptions()).toString();
			}
		};
	}
	
	protected String getExceptionNameWithCause(Throwable exception) {
		if (exception != null) {
			StringBuilder builder = new StringBuilder(exception.getClass().getName());
			Throwable cause = exception.getCause();
			if (cause != null) {
				builder.append("{cause=").append(cause.getClass().getName()).append("}");
			}
			return builder.toString();
		}
		else {
			return "null";
		}
	}

	protected Backoff getBackoff(BackoffConfig backoff) {
		return Backoff.exponential(backoff.getFirstBackoff(), backoff.getMaxBackoff(),
				backoff.getFactor(), backoff.isBasedOnPreviousValue());
	}
	
	protected void trace(String message, Supplier<Object>... argSuppliers) {
		if (log.isTraceEnabled()) {
			Object[] args = new Object[argSuppliers.length];
			int i = 0;
			for (Supplier<Object> a : argSuppliers) {
				args[i] = a.get();
				++i;
			}
			log.trace(String.format(message, args));
		}
	}
	
}
