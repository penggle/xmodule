package com.penglecode.xmodule.master4j.java.io.bytes;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.Properties;

/**
 * OutputStream - 输出字节流
 *
 * @author pengpeng
 * @version 1.0
 * @date 2020/8/14 21:00
 */
public class OutputStreamExample {

    public static final Charset CHARSET = StandardCharsets.UTF_8;

    public static void main(String[] args) throws IOException {
        byte[] lineSeparator = System.getProperty("line.separator").getBytes(CHARSET);
        try(OutputStream out = new FileOutputStream("d:/system.properties")) {
            Properties properties = System.getProperties();
            properties.forEach((name, value) -> {
                try {
                    out.write(name.toString().getBytes(CHARSET));
                    out.write('=');
                    out.write(value.toString().getBytes(CHARSET));
                    out.write(lineSeparator);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            });
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}