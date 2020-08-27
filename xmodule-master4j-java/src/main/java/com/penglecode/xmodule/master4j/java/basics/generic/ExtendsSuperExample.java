package com.penglecode.xmodule.master4j.java.basics.generic;

/**
 * https://www.cnblogs.com/JasonLGJnote/p/11159869.html
 *
 * <? extends T>：是指 “上界通配符（Upper Bounds Wildcards）”
 *
 * 为什么要用通配符和边界？
 * 使用泛型的过程中，经常出现一种很别扭的情况。比如按照题主的例子，我们有Fruit类，和它的派生类Apple类。
 *
 * 然后有一个最简单的容器：Plate类。盘子里可以放一个泛型的“东西”。我们可以对这个东西做最简单的“放”和“取”的动作：set( )和get( )方法。
 *
 * PECS原则
 * 最后看一下什么是PECS（Producer Extends Consumer Super）原则，已经很好理解了：
 *
 * 频繁往外读取内容的，适合用上界Extends。
 * 经常往里插入的，适合用下界Super。
 *
 * @author pengpeng
 * @version 1.0
 * @date 2020/8/25 16:43
 */
public class ExtendsSuperExample {

    static class Fruit {}
    static class Apple extends Fruit {}
    static class Orange extends Fruit {}

    /**
     * 水果盘子容器
     * @param <T>
     */
    static class Plate<T>{
        private T item;
        public Plate(T t){item=t;}
        public void set(T t){item=t;}
        public T get(){return item;}
    }

    public static void main(String[] args) {
        /**
         * 现在我定义一个“水果盘子”，逻辑上水果盘子当然可以装苹果：
         * 但实际上Java编译器不允许这个操作。会报错，“装苹果的盘子”无法转换成“装水果的盘子”。
         * error: incompatible types: Plate<Apple> cannot be converted to Plate<Fruit>
         */
        //Plate<Fruit> fruitPlate = new Plate<Apple>(new Apple());

        /**
         * Plate<？ extends Fruit>即是“上界通配符（Upper Bounds Wildcards）”：
         * 翻译一下就是：一个能放水果以及一切是水果派生类的盘子。再直白点就是：啥水果都能放的盘子。
         * 这和我们人类的逻辑就比较接近了。Plate<？ extends Fruit>和Plate<Apple>最大的区别就是：
         * Plate<？ extends Fruit>是Plate<Fruit>以及Plate<Apple>的基类。
         * 直接的好处就是，我们可以用“苹果盘子”给“水果盘子”赋值了。
         */
        Plate<? extends Fruit> fruitPlate = new Plate<Apple>(new Apple());

        /**
         * 合法，因为盘子里的肯定是水果
         */
        Fruit apple1 = fruitPlate.get();
        /**
         * 不合法，因为盘子里的水果不一定是苹果，
         * 但是实际上这句就目前上下文语境来说是没有问题，盘子里实际装的是苹果
         * 需要加上强制转换就不会编译错误，之所以会这样是由于JVM(确切地说是编译器)的静态分派机制引起的
         */
        //Apple apple2 = fruitPlate.get();

        //上界<? extends T>不能往里存，只能往外取
        //不能存入任何元素
        //fruitPlate.set(new Apple()); //报错
        //fruitPlate.set(new Fruit()); //报错
        //fruitPlate.set(new Object()); //报错

        /**
         * 因为下界规定了元素的最小粒度的下限，实际上是放松了容器元素的类型控制。
         * 既然元素是Fruit的基类，那往里存粒度比Fruit小的都可以。但往外读取元素就费劲了，
         * 只有所有类的基类Object对象才能装下。但这样的话，元素的类型信息就全部丢失。
         */
        Plate<? super Fruit> pfruit = new Plate<Fruit>(new Fruit());
        //存入元素正常
        pfruit.set(new Fruit());
        pfruit.set(new Apple());
        //pfruit.set(new Object()); //报错

        //读取出来的东西只能存放在Object类里。
        //Apple newFruit3 = pfruit.get(); //报错
        //Fruit newFruit1 = pfruit.get(); //报错
        Object newFruit2 = pfruit.get();
    }

}
