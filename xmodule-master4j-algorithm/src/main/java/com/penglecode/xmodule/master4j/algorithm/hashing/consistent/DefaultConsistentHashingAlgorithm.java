package com.penglecode.xmodule.master4j.algorithm.hashing.consistent;

import org.springframework.util.Assert;

import java.util.*;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantReadWriteLock;
import java.util.function.Function;

/**
 * 默认的哈希一致性算法实现
 *
 * @author pengpeng
 * @version 1.0
 * @date 2020/9/19 14:57
 */
public class DefaultConsistentHashingAlgorithm implements ConsistentHashingAlgorithm {

    private static final int DEFAULT_VIRTUAL_COPIES = 1024 * 1024;

    /**
     * 每个物理节点具有的虚拟节点副本数
     */
    private final int virtualCopies;

    /**
     * 存储物理节点
     */
    private final Set<Node> physicalNodes = new TreeSet<>();

    /**
     * 哈希值与虚拟节点的映射关系
     */
    private final TreeMap<Long,Node> virtualNodes = new TreeMap<>();

    private final ReentrantReadWriteLock readWriteLock = new ReentrantReadWriteLock();

    public DefaultConsistentHashingAlgorithm(Collection<Node> physicalNodes) {
        this(DEFAULT_VIRTUAL_COPIES, physicalNodes);
    }

    public DefaultConsistentHashingAlgorithm(int virtualCopies, Collection<Node> physicalNodes) {
        Assert.isTrue(virtualCopies > 0, "Property 'virtualCopies' must be > 1");
        Assert.notEmpty(physicalNodes, "Property 'physicalNodes' can not be empty!");
        this.virtualCopies = virtualCopies;
        for(Node physicalNode : physicalNodes) {
            addNode(physicalNode);
        }
    }

    /**
     * 32位的 Fowler-Noll-Vo 哈希算法
     * https://en.wikipedia.org/wiki/Fowler–Noll–Vo_hash_function
     */
    protected Long hashing(String key) {
        final int p = 16777619;
        long hash = 2166136261L;
        for (int idx = 0, num = key.length(); idx < num; ++idx) {
            hash = (hash ^ key.charAt(idx)) * p;
        }
        hash += hash << 13;
        hash ^= hash >> 7;
        hash += hash << 3;
        hash ^= hash >> 17;
        hash += hash << 5;

        if (hash < 0) {
            hash = Math.abs(hash);
        }
        return hash;
    }

    @Override
    public void addNode(Node node) {
        Lock lock = readWriteLock.writeLock();
        lock.lock();
        try {
            physicalNodes.add(node);
            for (int i = 0; i < virtualCopies; i++) {
                long hash = hashing(node.key() + "#" + i);
                virtualNodes.put(hash, node);
            }
        } finally {
            lock.unlock();
        }
    }

    @Override
    public void removeNode(Node node) {
        Lock lock = readWriteLock.writeLock();
        lock.lock();
        try {
            physicalNodes.remove(node);
            for (int i = 0; i < virtualCopies; i++) {
                long hash = hashing(node.key() + "#" + i);
                virtualNodes.remove(hash);
            }
        } finally {
            lock.unlock();
        }
    }

    @Override
    public Node getNodeOfElement(String elementKey) {
        Assert.hasText(elementKey, "Parameter 'elementKey' can not be empty!");
        Lock lock = readWriteLock.readLock();
        lock.lock();
        try {
            long elementHash = hashing(elementKey);
            //相当于顺时针获取当前元素key所在位置之后的虚拟节点集合
            SortedMap<Long, Node> tailMap = virtualNodes.tailMap(elementHash);
            //如果tailMap为空则说明元素key的hash值已经无限接近圆环的起点位置了
            long nodeHash = tailMap.isEmpty() ? virtualNodes.firstKey() : tailMap.firstKey();
            return virtualNodes.get(nodeHash);
        } finally {
            lock.unlock();
        }
    }

    @Override
    public Map<Node,Integer> testElementDistributions(int elementTotal, Function<Integer,String> elementProvider) {
        Map<Node,Integer> distributions = new TreeMap<>();
        String elementKey;
        for(int i = 0; i < elementTotal; i++) {
            Node node = getNodeOfElement(elementProvider.apply(i));
            distributions.merge(node, 1, Integer::sum);
        }
        return distributions;
    }

    protected static void testDistributionRates(int virtualCopies) {
        List<Node> serverNodes = Arrays.asList(
                new ServerNode("172.16.18.101"),
                new ServerNode("172.16.18.102"),
                new ServerNode("172.16.18.103"),
                new ServerNode("172.16.18.104"));
        ConsistentHashingAlgorithm hashingAlgorithm = new DefaultConsistentHashingAlgorithm(virtualCopies, serverNodes);

        Function<Integer,String> elementProvider = i -> "mytestkey-" + i;

        final int elementTotal = 65536;

        //初始情况
        System.out.println("=================================初始情况=================================");
        Map<Node,Integer> distributions1 = hashingAlgorithm.testElementDistributions(elementTotal, elementProvider);
        distributions1.forEach((node, rate) -> {
            int percent = (100 * rate / elementTotal);
            System.out.println(String.format("IP=%s : RATE=%s", node.key(), percent));
        });

        //删除物理节点
        Node node2 = new ServerNode("172.16.18.103");
        System.out.println("=================================删除节点(" + node2.key() + ")=================================");
        hashingAlgorithm.removeNode(node2);
        Map<Node,Integer> distributions2 = hashingAlgorithm.testElementDistributions(elementTotal, elementProvider);
        distributions2.forEach((node, rate) -> {
            int percent = (100 * rate / elementTotal);
            System.out.println(String.format("IP=%s : RATE=%s", node.key(), percent));
        });

        //增加物理节点
        Node node3 = new ServerNode("172.16.18.108");
        System.out.println("=================================增加节点(" + node3.key() + ")=================================");
        hashingAlgorithm.addNode(node3);
        Map<Node,Integer> distributions3 = hashingAlgorithm.testElementDistributions(elementTotal, elementProvider);
        distributions3.forEach((node, rate) -> {
            int percent = (100 * rate / elementTotal);
            System.out.println(String.format("IP=%s : RATE=%s", node.key(), percent));
        });
    }

    public static void main(String[] args) {
        //testDistributionRates(1);
        //testDistributionRates(32);
        //testDistributionRates(1024);
        testDistributionRates(1024 * 1024);
    }

}
