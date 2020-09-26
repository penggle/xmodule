package com.penglecode.xmodule.master4j.spring.core.annotation.aliasfor;

import org.springframework.core.annotation.AliasFor;
import org.springframework.core.annotation.AnnotatedElementUtils;
import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.lang.annotation.*;
import java.lang.reflect.Method;
import java.util.Arrays;

/**
 * 在Spring的众多注解中，经常会发现很多注解的不同属性起着相同的作用，
 * 比如@RequestMapping的value属性和path属性，这就需要做一些基本的限制，
 * 比如value和path的值不能冲突，比如任意设置value或者设置path属性的值，
 * 都能够通过另一个属性来获取值等等。为了统一处理这些情况，Spring创建了@AliasFor标签。
 *
 *
 * @author pengpeng
 * @version 1.0
 * @date 2020/9/23 15:45
 */
public class AliasForExample {

    /**
     * 由于value和path使用@AliasFor注解互为别名，因此两者必须使用相同的数据类、相同的default值，以及指定值也得相同
     * 否则会报错: AnnotationConfigurationException: Different @AliasFor mirror values for annotation ...
     */
    @RequestMapping(value="/abc", path="/123")
    public void againstAliasForAnnotation11() {

    }

    /**
     * 由于value和path使用@AliasFor注解互为别名，因此两者必须使用相同的数据类、相同的default值，以及指定值也得相同
     * 此时获取@RequestMapping注解上的path值也将是"/abc"
     */
    @RequestMapping(value="/abc")
    public void againstAliasForAnnotation12() {

    }

    public static void againstAliasForAnnotation11Test() throws Exception {
        Method method = AliasForExample.class.getMethod("againstAliasForAnnotation11");
        RequestMapping requestMapping = AnnotationUtils.findAnnotation(method, RequestMapping.class);
    }

    public static void againstAliasForAnnotation12Test() throws Exception {
        Method method = AliasForExample.class.getMethod("againstAliasForAnnotation12");
        RequestMapping requestMapping = AnnotationUtils.findAnnotation(method, RequestMapping.class);
        System.out.println(Arrays.toString(requestMapping.path())); //输出：[/abc]
    }

    @Retention(RetentionPolicy.RUNTIME)
    @Target(ElementType.METHOD)
    @Documented
    @RequestMapping(method = RequestMethod.GET)
    public static @interface GetMapping {

        /**
         * 与RequestMapping#value互为别名
         */
        @AliasFor(annotation = RequestMapping.class)
        String[] value() default {};

        /**
         * 与RequestMapping#path互为别名
         */
        @AliasFor(annotation = RequestMapping.class)
        String[] path() default {};

    }

    /**
     * 由于value和path使用@AliasFor注解互为别名，因此两者必须使用相同的数据类、相同的default值，以及指定值也得相同
     * 否则会报错: AnnotationConfigurationException: Different @AliasFor mirror values for annotation ...
     */
    @GetMapping(value="/list", path="/123")
    public void againstAliasForAnnotation21() {

    }

    /**
     * 由于value和path使用@AliasFor注解互为别名，因此两者必须使用相同的数据类、相同的default值，以及指定值也得相同
     * 此时获取@RequestMapping注解上的path值也将是"/abc"
     */
    @GetMapping(value="/abc")
    public void againstAliasForAnnotation22() {

    }

    public static void againstAliasForAnnotation21Test() throws Exception {
        Method method = AliasForExample.class.getMethod("againstAliasForAnnotation21");
        RequestMapping requestMapping = AnnotationUtils.findAnnotation(method, RequestMapping.class);
    }

    public static void againstAliasForAnnotation22Test() throws Exception {
        Method method = AliasForExample.class.getMethod("againstAliasForAnnotation22");
        GetMapping getMapping = AnnotationUtils.findAnnotation(method, GetMapping.class);
        System.out.println(Arrays.toString(getMapping.path())); //输出：[/abc]

        RequestMapping requestMapping = AnnotatedElementUtils.findMergedAnnotation(method, RequestMapping.class);
        //输出：@RequestMapping(consumes=[], headers=[], method=[GET], name=, params=[], path=[/abc], produces=[], value=[/abc])
        System.out.println(requestMapping);
    }

    public static void againstAliasForAnnotation23Test() throws Exception {
        Method method = AliasForExample.class.getMethod("againstAliasForAnnotation22");
        GetMapping getMapping = AnnotationUtils.findAnnotation(method, GetMapping.class);
        System.out.println(getMapping.path());
    }

    public static void main(String[] args) throws Exception {
        //againstAliasForAnnotation11Test();
        //againstAliasForAnnotation12Test();
        //againstAliasForAnnotation21Test();
        //againstAliasForAnnotation22Test();
        againstAliasForAnnotation23Test();
    }

}
