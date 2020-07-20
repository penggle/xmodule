package com.penglecode.xmodule.master4j.jvm.chapter7.classloader;

import java.io.IOException;
import java.io.InputStream;

/**
 *
 * 具有相同标识符的class通过不同的ClassLoader加载出来，其相等性却为false的示例
 *
 * 这句话可以表达得更通俗一些：比 较两个类是否“相等”，只有在这两个类是由同一个类加载器加载的前提 下才有意义，
 * 否则，即使这两个类来源于同一个Class文件，被同一个 Java虚拟机加载，只要加载它们的类加载器不同，那这两个类就必定不相等。
 *
 * 这里的相等指的是指：包括代表类的Class对象的equals()方法、 isAssignableFrom()方法、isInstance()方法的返回结果，
 * 也包括了使用 instanceof关键字做对象所属关系判定等各种情况
 *
 * @author pengpeng
 * @version 1.0
 * @date 2020/7/20 11:16
 */
public class UniformClassNotEqualsExample {

    public static class SimpleClassLoader extends ClassLoader {

        @Override
        public Class<?> loadClass(String name) throws ClassNotFoundException {
            try {
                InputStream is = getClass().getResourceAsStream(name.replace(".", "/") + ".class");
                if (is != null) {
                    byte[] b = new byte[is.available()];
                    is.read(b);
                    return defineClass(name, b, 0, b.length);
                }
                return super.loadClass(name);
            } catch (IOException e) {
                throw new ClassNotFoundException(name);
            }
        }
    }

    public static void main(String[] args) throws ClassNotFoundException {
        Class<UniformClassNotEqualsExample> classFromDefault = UniformClassNotEqualsExample.class;
        String className = classFromDefault.getName();
        SimpleClassLoader customClassLoader = new SimpleClassLoader();
        Object classFromCustom = customClassLoader.loadClass(className);
        System.out.println(classFromCustom);
        System.out.println(classFromCustom instanceof UniformClassNotEqualsExample);
    }

}
