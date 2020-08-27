package com.penglecode.xmodule.master4j.java.nio.buffer;

/**
 * HeapByteBuffer示例
 *
 * HeapByteBuffer，顾名思义，是写在jvm堆上面的一个buffer，底层的本质是一个数组，用类封装维护了很多的索引（limit/position/capacity等）
 * HeapByteBuffer优点：由于内容维护在jvm里，所以把内容写进buffer里速度会快些；并且，可以更容易回收
 * HeapByteBuffer不具备零拷贝的特性，主要原因是HeapByteBuffer的内容是维护在java堆内存中的，堆内存随时都有GC发生，GC发生时就意味着内容可能被移动
 * 但是被移动时内核并不能感知，所以会存在数据错乱的问题(即原来地址上的数据已经被挪走了，复制错了数据)。
 * 所以JVM的解决办法是将HeapByteBuffer中的数据copy到DirectByteBuffer来读写，DirectByteBuffer分配的内存不在堆中，不受GC控制。
 *
 * @author pengpeng
 * @version 1.0
 * @date 2020/8/18 21:08
 */
public class HeapByteBufferExample {



}
