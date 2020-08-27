package com.penglecode.xmodule.master4j.java.io.bytes;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

/**
 * InputStream - 输入字节流
 *
 * @author pengpeng
 * @version 1.0
 * @date 2020/8/14 20:30
 */
public class InputStreamExample {

    public static void main(String[] args) {
        try (InputStream in = new FileInputStream("d:/system.properties")) {
            byte[] bytes = new byte[in.available()];
            int i = 0, b;
            while((b = in.read()) != -1) {
                bytes[i++] = Integer.valueOf(b).byteValue();
            }
            System.out.println(new String(bytes, "UTF-8"));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
