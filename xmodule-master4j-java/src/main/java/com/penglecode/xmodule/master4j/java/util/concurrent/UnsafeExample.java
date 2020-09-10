package com.penglecode.xmodule.master4j.java.util.concurrent;

import sun.misc.Unsafe;

import java.lang.reflect.Field;

/**
 * sun.misc.Unsafe使用示例
 *
 * https://blog.csdn.net/MeituanTech/article/details/98566848
 *
 * Unsafe做操作的是直接内存区，所以该类没有办法通过HotSpot的GC进行回收，需要进行手动回收，
 * 因此在使用此类时需要注意内存泄漏（Memory Leak）和内存溢出（Out Of Memory）。
 *
 * @author pengpeng
 * @version 1.0
 * @date 2020/8/30 16:21
 */
public class UnsafeExample {

    private static int id = 0;

    private char sex;

    static class Player {

        public static final int DEFAULT_AGE = 12;

        static {
            System.out.println("类初始化：DEFAULT_AGE = " + DEFAULT_AGE);
        }

        private int age = DEFAULT_AGE;

        {
            System.out.println("实例初始化：age = " + age);
        }

        private Player() {
            this.age = 50;
        }

        public int getAge() {
            return this.age;
        }

        public void setAge(int age) {
            this.age = age;
        }
    }

    class Movie {

        private final String name;

        private Movie(String name) {
            this.name = name;
        }

        public String getName() {
            return name;
        }
    }

    protected static Unsafe getUnsafeInstance() throws Exception {
        Field f = Unsafe.class.getDeclaredField("theUnsafe");
        f.setAccessible(true);
        return (Unsafe) f.get(null);
    }

    /**
     * Unsafe类提供了一个静态方法getUnsafe()来获取Unsafe的实例，但是如果你尝试调用Unsafe.getUnsafe()，
     * 会得到一个SecutiryException。这个类只有被JDK信任的类实例化。
     * 但是这总会是有变通的解决办法的，一个简单的方式就是使用反射进行实例化：
     */
    public static void getUnsafeInstanceTest() throws Exception {
        Unsafe theUnsafe = null;
        try {
            theUnsafe = Unsafe.getUnsafe();
        } catch (Exception e) {
            e.printStackTrace();
        }
        System.out.println(theUnsafe);

        theUnsafe = getUnsafeInstance();
        System.out.println(theUnsafe);
    }

    /**
     * 同过Unsafe的API设置基本数据类型变量的值：
     *
     * 其中targetOffset是表示的是i在内存中的偏移量。何谓偏移量？
     *
     * JVM的实现可以自由选择如何实现Java对象的“布局”，也就是在内存里Java对象的各个部分放在哪里，包括对象的实例字段和一些元数据之类。
     * sun.misc.Unsafe里关于对象字段访问的方法把对象布局抽象出来，它提供了objectFieldOffset()方法用于获取某个字段相对Java对象的“起始地址”的偏移量，
     * 也提供了getInt、getLong、getObject之类的方法可以使用前面获取的偏移量来访问某个Java对象的某个字段。
     */
    public static void modifyPrimitiveTypeValueTest() throws Exception {
        Unsafe theUnsafe = getUnsafeInstance();
        Field targetField;
        long targetOffset; //表示的是字段在内存中的偏移量

        System.out.println("-------------------------使用Unsafe修改实例字段的值---------------------------");
        UnsafeExample example = new UnsafeExample();
        System.out.println("Before modify, sex = " + example.sex);
        targetField = UnsafeExample.class.getDeclaredField("sex");
        targetOffset = theUnsafe.objectFieldOffset(targetField); //获取实例字段sex在内存中偏移量
        System.out.println("The memory offset of 'private char sex' : " + targetOffset);
        theUnsafe.putChar(example, targetOffset, '男'); //因为是实例变量，传入的第一个Object参数应为example对象
        System.out.println("After modify, sex = " + example.sex);

        System.out.println("-------------------------使用Unsafe修改静态字段的值---------------------------");
        System.out.println("Before modify, id = " + id);
        targetField = UnsafeExample.class.getDeclaredField("id");
        targetOffset = theUnsafe.staticFieldOffset(targetField); //获取静态字段id在内存中偏移量
        System.out.println("The memory offset of 'private static int id' : " + targetOffset);
        theUnsafe.putInt(UnsafeExample.class, targetOffset, 123); //因为是静态变量，传入的第一个Object参数应为class对象
        System.out.println("After modify, id = " + id);
    }

    /**
     * 突破限制创建实例
     *
     * 通过allocateInstance()方法，你可以创建一个类的实例，但是却不需要调用它的构造函数、初使化代码、
     * 各种JVM安全检查以及其它的一些底层的东西。即使构造函数是私有，我们也可以通过这个方法创建它的实例。
     *
     * （这个对单例模式情有独钟的程序员来说将会是一个噩梦，它们没有办法阻止这种方式调用）
     */
    public static void allocateInstanceTest() throws Exception {
        Unsafe theUnsafe = getUnsafeInstance();
        Player p = (Player) theUnsafe.allocateInstance(Player.class);
        System.out.println(p.getAge()); //输出0，说明的确连初始化都没做

        p.setAge(45);
        System.out.println(p.getAge());

        //实例化一个没有无参构造器的Movie实例
        Movie m = (Movie) theUnsafe.allocateInstance(Movie.class);
        System.out.println(m.getName());
    }

    public static void main(String[] args) throws Exception {
        //getUnsafeInstanceTest();
        //modifyPrimitiveTypeValueTest();
        allocateInstanceTest();
    }

}
