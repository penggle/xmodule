package com.penglecode.xmodule.master4j.jvm.chapter8.methodinvoke;

/**
 * 字段没有多态的特性，是静态分配的，如果父类和子类拥有同名字段，那么子类会屏蔽父类的字段
 *
 * @author pengpeng
 * @version 1.0
 * @date 2020/7/21 17:48
 */
public class FieldHasNoPolymorphicExample {

    static class Father {
        public int money = 1;

        public Father() {
            money = 2;
            showMeTheMoney();
        }

        public void showMeTheMoney() {
            System.out.println("I am Father, i have $" + money);
        }
    }

    static class Son extends Father {
        public int money = 3;

        /**
         * 进入构造器Son()首先会调用父类的默认构造器Father()
         * 在Father()构造器中的money为2，接着进入到showMeTheMoney()方法，而方法具有动态分派特性，
         * 此时调用的showMeTheMoney()却是子类重写的showMeTheMoney()，在子类重写的showMeTheMoney()方法中
         * 此时由于子类的构造器未执行，即子类实际还未初始化，也即子类Son的<init>方法还被JVM执行，
         * 因此此时对子类来说，子类的money字段还未被初始化，因此还是在"准备阶段"的"零"值：0
         * 因此首先输出"I am Son,  i have $0"
         *
         * 接着执行子类构造器Son()中的money = 4语句，此时对子类实例来说money由3 -> 4
         * 接着再次执行子类的showMeTheMoney()方法，此时输出"I am Son,  i have $4"没啥好说的
         *
         * main()方法中最后一句通过静态类型访问到了父类中的money，所以输出了2。这句话也体现了"字段没有多态性，是静态访问的，即编译时就确定的"
         */
        public Son() {
            money = 4;
            showMeTheMoney();
        }

        public void showMeTheMoney() {
            System.out.println("I am Son,  i have $" + money);
        }
    }

    public static void main(String[] args) {
        Father gay = new Son();
        System.out.println("This gay has $" + gay.money);
    }

}
