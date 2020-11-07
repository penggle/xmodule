package com.penglecode.xmodule.master4j.algorithm.loadbalance;

import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * 加权轮询算法
 *
 * 优点：可以根据服务器性能设置访问权重
 * 缺点：可能某个服务器权重大，长时间执行，遇到耗时大的请求，压力会很大
 *
 * @author pengpeng
 * @version 1.0
 * @date 2020/10/4 0:52
 */
public class WeightedRoundRobinLoadBalancer extends WeightedRandomLoadBalancer {

    private final ThreadLocal<Integer> currentCount = new ThreadLocal<>();

    private final AtomicInteger count = new AtomicInteger(0);

    public WeightedRoundRobinLoadBalancer(Map<String, Integer> weightedServers) {
        super(weightedServers);
    }

    @Override
    public String getServer(String client) {
        try {
            return super.getServer(client);
        } finally {
            currentCount.remove();
        }
    }

    @Override
    protected int getNextIndex() {
        int count = incrementAndGet();
        currentCount.set(count);
        return count % getTotalGroupedWeights();
    }

    @Override
    protected int getGroupedIndex(List<String> groupedServers) {
        int count = currentCount.get();
        return count % groupedServers.size();
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
        Map<String,Integer> servers = new HashMap<>();
        servers.put("192.168.137.101:8080", 1);
        servers.put("192.168.137.102:8080", 8);
        servers.put("192.168.137.103:8080", 1);

        LoadBalancer loadBalancer = new WeightedRoundRobinLoadBalancer(servers);

        Map<String,Integer> map = new HashMap<>();
        for(int i = 0; i < 10000; i++) {
            String server = loadBalancer.getServer("123");
            map.merge(server, 1, (oldValue, newValue) -> oldValue + 1);
        }
        System.out.println(map);
    }

}
