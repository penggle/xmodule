package com.penglecode.xmodule.master4j.java.io.chars;

import java.io.*;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;

/**
 * 输入缓冲字符流
 *
 * @author pengpeng
 * @version 1.0
 * @date 2020/8/14 22:10
 */
public class BufferedReaderExample {

    public static final Charset CHARSET = StandardCharsets.UTF_8;

    public static void main(String[] args) {
        try(BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream("d:/system.properties"), CHARSET))) {
            String line;
            while((line = br.readLine()) != null) {
                System.out.println(line);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
