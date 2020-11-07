package com.penglecode.xmodule.master4j.algorithm.loadbalance;

/**
 * 负载均衡算法
 *
 * @author pengpeng
 * @version 1.0
 * @date 2020/10/3 22:55
 */
public interface LoadBalancer {

    public String getServer(String client);

}
