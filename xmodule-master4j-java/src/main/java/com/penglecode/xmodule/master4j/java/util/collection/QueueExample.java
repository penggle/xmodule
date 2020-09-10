package com.penglecode.xmodule.master4j.java.util.collection;

import java.util.LinkedList;
import java.util.Queue;

/**
 * java.util.Queue示例
 *
 * 栈,类似弹夹：先进后出FILO
 * 队列，类似排队办事：先进先出FIFO
 *
 * @author pengpeng
 * @version 1.0
 * @date 2020/8/30 22:35
 */
public class QueueExample {

    public static void main(String[] args) {
        Queue<Integer> queue = new LinkedList<>();

        for(int i = 0; i < 10; i++) {
            queue.add(i);
        }

        System.out.println(queue);

        Integer element;
        while((element = queue.poll()) != null) {
            System.out.println(element);
        }
    }

}
