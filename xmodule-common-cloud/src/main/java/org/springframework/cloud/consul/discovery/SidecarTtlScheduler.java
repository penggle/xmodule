package org.springframework.cloud.consul.discovery;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledFuture;

import org.springframework.cloud.consul.discovery.HeartbeatProperties;
import org.springframework.cloud.consul.discovery.TtlScheduler;
import org.springframework.scheduling.TaskScheduler;
import org.springframework.scheduling.concurrent.ConcurrentTaskScheduler;

import com.ecwid.consul.v1.ConsulClient;
import com.ecwid.consul.v1.agent.model.NewService;

/**
 * sidecar服务代理异构微服务进行TTL健康检查的调度任务
 * 
 * @author 	pengpeng
 * @date 	2020年1月19日 下午12:45:16
 */
@SuppressWarnings("rawtypes")
public class SidecarTtlScheduler extends TtlScheduler {

	private final Map<String, ScheduledFuture> serviceHeartbeats = new ConcurrentHashMap<>();

	private final TaskScheduler scheduler = new ConcurrentTaskScheduler(
			Executors.newSingleThreadScheduledExecutor());

	private final HeartbeatProperties configuration;

	private final ConsulClient client;
	
	private final SidecarDelegateHeartbeatTask heartbeatTask;

	public SidecarTtlScheduler(HeartbeatProperties configuration, ConsulClient client, SidecarDelegateHeartbeatTask heartbeatTask) {
		super(configuration, client);
		this.configuration = configuration;
		this.client = client;
		this.heartbeatTask = heartbeatTask;
	}

	@Deprecated
	public void add(final NewService service) {
		add(service.getId());
	}

	/**
	 * Add a service to the checks loop.
	 * @param instanceId instance id
	 */
	public void add(String instanceId) {
		ScheduledFuture task = this.scheduler.scheduleAtFixedRate(heartbeatTask, this.configuration.computeHearbeatInterval().toStandardDuration().getMillis());
		ScheduledFuture previousTask = this.serviceHeartbeats.put(instanceId, task);
		if (previousTask != null) {
			previousTask.cancel(true);
		}
	}

	public void remove(String instanceId) {
		ScheduledFuture task = this.serviceHeartbeats.get(instanceId);
		if (task != null) {
			task.cancel(true);
		}
		this.serviceHeartbeats.remove(instanceId);
	}

	protected Map<String, ScheduledFuture> getServiceHeartbeats() {
		return serviceHeartbeats;
	}

	protected TaskScheduler getScheduler() {
		return scheduler;
	}

	protected HeartbeatProperties getConfiguration() {
		return configuration;
	}

	protected ConsulClient getClient() {
		return client;
	}

	protected SidecarDelegateHeartbeatTask getHeartbeatTask() {
		return heartbeatTask;
	}

}
