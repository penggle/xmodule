package com.penglecode.xmodule.master4j.java.io.object;

import com.penglecode.xmodule.common.util.FileUtils;

import java.io.*;
import java.nio.file.Paths;

/**
 * ObjectInputStream能够让你从输入流中读取Java对象，而不需要每次读取一个字节。
 * 你可以把InputStream包装到ObjectInputStream中，然后就可以从中读取对象了。
 * 前提是被写入的对象必须实现java.io.Serializable接口
 *
 * @author pengpeng
 * @version 1.0
 * @date 2020/8/17 22:21
 */
public class ObjectInputStreamExample {

    private static final String TEMP_DIR = System.getProperty("java.io.tmpdir");

    public static void main(String[] args) throws IOException {
        File file = FileUtils.getOrCreate(Paths.get(TEMP_DIR, "person.data")).toFile();
        try (ObjectInputStream inputStream = new ObjectInputStream(new FileInputStream(file))) {
            Person person = (Person) inputStream.readObject();
            System.out.println(person);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

}
