package com.penglecode.xmodule.master4j.netty.codec;

/**
 * 基于换行符的编码器
 *
 * @author pengpeng
 * @version 1.0
 * @date 2020/8/22 0:00
 */
public class LineEncoder extends DelimiterEncoder {

    public LineEncoder() {
        super(System.getProperty("line.separator", "\n"));
    }

}
