package com.penglecode.xmodule.master4j.java.util.collection;

import com.penglecode.xmodule.common.util.ReflectionUtils;

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

/**
 * HashMap示例
 *
 * @author pengpeng
 * @version 1.0
 * @date 2020/8/12 11:12
 */
public class HashMapExample {

    public static void resizeTest1() {
        Map<String,String> env = System.getenv();
        HashMap<String,Object> map = new HashMap<>();
        print(map);
        for(Map.Entry<String,String> entry : env.entrySet()) {
            map.put(entry.getKey(), entry.getValue());
            print(map);
        }
    }

    public static void resizeTest2(int initialCapacity) {
        Map<String,String> env = System.getenv();
        HashMap<String,Object> map = new HashMap<>(initialCapacity);
        print(map);
        for(Map.Entry<String,String> entry : env.entrySet()) {
            map.put(entry.getKey(), entry.getValue());
            print(map);
        }
    }

    public static void resizeTest3() {
        Map<String,String> env = System.getenv();
        int initialCapacity = env.size();
        System.out.println("=========================使用待添加元素总个数作为initialCapacity(" + initialCapacity + ")=========================");
        /**
         * initialCapacity绝大多数情况下传进去以后并不代表HashMap中真正的capacity，因为capacity只可能是2的n次幂
         * 比如initialCapacity=60传进去后，离他最接近的且大于等于他的2的n次幂数是64，那么真正的capacity就是64，
         * 因此，在loadFactor=0.75的情况下，实际的扩容阈值threshold=64 * 0.75 = 48，即当第49个元素put进去后即会发送扩容
         *
         * 在明确知道put进入的元素总个数的情况下，这个担保绝对不发生扩容的initialCapacity计算公式是：
         *
         * initialCapacity = expectedSize/0.75 + 1
         */
        HashMap<String,Object> map1 = new HashMap<>(initialCapacity);
        print(map1);
        for(Map.Entry<String,String> entry : env.entrySet()) {
            map1.put(entry.getKey(), entry.getValue());
            print(map1);
        }

        /**
         * 在明确知道put进入的元素总个数的情况下，这个担保绝对不发生扩容的initialCapacity计算公式是：
         *
         * initialCapacity = expectedSize/0.75 + 1
         */
        initialCapacity = (int)(initialCapacity / 0.75) + 1;
        System.out.println("=========================使用待添加元素总个数作为initialCapacity(" + initialCapacity + ")=========================");
        HashMap<String,Object> map2 = new HashMap<>(initialCapacity);
        print(map2);
        for(Map.Entry<String,String> entry : env.entrySet()) {
            map2.put(entry.getKey(), entry.getValue());
            print(map2);
        }
    }

    private static void print(HashMap<String,Object> map) {
        int size = map.size();
        int threshold = ReflectionUtils.getFieldValue(map, "threshold");
        float loadFactor = ReflectionUtils.getFieldValue(map, "loadFactor");
        Object[] table = ReflectionUtils.getFieldValue(map, "table");
        Method capacityMethod = ReflectionUtils.findMethod(HashMap.class, "capacity");
        capacityMethod.setAccessible(true);
        int capacity = (int) ReflectionUtils.invokeMethod(capacityMethod, map);
        System.out.println(String.format("size = %s, threshold = %s, capacity = %s, table.length = %s, loadFactor = %s", size, threshold, capacity, table == null ? 0 : table.length, loadFactor));
    }

    public static void main(String[] args) {
        //resizeTest1();
        //resizeTest2(20);
        //resizeTest3();
    }

}
