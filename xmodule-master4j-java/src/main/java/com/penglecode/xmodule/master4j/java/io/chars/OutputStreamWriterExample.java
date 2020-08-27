package com.penglecode.xmodule.master4j.java.io.chars;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.Map;
import java.util.Properties;

/**
 * OutputStreamWriter 是字符流通向字节流的桥梁
 *
 * @author pengpeng
 * @version 1.0
 * @date 2020/8/14 21:59
 */
public class OutputStreamWriterExample {

    public static final Charset CHARSET = StandardCharsets.UTF_8;

    public static final String LINE_SEPARATOR = System.getProperty("line.separator");

    public static void main(String[] args) {
        try(OutputStreamWriter osw = new OutputStreamWriter(new FileOutputStream("d:/system.properties"), CHARSET)) {
            Properties properties = System.getProperties();
            for(Map.Entry<Object,Object> entry : properties.entrySet()) {
                osw.write(entry.getKey().toString() + "=" + entry.getValue().toString() + LINE_SEPARATOR);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
