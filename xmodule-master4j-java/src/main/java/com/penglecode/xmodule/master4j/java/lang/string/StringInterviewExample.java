package com.penglecode.xmodule.master4j.java.lang.string;

/**
 * String面试题 (基于JDK 8环境下)
 *
 * @author pengpeng
 * @version 1.0
 * @date 2020/7/30 10:46
 */
public class StringInterviewExample {

    /**
     * 创建字符串对象的方式之一：采用字面量的方式赋值
     *
     * 当代码中出现双引号形式（字面量）创建字符串对象时，JVM 会先对这个字符串进行检查，
     * 如果字符串常量池中存在相同内容的字符串对象的引用，则将这个引用返回；否则，创建新的字符串对象，
     * 然后将这个引用放入字符串常量池，并返回该引用。
     */
    public static void literalString() {
        String str1 = "abc"; //存在字符串字面量，编译期即可加入字符串常量池
        String str2 = "abc"; //字符串常量池中存在该字面量，直接返回其引用
        System.out.println(str1 == str2); //true
    }

    /**
     * 创建字符串对象的方式之二：采用new关键字新建一个字符串对象
     *
     * 调换str1与str2的什么顺序，也是同样的结果
     */
    public static void newString1() {
        //产生两个对象，池中的original，堆中str1
        String str1 = new String("abc");
        String str2 = "abc"; //池中已存在，直接返回引用
        System.out.println(str1 == str2); //false
        System.out.println(str1.intern() == str2); //true
    }

    /**
     * 创建字符串对象的方式之二：采用new关键字新建一个字符串对象
     *
     * 调换str1与str2的什么顺序，也是同样的结果
     */
    public static void newString2() {
        //产生两个对象，池中的original，堆中str1
        String str2 = "abc"; //池中已存在，直接返回引用
        String str1 = new String("abc");
        System.out.println(str1 == str2); //false
        System.out.println(str1.intern() == str2); //true
    }

    /**
     * 编译时优化
     */
    public static void compileOptimize() {
        String str1 = "a" + "b" + "c"; //声明时，产生编译优化(变量折叠)，等价于：String str1 = "abc";
        String str2 = "abc"; //发现池中已经存在"abc"字符串对象，则直接返回其引用给str2
        System.out.println(str1 == str2); //true
    }

    /**
     * 这种情况不存在编译期优化
     */
    public static void nocompileOptimize() {
        String str1 = "ab";
        //声明时，产生编译优化，直接在字符串池中创建"abc"字符串对象并将其引用指向str1
        String str2 = "abc";

        /**
         * 因为表达式中str1是变量，不会进行编译优化，
         * 其等价于new StringBuilder().append(str1).append("c").toString()，
         * 而toString()方法实际就是new String(...)
         */
        String str3 = str1 + "c";
        System.out.println(str2 == str3); //false
    }

    /**
     * JVM编译期遇到的字面量(即代码中所有出现的由双引号扩着的字符串)，
     * 一旦遇到这个，JVM将会在类初始化阶段就将其加入到字符串常量池中。
     * 这个是个自动加入的过程
     */
    public static void literalString1() {
        //发现字面量"abc"并自动加入字符串常量池中，且str1最终是new String(...)得出来的
        //此句等价于String str1 = new String("abc");
        String str1 = new StringBuilder("abc").toString();
        //str1.intern()返回的就是字符串池中的那个对象，str1本身是new出来的，所以两者不相同
        System.out.println(str1.intern() == str1); //false

        //区别上面：字符串常量池中存在"hello"和"world"，就是没有与str2字符序列相同的"helloworld"，
        String str2 = new StringBuilder("hello").append("world").toString();
        System.out.println(str2.intern() == str2); //true(JDK 7+),false(JDK 6-)
    }

    /**
     * String.intern()方法在不同版本JDK中的差异：
     *
     * - 在JDK 6中，intern()方法会把首次遇到的字符串实例复制到永久代的字符串常量池中存储，返回的也是永久代里面这个字符串实例的引用。
     * - 而JDK 7（以及部分其他虚拟机，例如JRockit）的intern()方法实现就不需要再拷贝字符串的实例到永久代了，既然字符串常量池已经移到Java堆中，
     *   那只需要在常量池里记录一下首次出现的实例引用即可。
     */
    public static void internDifferenceAtJdks() {
        //分析此句代码可知，str1是运行期产生的字符串，而其中的字面量"计算机"、"软件"却是编译期就可确定其在字符串常量池中
        String str1 = new StringBuilder("计算机").append("软件").toString();

        //JDK 6中是false，JDK 7+是true
        System.out.println(str1.intern() == str1);

        //编译期优化，等价于：String str1 = new String("java");
        String str2 = new StringBuilder("ja").append("va").toString();

        /**
         * 所有版本都输出false，因为"java"这个字符串是个特殊的字符串，在JVM启动时由sun.misc.Version这个类放入常量池的，已经不是首次出现了，
         * 所以肯定为false
         */
        System.out.println(str2.intern() == str2);
    }

    /**
     * 本例基于JDK 7+
     * 注意与complexExample2对比语句String intern3 = s3.intern()的位置
     */
    public static void complexExample1() {
        //等价于s3 = new StringBuilder().append("hello").append("hello");
        String s3 = new String("hello") + new String("hello");
        String intern3 = s3.intern(); //intern3是字符串常量池中的引用，也即是堆中s3的引用
        String s4 = "hellohello"; //等同再次调了s3.intern()
        System.out.println("s3 == s4 : " + (s3 == s4)); //true
        System.out.println("intern3 == s3 : " + (intern3 == s3)); //true
        System.out.println("intern3 == s4 : " + (intern3 == s4)); //true
    }

    /**
     * 本例基于JDK 7+
     * 注意与complexExample1对比语句String intern3 = s3.intern()的位置
     */
    public static void complexExample2() {
        //等价于s3 = new StringBuilder().append("hello").append("hello");
        String s3 = new String("hello") + new String("hello");
        //此时字符串常量池中只有hello
        String s4 = "hellohello"; //字符串常量池中没有hellohello，没有则创建，并将其引用返回给s4
        //与上面示例相比,注意s3.intern()在代码中行位置的变化
        String intern3 = s3.intern(); //返回的就是s4的引用
        System.out.println("s3 == s4 : " + (s3 == s4)); //false
        System.out.println("intern3 == s3 : " + (intern3 == s3)); //false
        System.out.println("intern3 == s4 : " + (intern3 == s4)); //true
    }

    public static void main(String[] args) {
        //literalString();
        //newString1();
        //newString2();
        //internDifferenceAtJdks();
        //literalString1();
        //complexExample1();
    }
}
