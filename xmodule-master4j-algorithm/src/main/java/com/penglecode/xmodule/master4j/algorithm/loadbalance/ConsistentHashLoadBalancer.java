package com.penglecode.xmodule.master4j.algorithm.loadbalance;

import com.google.common.hash.Hashing;

import java.nio.charset.StandardCharsets;
import java.util.*;

/**
 * 一致性哈希负载均衡算法
 *
 *
 * @author pengpeng
 * @version 1.0
 * @date 2020/10/4 1:21
 */
public class ConsistentHashLoadBalancer implements LoadBalancer {

    private final List<String> servers;

    public ConsistentHashLoadBalancer(List<String> servers) {
        if(servers == null || servers.isEmpty()) {
            throw new IllegalArgumentException("Parameter 'servers' must be not empty!");
        }
        this.servers = new ArrayList<>(servers);
    }

    @Override
    public String getServer(String client) {
        int index = Hashing.consistentHash(Hashing.crc32().hashString(client, StandardCharsets.UTF_8), servers.size());
        return servers.get(index);
    }

    public static void main(String[] args) {
        List<String> servers = Arrays.asList("192.168.137.101:8080", "192.168.137.102:8080", "192.168.137.103:8080");
        LoadBalancer loadBalancer = new ConsistentHashLoadBalancer(servers);

        Map<String,Integer> map = new HashMap<>();
        for(int i = 0; i < 9999; i++) {
            String server = loadBalancer.getServer("123");
            map.merge(server, 1, (oldValue, newValue) -> oldValue + 1);
        }
        System.out.println(map);
    }

}
