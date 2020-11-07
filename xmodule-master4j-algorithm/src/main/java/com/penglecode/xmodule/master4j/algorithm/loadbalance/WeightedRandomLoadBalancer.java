package com.penglecode.xmodule.master4j.algorithm.loadbalance;

import java.util.*;

/**
 * 加权随机负载均衡算法
 *
 * 场景：有的服务器性能高，可以让随机到此服务器的可能性增大
 *
 * 缺点：权重低的服务器可能很长一段时间都访问不到
 *
 *
 * @author pengpeng
 * @version 1.0
 * @date 2020/10/3 23:15
 */
public class WeightedRandomLoadBalancer implements LoadBalancer {

    private final Random random = new Random();

    /**
     * 按权重分组
     */
    private final TreeMap<Integer,List<String>> weightGroupedServers = new TreeMap<>();

    /**
     * 分组后的权重综合，即权重distinct后的总和
     */
    private final int totalGroupedWeights;

    public WeightedRandomLoadBalancer(Map<String,Integer> weightedServers) {
        if(weightedServers == null || weightedServers.isEmpty()) {
            throw new IllegalArgumentException("Parameter 'servers' must be not empty!");
        }
        int totalGroupedWeights = 0;
        //按权重分组
        for(Map.Entry<String,Integer> entry : weightedServers.entrySet()) {
            List<String> groupedServers = weightGroupedServers.get(entry.getValue());
            if(groupedServers == null) {
                groupedServers = new ArrayList<>();
                weightGroupedServers.put(entry.getValue(), groupedServers);
                totalGroupedWeights += entry.getValue();
            }
            groupedServers.add(entry.getKey());
        }
        this.totalGroupedWeights = totalGroupedWeights;
    }

    @Override
    public String getServer(String client) {
        int index = getNextIndex();
        int minGroupedWeight = Integer.MAX_VALUE;
        do {
            for(Map.Entry<Integer,List<String>> entry : weightGroupedServers.entrySet()) {
                //如果权重大于索引，则就返回这个服务组中的随机一个
                if(entry.getKey() >= index) {
                    List<String> groupedServers = entry.getValue();
                    if(groupedServers.size() > 1) {
                        return groupedServers.get(getGroupedIndex(groupedServers));
                    } else {
                        return groupedServers.get(0);
                    }
                }
                minGroupedWeight = Math.min(minGroupedWeight, entry.getKey());
            }
            index = index - minGroupedWeight;
        } while (index >= 0);
        throw new IllegalStateException("Unreachable Code");
    }

    protected int getNextIndex() {
        return random.nextInt(totalGroupedWeights);
    }

    protected int getGroupedIndex(List<String> groupedServers) {
        return random.nextInt(groupedServers.size());
    }

    protected int getTotalGroupedWeights() {
        return totalGroupedWeights;
    }

    public static void main(String[] args) {
        Map<String,Integer> servers = new HashMap<>();
        servers.put("192.168.137.101:8080", 1);
        servers.put("192.168.137.102:8080", 8);
        servers.put("192.168.137.103:8080", 1);

        LoadBalancer loadBalancer = new WeightedRandomLoadBalancer(servers);

        Map<String,Integer> map = new HashMap<>();
        for(int i = 0; i < 10000; i++) {
            String server = loadBalancer.getServer("123");
            map.merge(server, 1, (oldValue, newValue) -> oldValue + 1);
        }
        System.out.println(map);
    }

}
