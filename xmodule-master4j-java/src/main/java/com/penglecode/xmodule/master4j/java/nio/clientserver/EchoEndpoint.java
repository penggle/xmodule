package com.penglecode.xmodule.master4j.java.nio.clientserver;

import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;

/**
 * @author pengpeng
 * @version 1.0
 * @date 2020/8/19 14:38
 */
public abstract class EchoEndpoint extends Thread {

    private final int bufferSize = 1024;

    private final Charset charset = StandardCharsets.UTF_8;

    private final String lineSeparator = "\r\n";

    public int getBufferSize() {
        return bufferSize;
    }

    public Charset getCharset() {
        return charset;
    }

    public String getLineSeparator() {
        return lineSeparator;
    }

}
