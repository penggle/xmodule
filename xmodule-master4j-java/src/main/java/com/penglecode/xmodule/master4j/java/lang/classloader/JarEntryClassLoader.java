package com.penglecode.xmodule.master4j.java.lang.classloader;

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;

/**
 * 基于双亲委派模型的针对jar包中单个class文件加载的ClassLoader
 *
 * 自定义类加载器步骤
 * （1）继承ClassLoader
 * （2）重写findClass()方法
 * （3）调用defineClass()方法
 *
 * @author pengpeng
 * @version 1.0
 * @date 2020/8/28 23:12
 */
public class JarEntryClassLoader extends ClassLoader {

    private final URL jarFileUrl;

    public JarEntryClassLoader(String jarFileUrl) {
        this.jarFileUrl = prepareJarFileUrl(jarFileUrl);
    }

    protected URL prepareJarFileUrl(String jarFileUrl) {
        if(jarFileUrl.endsWith(".jar")) {
            try {
                URL tempJarFileUrl = new URL(jarFileUrl);
                String urlProtocol = tempJarFileUrl.getProtocol().toLowerCase();
                if(!"jar".equals(urlProtocol)) {
                    if("file".equals(urlProtocol) || "http".equals(urlProtocol) || "https".equals(urlProtocol)) {
                        return new URL("jar:" + tempJarFileUrl.toString() + "!/");
                    }
                } else {
                    return tempJarFileUrl;
                }
            } catch (MalformedURLException e) {
            }
        }
        throw new IllegalArgumentException("不合法的jar包URL(仅支持file:/path/abc.jar、http://my.host/abc.jar、https://my.host/abc.jar)");
    }

    protected URL getJarEntryUrl(String name) {
        String url = jarFileUrl + name.replace(".","/") + ".class";
        try {
            return new URL(url);
        } catch (MalformedURLException e) {
            throw new IllegalArgumentException("不合法的jar包URL: " + url);
        }
    }

    @Override
    protected Class<?> findClass(String name) throws ClassNotFoundException {
        System.out.println(String.format(">>> 父类加载器都没有加载出: %s, 动用本类[%s]做最后加载尝试!", name, getClass().getSimpleName()));
        try(InputStream inputStream = getJarEntryUrl(name).openStream()) {
            byte[] classBytes = new byte[inputStream.available()];
            inputStream.read(classBytes);
            return defineClass(name, classBytes, 0, classBytes.length);
        } catch (IOException e) {
            e.printStackTrace();
            throw new ClassNotFoundException(name);
        }
    }

}
