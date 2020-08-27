package com.penglecode.xmodule.master4j.java.io.chars;

import java.io.*;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.Map;
import java.util.Properties;

/**
 * 输出缓冲字符流
 *
 * @author pengpeng
 * @version 1.0
 * @date 2020/8/14 22:17
 */
public class BufferedWriterExample {

    public static final Charset CHARSET = StandardCharsets.UTF_8;

    public static final String LINE_SEPARATOR = System.getProperty("line.separator");

    public static void main(String[] args) {
        try(BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(new FileOutputStream("d:/system.properties"), CHARSET))) {
            Properties properties = System.getProperties();
            for(Map.Entry<Object,Object> entry : properties.entrySet()) {
                bw.write(entry.getKey().toString() + "=" + entry.getValue().toString() + LINE_SEPARATOR);
            }
        } catch (IOException e) {
            e.printStackTrace();
        } ;
    }

}
