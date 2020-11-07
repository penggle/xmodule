package com.penglecode.xmodule.master4j.algorithm.loadbalance;

import java.util.*;

/**
 * 1、完全随机算法
 * 缺点：所有服务器的访问概率都是相同的。
 *
 * @author pengpeng
 * @version 1.0
 * @date 2020/10/3 23:00
 */
public class RandomLoadBalancer implements LoadBalancer {

    private final Random random = new Random();

    private final List<String> servers;

    public RandomLoadBalancer(List<String> servers) {
        if(servers == null || servers.isEmpty()) {
            throw new IllegalArgumentException("Parameter 'servers' must be not empty!");
        }
        this.servers = new ArrayList<>(servers);
    }

    @Override
    public String getServer(String client) {
        return servers.get(random.nextInt(servers.size()));
    }

    public static void main(String[] args) {
        List<String> servers = Arrays.asList("192.168.137.101:8080", "192.168.137.102:8080", "192.168.137.103:8080");
        LoadBalancer loadBalancer = new RandomLoadBalancer(servers);

        Map<String,Integer> map = new HashMap<>();
        for(int i = 0; i < 100; i++) {
            String server = loadBalancer.getServer("123");
            map.merge(server, 1, (oldValue, newValue) -> oldValue + 1);
        }
        System.out.println(map);
    }

}
