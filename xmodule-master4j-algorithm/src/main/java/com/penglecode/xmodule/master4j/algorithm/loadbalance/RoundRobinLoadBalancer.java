package com.penglecode.xmodule.master4j.algorithm.loadbalance;

import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * 完全轮训算法
 *
 * 缺点：从头到尾轮询一遍，不能根据服务器性能设置权重
 *
 * @author pengpeng
 * @version 1.0
 * @date 2020/10/4 0:26
 */
public class RoundRobinLoadBalancer implements LoadBalancer {

    private final List<String> servers;

    private final AtomicInteger count = new AtomicInteger(0);

    public RoundRobinLoadBalancer(List<String> servers) {
        if(servers == null || servers.isEmpty()) {
            throw new IllegalArgumentException("Parameter 'servers' must be not empty!");
        }
        this.servers = new ArrayList<>(servers);
    }

    @Override
    public String getServer(String client) {
        int index = incrementAndGet();
        return servers.get(index % servers.size());
    }

    protected final int incrementAndGet() {
        int current, next;
        do {
            current = count.get();
            next = current >= Integer.MAX_VALUE ? 0 : current + 1;
        } while(!count.compareAndSet(current, next));
        return next;
    }

    public static void main(String[] args) {
        List<String> servers = Arrays.asList("192.168.137.101:8080", "192.168.137.102:8080", "192.168.137.103:8080");
        LoadBalancer loadBalancer = new RoundRobinLoadBalancer(servers);

        Map<String,Integer> map = new HashMap<>();
        for(int i = 0; i < 9999; i++) {
            String server = loadBalancer.getServer("123");
            map.merge(server, 1, (oldValue, newValue) -> oldValue + 1);
        }
        System.out.println(map);
    }

}
