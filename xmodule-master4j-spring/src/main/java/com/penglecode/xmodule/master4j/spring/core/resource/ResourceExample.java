package com.penglecode.xmodule.master4j.spring.core.resource;

import com.penglecode.xmodule.common.util.PropertiesUtils;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.Properties;

/**
 * Resource - 统一资源示例
 *
 * @author pengpeng
 * @version 1.0
 * @date 2020/9/17 14:55
 */
public class ResourceExample {

    public static void classPathResourceTest() throws IOException {
        //classpath的相对路径模式
        Resource resource1 = new ClassPathResource("foo.properties", ResourceExample.class);
        Properties properties1 = PropertiesUtils.loadProperties(resource1);
        //classpath的绝对路径模式
        Resource resource2 = new ClassPathResource("com/penglecode/xmodule/master4j/spring/core/resource/foo.properties");
        Properties properties2 = PropertiesUtils.loadProperties(resource2);

        System.out.println(properties1.equals(properties2));
    }

    public static void fileSystemResourceTest() throws IOException {
        Properties properties1 = System.getProperties();
        File file = new File("d:/system.properties");
        PropertiesUtils.storeProperties(properties1, file);
        FileSystemResource resource = new FileSystemResource(file); //注意path中不需要卸载协议头：file:
        Properties properties2 = PropertiesUtils.loadProperties(resource);
        properties2.forEach((key, value) -> {
            System.out.println(key + " = " + value);
        });
    }

    public static void urlResourceTest() throws IOException {
        String url = "https://mp.weixin.qq.com/s/AGkAF3GEPKKTIGaqLijovg";
        UrlResource resource = new UrlResource(url);
        Files.copy(resource.getInputStream(), Paths.get("d:/test.html"), StandardCopyOption.REPLACE_EXISTING);
    }
    
    public static void main(String[] args) throws IOException {
        //classPathResourceTest();
        //fileSystemResourceTest();
        urlResourceTest();
    }

}
