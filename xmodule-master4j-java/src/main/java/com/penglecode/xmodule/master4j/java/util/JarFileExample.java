package com.penglecode.xmodule.master4j.java.util;

import com.sun.xml.internal.ws.policy.privateutil.PolicyUtils;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.JarURLConnection;
import java.net.URL;
import java.util.Arrays;
import java.util.Enumeration;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;
import java.util.zip.ZipEntry;

/**
 * JarFile示例
 *
 * @author pengpeng
 * @version 1.0
 * @date 2020/8/28 22:31
 */
public class JarFileExample {

    public static void traverseJarEntrys() throws IOException {
        String jarFilePath = "C:\\Users\\Pengle\\.m2\\repository\\org\\springframework\\spring-core\\5.2.7.RELEASE\\spring-core-5.2.7.RELEASE.jar";
        JarFile jarFile = new JarFile(jarFilePath, true);
        Enumeration<JarEntry> jarEntries = jarFile.entries();
        while(jarEntries.hasMoreElements()) {
            JarEntry jarEntry = jarEntries.nextElement();
            System.out.println(jarEntry.getName() + " - " + jarEntry.getSize());
        }
    }

    public static void getJarEntryByJarFileApi() throws IOException {
        String jarFilePath = "C:/Users/Pengle/.m2/repository/org/springframework/spring-core/5.2.7.RELEASE/spring-core-5.2.7.RELEASE.jar";
        String targetClass = "org/springframework/util/Assert.class";
        JarFile jarFile = new JarFile(jarFilePath, true);
        Enumeration<JarEntry> jarEntries = jarFile.entries();
        JarEntry jarEntry = null;
        while(jarEntries.hasMoreElements()) {
            jarEntry = jarEntries.nextElement();
            if(targetClass.equals(jarEntry.getName())) {
                System.out.println(jarEntry.getName() + " - " + jarEntry.getSize());
                break;
            }
        }
        InputStream inputStream = jarFile.getInputStream(jarEntry);
        byte[] datas = new byte[inputStream.available()];
        inputStream.read(datas);
        inputStream.close();
        System.out.println(Arrays.toString(datas));
    }

    public static void getJarEntryByJarFileURL() throws IOException {
        String jarFilePath = "C:/Users/Pengle/.m2/repository/org/springframework/spring-core/5.2.7.RELEASE/spring-core-5.2.7.RELEASE.jar";
        String targetClass = "org/springframework/util/Assert.class";
        URL jarEntryUrl = new URL("jar:file:/" + jarFilePath + "!/" + targetClass);
        System.out.println(jarEntryUrl);
        InputStream inputStream = jarEntryUrl.openStream();
        byte[] datas = new byte[inputStream.available()];
        inputStream.read(datas);
        inputStream.close();
        System.out.println(Arrays.toString(datas));
    }

    public static void getJarEntryByJarHttpURL() throws IOException {
        String jarHttpUrl = "https://repo1.maven.org/maven2/org/springframework/spring-core/5.2.2.RELEASE/spring-core-5.2.2.RELEASE.jar";
        String targetClass = "org/springframework/util/Assert.class";
        URL jarEntryUrl = new URL("jar:" + jarHttpUrl + "!/" + targetClass);
        System.out.println(jarEntryUrl.getProtocol());
        InputStream inputStream = jarEntryUrl.openStream();
        byte[] datas = new byte[inputStream.available()];
        inputStream.read(datas);
        inputStream.close();
        System.out.println(Arrays.toString(datas));
    }

    public static void testRtJarFile() throws IOException {
        String jarFilePath = "C:\\Program Files\\Java\\jdk1.8.0_241\\jre\\lib\\rt.jar";
        String targetClass = "java/lang/Object.class";
        JarFile jarFile = new JarFile(jarFilePath, true);
        JarEntry jarEntry = jarFile.getJarEntry(targetClass);
        System.out.println(jarEntry);
    }

    public static void main(String[] args) throws Exception {
        //traverseJarEntrys();
        //getJarEntryByJarFileApi();
        //getJarEntryByJarFileURL();
        //getJarEntryByJarHttpURL();
        testRtJarFile();
    }

}
