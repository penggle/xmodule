package com.penglecode.xmodule.master4j.algorithm.hashing.consistent;

import com.google.common.hash.Hashing;
import com.penglecode.xmodule.common.util.StringUtils;

import java.nio.charset.StandardCharsets;
import java.util.*;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantReadWriteLock;
import java.util.function.Function;
import java.util.function.Supplier;
import java.util.stream.Collectors;

/**
 * 基于Guava的哈希一致性算法
 *
 * @author pengpeng
 * @version 1.0
 * @date 2020/9/19 16:35
 */
public class GuavaConsistentHashingAlgorithm implements ConsistentHashingAlgorithm {

    /**
     * 存储物理节点
     */
    private final List<Node> nodeBuckets;

    private final ReentrantReadWriteLock readWriteLock = new ReentrantReadWriteLock();

    public GuavaConsistentHashingAlgorithm(List<Node> nodeBuckets) {
        this.nodeBuckets = new ArrayList<>(nodeBuckets);
        Collections.sort(nodeBuckets);
    }

    @Override
    public void addNode(Node node) {
        Lock lock = readWriteLock.writeLock();
        lock.lock();
        try {
            nodeBuckets.add(node);
            Collections.sort(nodeBuckets);
        } finally {
            lock.unlock();
        }
    }

    @Override
    public synchronized void removeNode(Node node) {
        Lock lock = readWriteLock.writeLock();
        lock.lock();
        try {
            nodeBuckets.remove(node);
            Collections.sort(nodeBuckets);
        } finally {
            lock.unlock();
        }
    }

    @Override
    public Node getNodeOfElement(String elementKey) {
        Lock lock = readWriteLock.readLock();
        lock.lock();
        try {
            int bucketIndex = Hashing.consistentHash(Hashing.md5().hashString(elementKey, StandardCharsets.UTF_8), nodeBuckets.size());
            return nodeBuckets.get(bucketIndex);
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

    protected static void testDistributionRates() {
        List<Node> serverNodes = Arrays.asList(
                new ServerNode("172.16.18.101"),
                new ServerNode("172.16.18.102"),
                new ServerNode("172.16.18.103"),
                new ServerNode("172.16.18.104"));
        ConsistentHashingAlgorithm hashingAlgorithm = new GuavaConsistentHashingAlgorithm(serverNodes);

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

    protected static void testDistributionHits() {
        List<Node> serverNodes = Arrays.asList(
                new ServerNode("172.16.18.101"),
                new ServerNode("172.16.18.102"),
                new ServerNode("172.16.18.103"),
                new ServerNode("172.16.18.104"));
        ConsistentHashingAlgorithm hashingAlgorithm = new GuavaConsistentHashingAlgorithm(serverNodes);

        Function<Integer,String> elementProvider = i -> "mytestkey-" + i;

        final int elementTotal = 100;
        Map<String,String> elementKeyNodeMapping = new LinkedHashMap<>();

        //初始情况
        System.out.println("=================================初始情况=================================");
        for(int i = 0; i < elementTotal; i++) {
            String elementKey = elementProvider.apply(i);
            Node hitedNode = hashingAlgorithm.getNodeOfElement(elementKey);
            elementKeyNodeMapping.put(elementKey, hitedNode.key());
            System.out.println(StringUtils.rightPad(elementKey, ' ', 13) + " : " + hitedNode.key());
        }
        System.out.println(elementKeyNodeMapping.entrySet().stream().collect(Collectors.groupingBy(Map.Entry::getValue, (Supplier<TreeMap<String, Long>>) TreeMap::new, Collectors.counting())));

        //删除物理节点
        elementKeyNodeMapping.clear();
        Node node2 = new ServerNode("172.16.18.103");
        System.out.println("=================================删除节点(" + node2.key() + ")=================================");
        hashingAlgorithm.removeNode(node2);
        for(int i = 0; i < elementTotal; i++) {
            String elementKey = elementProvider.apply(i);
            Node hitedNode = hashingAlgorithm.getNodeOfElement(elementKey);
            elementKeyNodeMapping.put(elementKey, hitedNode.key());
            System.out.println(StringUtils.rightPad(elementKey, ' ', 13) + " : " + hitedNode.key());
        }
        System.out.println(elementKeyNodeMapping.entrySet().stream().collect(Collectors.groupingBy(Map.Entry::getValue, (Supplier<TreeMap<String, Long>>) TreeMap::new, Collectors.counting())));

        //增加物理节点
        elementKeyNodeMapping.clear();
        Node node3 = new ServerNode("172.16.18.103");
        System.out.println("=================================增加节点(" + node3.key() + ")=================================");
        hashingAlgorithm.addNode(node3);
        for(int i = 0; i < elementTotal; i++) {
            String elementKey = elementProvider.apply(i);
            Node hitedNode = hashingAlgorithm.getNodeOfElement(elementKey);
            elementKeyNodeMapping.put(elementKey, hitedNode.key());
            System.out.println(StringUtils.rightPad(elementKey, ' ', 13) + " : " + hitedNode.key());
        }
        System.out.println(elementKeyNodeMapping.entrySet().stream().collect(Collectors.groupingBy(Map.Entry::getValue, (Supplier<TreeMap<String, Long>>) TreeMap::new, Collectors.counting())));
    }

    public static void main(String[] args) {
        //testDistributionRates();
        testDistributionHits();
    }
}
