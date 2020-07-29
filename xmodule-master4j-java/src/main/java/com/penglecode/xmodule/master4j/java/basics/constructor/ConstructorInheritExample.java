package com.penglecode.xmodule.master4j.java.basics.constructor;

/**
 * 构造器在类继承中的特性
 *
 * - 如果父类存在默认构造函数，那么子类在未显示调用(super(...))父类构造器的情况下，会默认调用父类的默认无参构造器，如果子类显示调用了父类的构造器则不会再默认调用父类的默认无参构造器了。
 * - 如果父类不存在默认构造函数，那么子类必须显示调用(super(...))父类的构造器。
 *
 * @author pengpeng
 * @version 1.0
 * @date 2020/7/28 10:41
 */
public class ConstructorInheritExample {

    static class Parent {

        //父类的无参构造器就算是private的，子类照样也能默认调用或者显示调用super()
        private Parent() {
            System.out.println("Parent()");
        }

        public Parent(String name) {
            System.out.println("Parent(String name)");
        }

    }

    static class Child extends Parent {

        public Child() {
            System.out.println("Child()");
        }

        public Child(String name) {
            System.out.println("Child(String name)");
        }

        public Child(String name, char sex) {
            super(name); //此处显示调用父类的有参构造器，则不会再默认调用父类的默认无参构造器了
            System.out.println("Child(String name, char sex)");
        }

    }

    static class Person {

        public Person(String name, char sex) {
            System.out.println("Person(String name, char sex)");
        }

    }

    static class Gentleman extends Person {

        public Gentleman(String name) {
            super(name, '男');
            System.out.println("Gentleman(String name)");
        }

    }

    public static void main(String[] args) {
        new Child();
        System.out.println("--------------------------------------");
        new Child("Tom");
        System.out.println("--------------------------------------");
        new Child("Tony", '男');
        System.out.println("--------------------------------------");
        new Gentleman("Jack");
    }

}
