package com.penglecode.xmodule.master4j.algorithm.hashing.consistent;

import java.util.Map;
import java.util.function.Function;

/**
 * 一致性哈希算法
 *
 * @author pengpeng
 * @version 1.0
 * @date 2020/9/19 14:45
 */
public interface ConsistentHashingAlgorithm {

    /**
     * 添加节点
     * @param node
     */
    public void addNode(Node node);

    /**
     * 删除节点
     * @param node
     */
    public void removeNode(Node node);

    /**
     * 获取元素(key)所映射的节点
     * @param elementKey
     * @return
     */
    public Node getNodeOfElement(String elementKey);

    /**
     * 测试元素分布情况
     * @param label
     * @param elementTotal
     * @param elementProvider
     * @return
     */
    public Map<Node,Integer> testElementDistributions(int elementTotal, Function<Integer,String> elementProvider);

    /**
     * 节点抽象
     */
    public static interface Node extends Comparable<Node> {

        public String key();

        default public int compareTo(Node o) {
            return key().compareTo(o.key());
        }

    }

}
