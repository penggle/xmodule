package com.penglecode.xmodule.master4j.java.util.collection;

import java.util.HashSet;
import java.util.Objects;
import java.util.Set;
import java.util.TreeSet;

/**
 * java.util.Set不可重复性示例
 *
 * - HashSet是无序的，底层是基于HashMap实现的，其不可重复特性是通过hashcode()方法和equals()方法来保证的
 * - TreeSet是有序的，底层是基于TreeMap实现的，TreeMap是按key排序的，元素在插入TreeSet时compareTo()方法要被调用，
 *   所以TreeSet中的元素要实现Comparable接口，其不可重复特性是通过Comparable.compareTo()方法来实现的
 *
 * @author pengpeng
 * @version 1.0
 * @date 2020/8/3 10:21
 */
public class SetUnrepeatableExample {

    static class Student1 {

        private Long id;

        private String name;

        public Student1(Long id, String name) {
            this.id = id;
            this.name = name;
        }

        public Long getId() {
            return id;
        }

        public void setId(Long id) {
            this.id = id;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        @Override
        public String toString() {
            return "Student1{" +
                    "id=" + id +
                    ", name='" + name + '\'' +
                    '}';
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (!(o instanceof Student1)) return false;
            Student1 student = (Student1) o;
            return id.equals(student.id);
        }

        @Override
        public int hashCode() {
            return Objects.hash(id);
        }

    }

    static class Student2 implements Comparable<Student2> {

        private Long id;

        private String name;

        public Student2(Long id, String name) {
            this.id = id;
            this.name = name;
        }

        public Long getId() {
            return id;
        }

        public void setId(Long id) {
            this.id = id;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        @Override
        public String toString() {
            return "Student2{" +
                    "id=" + id +
                    ", name='" + name + '\'' +
                    '}';
        }

        @Override
        public int compareTo(Student2 o) {
            if (o == null) {
                return 1;
            }
            if (this != o) {
                if (id == null) {
                    return -1;
                } else if (o.id == null) {
                    return 1;
                } else {
                    return id.compareTo(o.id);
                }
            }
            return 0;
        }
    }

    /**
     * 测试HashSet的不可重复性
     */
    public static void testHashSetUnrepeatable() {
        Set<Student1> set = new HashSet<>();
        set.add(new Student1(1L, "张三"));
        set.add(new Student1(2L, "李四"));
        set.add(new Student1(3L, "王五"));
        set.add(new Student1(1L, "赵六"));
        //[Student1{id=1, name='张三'}, Student1{id=2, name='李四'}, Student1{id=3, name='王五'}]
        System.out.println(set);
    }
    
    public static void testTreeSetUnrepeatable() {
        Set<Student2> set = new TreeSet<>();
        set.add(new Student2(1L, "张三"));
        set.add(new Student2(2L, "李四"));
        set.add(new Student2(3L, "王五"));
        set.add(new Student2(1L, "赵六"));
        //[Student2{id=1, name='张三'}, Student1{id=2, name='李四'}, Student1{id=3, name='王五'}]
        System.out.println(set);
    }

    public static void main(String[] args) {
        testHashSetUnrepeatable();
        testTreeSetUnrepeatable();
    }
}
