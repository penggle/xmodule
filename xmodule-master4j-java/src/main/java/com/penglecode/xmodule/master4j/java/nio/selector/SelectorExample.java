package com.penglecode.xmodule.master4j.java.nio.selector;

import java.nio.channels.SelectableChannel;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.util.Iterator;
import java.util.Set;

/**
 * Selector（选择器）是Java NIO中能够检测一到多个NIO通道，并能够知晓通道是否为诸如读写事件做好准备的组件。
 * 这样，一个单独的线程可以管理多个channel，从而管理多个网络连接。
 *
 * 向Selector注册通道：
 * SelectionKey key = channel.register(selector, Selectionkey.OP_XXX);
 *
 * register()方法的第二个参数。这是一个“interest集合”，意思是在通过Selector监听Channel时对什么事件感兴趣。
 * 通道触发了一个事件意思是该事件已经就绪
 * 可以监听四种不同类型的事件：
 *  1、SelectionKey.OP_CONNECT   对客户端来说，某个客户端channel成功连接到另一个服务器称为“连接就绪”
 *  2、SelectionKey.OP_ACCEPT    一个服务端channel准备好接收新进入的客户端连接称为“接收就绪”
 *  3、SelectionKey.OP_READ      一个有数据可读的通道可以说是“读就绪”
 *  4、SelectionKey.OP_WRITE     等待写数据的通道可以说是“写就绪”
 *
 * 详细请参阅：http://ifeve.com/selectors/
 *
 * @author pengpeng
 * @version 1.0
 * @date 2020/8/19 10:33
 */
public class SelectorExample {

    /**
     * 以下是示例代码并不能直接运行
     */
    public static void selectorUsage() throws Exception {
        SelectableChannel channel = null;

        Selector selector = Selector.open();
        channel.configureBlocking(false);
        SelectionKey interestkey = channel.register(selector, SelectionKey.OP_READ);
        while(true) {
            if(selector.select() > 0) { //has channel ready?
                Set<SelectionKey> selectedKeys = selector.selectedKeys();
                Iterator<SelectionKey> keyIterator = selectedKeys.iterator();
                while(keyIterator.hasNext()) {
                    SelectionKey key = keyIterator.next();
                    if(key.isAcceptable()) {
                        // a connection was accepted by a ServerSocketChannel.
                    } else if (key.isConnectable()) {
                        // a connection was established with a remote server.
                    } else if (key.isReadable()) {
                        // a channel is ready for reading
                    } else if (key.isWritable()) {
                        // a channel is ready for writing
                    }
                    keyIterator.remove();
                }
            }
        }
    }

}
