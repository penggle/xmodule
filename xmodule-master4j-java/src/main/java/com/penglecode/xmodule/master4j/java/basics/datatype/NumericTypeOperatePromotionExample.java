package com.penglecode.xmodule.master4j.java.basics.datatype;

/**
 * Java数值类型运算提升机制
 *
 * @author pengpeng
 * @version 1.0
 * @date 2020/7/28 15:13
 */
public class NumericTypeOperatePromotionExample {

    /**
     * 一元数值提升示例
     */
    public static void unaryPromotionTest1() {
        byte b = 2;
        int[] a = new int[b]; //数组创建表达式的变量提升
        char c = '\u0001';
        a[c] = 1; //数组索引表达式的变量提升
        a[0] = -c; //正负号运算符的变量提升
        System.out.println("a: " + a[0] + "," + a[1]);
        b = -1;
        int i = ~b; //按位补运算符(~)的变量提升
        System.out.println("~0x" + Integer.toHexString(b) + "==0x" + Integer.toHexString(i));
        i = b << 4L; // 移位运算符(>>, >>>, << )的每一个操作数(左操作数)的变量提升,(运算符右边不会发生提升)
        System.out.println("0x" + Integer.toHexString(b) + "<<4L==0x" + Integer.toHexString(i));
    }

    /**
     * 一元数值提升示例
     */
    public static void unaryPromotionTest2() {
        byte b = 1;
        //其实数值类型的++,--操作都会发生变量提升，只是里面隐含了一层强制类型转换：byte c = (byte)(b + 1)
        byte c = b++; //编译通过
        byte d = ++b; //编译通过
        //byte e = b + 1; //编译报错
        System.out.println(c + ", " + d);

        short s1 = 1;
        //short s2 = s1 + 1; //编译报错
        //+=是java语言规定的运算符，java编译器会对它进行特殊处理，该语句相当于 s1 = (short)(s1 + 1);
        s1 += 1; //编译通过
        System.out.println(s1);

        char c1 = 'A';
        int i1 = c1; //隐式转换
        int i2 = c1 + 1; //变量提升：int + int

        /**
         * 从JVM的角度来说，switch语句只认int类型，因此
         * 1、switch支持byte, short, char存在小转大的隐式转换为int
         * 2、switch支持enum, 本质上是由于enum的ordinal()方法支持
         * 3、switch支持String, 本质上是由于hashcode()方法支持，当然也要结合equals()方法
         */
        int flag = 0;
        switch (flag) {
            case 1:
                System.out.println("1");
                break;
            case 2:
                System.out.println("2");
                break;
            default :
                System.out.println("0");
                break;
        }

        Thread.State state = Thread.State.BLOCKED;
        switch (state) {
            case BLOCKED:
                System.out.println("BLOCKED");
                break;
            case NEW:
                System.out.println("NEW");
                break;
            default :
                System.out.println("RUNNABLE");
                break;
        }

    }

    /**
     * 二元数值提升示例
     */
    public static void binaryPromotionTest1() {
        Integer i = 2;
        float f = 1.0f;
        double d = 2.0;
        //Integer * float 被自动拆箱为 int * float
        //int * float 被提升为 float * float，然后
        //float == double 被提升为 double == double
        if (i * f == d) {
            System.out.println("Oops");
        }

        byte b = 0x1f;
        char c = 'G';
        int control = c & b;
        System.out.println(Integer.toHexString(control));

        //此处 Integer : float 先是被自动拆箱为 int : float ，然后被提升为 float : float
        f = (b==0) ? i : 4.0f;
        System.out.println(1.0/f);
    }

    /**
     * 三元数值提升示例
     */
    public static void ternaryPromotionTest1() {
        //编译器无法确定null的静态类型，所以得让第三个参数1进行自动装箱
        Object k = true ? null : 1;
        System.out.println(k); //输出 null
    }

    /**
     * 三元数值提升示例
     */
    public static void ternaryPromotionTest2() {
        Integer a = null;
        //a的静态类型是Integer，根据规则a需要自动拆箱为int，于是报了NPE
        //此句等效于：Object k = true ? (Integer)null : 1;
        Object k = true ? a : 1; //此处报NPE
        System.out.println(k);
    }

    public static void main(String[] args) {
        //unaryPromotionTest1();
        unaryPromotionTest2();
        //binaryPromotionTest1();
        //ternaryPromotionTest1();
        //ternaryPromotionTest2();
    }

}
