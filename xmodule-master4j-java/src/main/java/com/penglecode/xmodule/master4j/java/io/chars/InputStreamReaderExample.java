package com.penglecode.xmodule.master4j.java.io.chars;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;

/**
 * InputStreamReader - 是字节流通向字符流的桥梁
 *
 * @author pengpeng
 * @version 1.0
 * @date 2020/8/14 21:29
 */
public class InputStreamReaderExample {

    public static final Charset CHARSET = StandardCharsets.UTF_8;

    public static void main(String[] args) {
        try(InputStreamReader isr = new InputStreamReader(new FileInputStream("d:/system.properties"), CHARSET)) {
            StringBuilder sb = new StringBuilder(256);
            int c;
            while((c = isr.read()) != -1) {
                sb.append((char)c);
            }
            System.out.println(sb.toString());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
