package com.penglecode.xmodule.master4j.java.util.collection;

import com.penglecode.xmodule.common.util.ReflectionUtils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

/**
 * ArrayList示例
 *
 * @author pengpeng
 * @version 1.0
 * @date 2020/8/1 20:46
 */
public class ArrayListExample {

    /**
     * ArrayList默认容量为10
     * 默认构造函数new出来的ArrayList的初始容量为0，在第一次add元素的时候出发扩容
     * ArrayList每次扩容至少为原容量的0.5倍
     * ArrayList不像HashMap那样会提前扩容，仅在size == elementData.length + 1时进行扩容
     */
    public static void expandCapacity() {
        ArrayList<String> list = new ArrayList<String>();
        trace(list);

        for(int i = 1; i <= 50; i++) {
            list.add(String.valueOf(i));
            trace(list);
        }
    }

    /**
     * trimToSize：将此ArrayList实例的容量调整为列表的当前大小。
     * 即将elementData进行缩容，使得elementData.length == size
     */
    public static void trimToSize() {
        ArrayList<String> list = new ArrayList<String>(15);
        for(int i = 1; i <= 10; i++) {
            list.add(String.valueOf(i));
        }
        trace(list);
        list.trimToSize();
        trace(list);
    }

    /**
     * ArrayList重写了clone方法，不过是浅克隆
     */
    public static void cloneList() {
        ArrayList<String> list1 = new ArrayList<String>(Arrays.asList("1", "2", "3", "4", "5"));
        ArrayList<String> list2 = (ArrayList<String>) list1.clone();
        trace(list1);
        trace(list2);
        System.out.println(list1 == list2);
        System.out.println(list1.equals(list2));
        for(int i = 0, len = list1.size(); i < len; i++){
            System.out.println(String.format("list1[%s] == list2[%s] ? %s", i, i, list1.get(i) == list2.get(i)));
        }
    }

    public static void toArray() {
        List<Integer> list = new ArrayList<>(Arrays.asList(1,2,3,4,5));

        Object[] a1 = list.toArray();
        System.out.println(a1.getClass() + " : " + Arrays.toString(a1) + ", elementType = " + a1[1].getClass());

        Integer[] a2 = list.toArray(new Integer[0]);
        System.out.println(a2.getClass() + " : " + Arrays.toString(a2) + ", elementType = " + a2[1].getClass());

        Number[] a3 = list.toArray(new Number[0]);
        System.out.println(a3.getClass() + " : " + Arrays.toString(a3) + ", elementType = " + a3[1].getClass());

        Long[] a4 = list.toArray(new Long[0]); //java.lang.ArrayStoreException
        System.out.println(a4.getClass() + " : " + Arrays.toString(a4) + ", elementType = " + a4[1].getClass());
    }

    public static void testIndexOutOfBoundsException1() {
        List<Integer> list = new ArrayList<>();
        for(int i = 0; i < 10; i++){
            list.add(i);
            if(i % 2 == 0){
                list.remove(i);
            }
            System.out.println(list);
        }
    }

    /**
     * 通过常规的数组迭代方式遍历List：for(int i = 0; i < len; i++) {...}
     * 则只会检测数组下标是否越界
     * 也即只会报java.lang.IndexOutOfBoundsException
     */
    public static void testIndexOutOfBoundsException2() {
        List<Integer> original = Arrays.asList(1, 2, 3, 4, 5, 6, 7, 8, 9);
        //以下两种方式均会触发java.lang.IndexOutOfBoundsException
        try {
            //删除偶数位上的元素(实现方式1)
            List<Integer> list1 = new ArrayList<>(original);
            for (int i = 0, len = list1.size(); i < len; i++) {
                if (i % 2 == 0) {
                    //调用ArrayList.remove(int index)
                    list1.remove(i); //java.lang.IndexOutOfBoundsException
                }
                System.out.println(list1);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        System.out.println("------------------------------------------------");
        try {
            //删除偶数位上的元素(实现方式2)
            List<Integer> list2 = new ArrayList<>(original);
            for(int i = 0, len = list2.size(); i < len; i++){
                Integer element = list2.get(i); //java.lang.IndexOutOfBoundsException
                if(i % 2 == 0){
                    //调用ArrayList.remove(Object o)
                    list2.remove(element);
                }
                System.out.println(list2);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * JDK 5新增的 foreach循环其实是个语法糖，通过反编译得知其相当于
     * for(Iterator it = list.iterator(); it.hasNext();) { Integer i = (Integer)it.next(); ... }
     *
     * 只要是通过Iterator接口迭代集合，则只会进行checkForComodification()检测
     * 产生java.util.ConcurrentModificationException的原因是语句Iterator it = list.iterator();生成了一个迭代器对象，
     * 这个迭代器见java.util.ArrayList$Itr，在new出迭代器的那一刻，迭代器中的expectedModCount固化为ArrayList.modCount，
     * 只有调用Iterator.remove()方法时才会更新ArrayList$Itr.expectedModCount的值与ArrayList.modCount保持同步
     * 而当调用list.remove()时是不会进行上述同步操作的，于此同时在每次调用迭代器ArrayList$Itr.next()方法时都会检测他们两者是否一样,
     * 这样就导致了java.util.ConcurrentModificationException
     *
     * 解决方法就是只能调用Iterator.remove()来删除元素
     */
    public static void testConcurrentModificationException() {
        List<Integer> original = Arrays.asList(1, 2, 3, 4, 5, 6, 7, 8, 9);

        //错误的在迭代过程中删除元素的方式
        try {
            List<Integer> list1 = new ArrayList<>(original);
            for (Integer i : list1) {
                if (i % 2 == 0) {
                    list1.remove(i);
                }
                System.out.println(list1);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        System.out.println("-----------------------------------------");
        //正确的在迭代过程中删除元素的方式
        try {
            List<Integer> list2 = new ArrayList<>(original);
            for (Iterator<Integer> it  = list2.iterator(); it.hasNext();) {
                Integer i = it.next();
                if (i % 2 == 0) {
                    it.remove();
                }
                System.out.println(list2);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static void trace(List<?> list) {
        Object[] elementData = getElementData(list);
        int size = list.size();
        int capacity = elementData.length;
        System.out.println(String.format("{ size = %s, capacity = %s, elementData = %s}", size, capacity, Arrays.toString(elementData)));
    }

    private static Object[] getElementData(List<?> list) {
        return ReflectionUtils.getFieldValue(list, "elementData");
    }

    public static void main(String[] args) {
        //expandCapacity();
        //trimToSize();
        //cloneList();
        //toArray();
        //testIndexOutOfBoundsException1();
        //testIndexOutOfBoundsException2();
        testConcurrentModificationException();
    }
}
