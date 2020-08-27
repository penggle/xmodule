package com.penglecode.xmodule.master4j.java.nio.buffer;

import sun.nio.ch.DirectBuffer;

import java.nio.ByteBuffer;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.LockSupport;

/**
 * DirectByteBuffer示例
 *
 * DirectByteBuffer，底层的数据其实是维护在操作系统的内存中，而不是jvm里，DirectByteBuffer里维护了一个引用address指向了数据，从而操作数据
 * DirectByteBuffer优点：跟外设（IO设备）打交道时会快很多，因为外设读取jvm堆里的数据时，不是直接读取的，而是把jvm里的数据读到一个内存块里，再在这个块里读取的，如果使用DirectByteBuffer，则可以省去这一步，实现zero copy（零拷贝）
 *
 * 题外：外设之所以要把jvm堆里的数据copy出来再操作，不是因为操作系统不能直接操作jvm内存，而是因为jvm在进行gc（垃圾回收）时，会对数据进行移动，一旦出现这种问题，外设就会出现数据错乱的情况
 *
 * DirectByteBuffer直接内存的释放并不是由你控制的，而是由full gc来控制的，直接内存会自己检测情况而调用system.gc()，但是如果参数中使用了DisableExplicitGC 那么这是个坑了
 * 那么full gc不触发，我想自己释放这部分内存有方法吗？可以的，
 *
 * 我们看看它的源码中DirectByteBuffer发现有一个：Cleaner，貌似是用来搞资源回收的，经过查证，的确是
 *
 * @author pengpeng
 * @version 1.0
 * @date 2020/8/18 23:02
 */
public class DirectByteBufferExample {


    public static void cleanDirectBuffer() {
        ByteBuffer buffer = ByteBuffer.allocateDirect(1024 * 1024 * 200); //分配200MB的直接内存
        System.out.println("start");

        LockSupport.parkNanos(TimeUnit.SECONDS.toNanos(10));

        if(buffer instanceof DirectBuffer) {
            System.out.println("clean direct buffer");
            DirectBuffer directBuffer = (DirectBuffer) buffer;
            directBuffer.cleaner().clean(); //清理回收直接内存
        }

        System.out.println("end");

        LockSupport.parkNanos(TimeUnit.SECONDS.toNanos(10));
    }

    public static void main(String[] args) {
        cleanDirectBuffer();
    }

}
