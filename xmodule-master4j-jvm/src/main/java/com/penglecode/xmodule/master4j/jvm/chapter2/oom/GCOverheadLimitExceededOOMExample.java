package com.penglecode.xmodule.master4j.jvm.chapter2.oom;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

/**
 * JVM抛出 java.lang.OutOfMemoryError: GC overhead limit exceeded
 * 错误就是发出了这样的信号: 执行垃圾收集的时间比例太大, 有效的运算量太小.
 * 默认情况下, 如果GC花费的时间超过 98%, 并且GC回收的内存少于 2%, JVM就会抛出这个错误。
 *
 * 注意, java.lang.OutOfMemoryError: GC overhead limit exceeded 错误只在连续多次 GC 都只回收了不到2%的极端情况下才会抛出。
 *
 * 其实发生java.lang.OutOfMemoryError: GC overhead limit exceeded与发生java.lang.OutOfMemoryError: Java heap space是不太确定的，
 * 比如调整了堆的大小那么有可能会是OutOfMemoryError: GC overhead limit exceeded变为java.lang.OutOfMemoryError: Java heap space
 *
 * VM Args：-Xmx20m -Xms20m -XX:+PrintCommandLineFlags
 *
 * @author pengpeng
 * @version 1.0
 * @date 2020/7/18 21:17
 */
public class GCOverheadLimitExceededOOMExample {

    public static void main(String[] args) {
        Map<String, String> map = new HashMap<>();
        while (true) {
            String str = UUID.randomUUID().toString();
            map.put(str, str);
        }
    }

}
