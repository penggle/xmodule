package com.penglecode.xmodule.master4j.spring.core.classscanning;

import org.springframework.core.type.ClassMetadata;
import org.springframework.core.type.classreading.MetadataReader;
import org.springframework.core.type.classreading.MetadataReaderFactory;
import org.springframework.core.type.filter.TypeFilter;

import java.io.IOException;
import java.lang.reflect.Method;
import java.util.Arrays;

/**
 * ClassMetadata对象查看
 *
 * ClassMetadata的实现是：org.springframework.core.type.classreading.SimpleAnnotationMetadata
 *
 * classMetadata.isIndependent() : 这个类是否是独立的，即这个类的实例化(new)是否依赖其他实例？例如一个非静态内部类的实例化就不是独立的。
 * classMetadata.hasEnclosingClass() : 这个类是否是个内部类
 * classMetadata.getEnclosingClassName() : 返回这个内部类声明所在类的名称
 * classMetadata.getMemberClassNames() : 返回这个类中声明的各个内部类组成的列表
 *
 */
public class ClassMetadataInspectTypeFilter implements TypeFilter {

        @Override
        public boolean match(MetadataReader metadataReader, MetadataReaderFactory metadataReaderFactory) throws IOException {
            ClassMetadata classMetadata = metadataReader.getClassMetadata();
            //System.out.println(classMetadata.getClass()); //org.springframework.core.type.classreading.SimpleAnnotationMetadata
            System.out.println("<===============================" + classMetadata.getClassName() + "===============================>");
            Method[] methods = ClassMetadata.class.getDeclaredMethods();
            for(Method method : methods) {
                try {
                    method.setAccessible(true);
                    Class<?> returnType = method.getReturnType();
                    Object returnValue = method.invoke(classMetadata);
                    String rawReturnValue = returnValue == null ? null : (returnType.isArray() ? Arrays.toString((Object[])returnValue) : returnValue.toString());
                    System.out.println(String.format(">>> %s = %s", method.getName(), rawReturnValue));
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            return true;
        }

    }