package com.penglecode.xmodule.master4j.java.io.object;

import com.penglecode.xmodule.common.util.FileUtils;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.nio.file.Files;
import java.nio.file.Paths;

/**
 * ObjectOutputStream能够让你把对象写入到输出流中，而不需要每次写入一个字节。
 * 前提是被写入的对象必须实现java.io.Serializable接口
 *
 * @author pengpeng
 * @version 1.0
 * @date 2020/8/17 22:21
 */
public class ObjectOutputStreamExample {

    private static final String TEMP_DIR = System.getProperty("java.io.tmpdir");

    public static void main(String[] args) throws IOException {
        File file = FileUtils.getOrCreate(Paths.get(TEMP_DIR, "person.data")).toFile();
        try (ObjectOutputStream outputStream = new ObjectOutputStream(new FileOutputStream(file))) {
            Person person = new Person();
            person.setId(123456789L);
            person.setName("张三");
            person.setAge(34);
            person.setSex('男');
            outputStream.writeObject(person);
            outputStream.flush();
        }
    }

}
