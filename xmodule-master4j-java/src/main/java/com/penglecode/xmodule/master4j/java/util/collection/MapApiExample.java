package com.penglecode.xmodule.master4j.java.util.collection;

import java.util.*;

/**
 * Map API示例
 *
 * @author pengpeng
 * @version 1.0
 * @date 2020/8/11 21:47
 */
public class MapApiExample {


    public static void forEach() {
        Map<String, String> map = new HashMap<>(System.getenv());
        map.forEach((k, v) -> System.out.println(k + " = " + v));
    }

    /**
     * keySet()返回的Set类型的key视图，对该视图的修改（确切的说是remove，很明显是不支持add操作的）都会影响到原来Map，
     * 同理对原来Map的修改（put、remove）也会影响到keySet()视图
     * 对keySet()视图的迭代删除中请使用Iterator.remove()
     */
    public static void keySet() {
        Map<String, String> map = new HashMap<>();
        map.put("username", "张三");
        map.put("password", "123456");
        map.put("mobilePhone", "13812345678");
        map.put("sex", "男");
        Set<String> keys = map.keySet();
        System.out.println(keys.getClass());
        System.out.println(keys);
        System.out.println("---------------------------------------------");
        keys.remove("mobilePhone");
        System.out.println(keys);
        System.out.println(map);
        System.out.println("---------------------------------------------");
        map.put("email", "admin@qq.com");
        System.out.println(keys);
        System.out.println(map);
        System.out.println("---------------------------------------------");
        keys.clear();
        System.out.println(keys);
        System.out.println(map);
    }


    /**
     * value()返回的Collection类型的value视图，对该视图的修改（确切的说是remove，很明显是不支持add操作的）都会影响到原来Map，
     * 同理对原来Map的修改（put、remove）也会影响到values()视图
     * 对value()视图的迭代删除中请使用Iterator.remove()
     */
    public static void values() {
        Map<String, String> map = new HashMap<>();
        map.put("username", "张三");
        map.put("password", "13812345678");
        map.put("mobilePhone", "13812345678");
        map.put("sex", "男");
        Collection<String> values = map.values();
        System.out.println(values.getClass());
        System.out.println(values);
        System.out.println("---------------------------------------------");
        values.remove("13812345678");
        System.out.println(values);
        System.out.println(map);
        System.out.println("---------------------------------------------");
        map.put("email", "admin@qq.com");
        System.out.println(values);
        System.out.println(map);
        System.out.println("---------------------------------------------");
        values.clear();
        System.out.println(values);
        System.out.println(map);
    }

    /**
     * entrySet()返回的Set类型的k-v视图，对该视图的修改（确切的说是remove，很明显是不支持add操作的）都会影响到原来Map，
     * 同理对原来Map的修改（put、remove）也会影响到keySet()视图
     * 对entrySet()视图的迭代删除中请使用Iterator.remove()
     */
    public static void entrySet() {
        Map<String, String> map = new HashMap<>();
        map.put("username", "张三");
        map.put("password", "123456");
        map.put("mobilePhone", "13812345678");
        map.put("sex", "男");
        Set<Map.Entry<String,String>> entrys = map.entrySet();
        System.out.println(entrys.getClass());
        System.out.println(entrys);
        System.out.println("---------------------------------------------");
        entrys.removeIf(entry -> "sex".equals(entry.getKey()));
        System.out.println(entrys);
        System.out.println(map);
        System.out.println("---------------------------------------------");
        map.put("email", "admin@qq.com");
        System.out.println(entrys);
        System.out.println(map);
        System.out.println("---------------------------------------------");
        entrys.clear();
        System.out.println(entrys);
        System.out.println(map);
    }

    /**
     * Map.getOrDefault(key, default)如果key不存在则返回指定的default值，
     * 如果key存在则不管key对应的value是否为空
     */
    public static void getOrDefault() {
        Map<String, String> map = new HashMap<>();
        map.put("username", "张三");
        map.put("password", null);
        map.put("mobilePhone", "13812345678");
        System.out.println(map.getOrDefault("password", "123456"));
        System.out.println(map.getOrDefault("sex", "男"));
    }

    /**
     * 遍历所有key，用BiFunction<K,V,V>产生的新V来替换对应的旧V
     */
    public static void replaceAll() {
        Map<String, String> map = new HashMap<>();
        map.put("username", "张三");
        map.put("password", "123456");
        map.put("mobilePhone", "13812345678");

        System.out.println(map);
        map.replaceAll((k, v) -> {
            if("123456".equals(v)) return "qwe123";
            return v;
        });
        System.out.println(map);
    }

    /**
     * 仅在K-V与指定参数都匹配的情况下才执行删除操作
     */
    public static void remove() {
        Map<String, String> map = new HashMap<>();
        map.put(null, null);
        map.put("username", "张三");
        map.put("password", null);

        System.out.println(map);

        map.remove(null, null);
        System.out.println(map);

        map.remove("username", "张三");
        System.out.println(map);

        map.remove("password", null);
        System.out.println(map);
    }

    public static void putIfAbsent() {
        Map<String, String> map = new HashMap<>();
        map.put("username", "张三");
        map.put("password", "123456");

        String old1 = map.putIfAbsent("sex", "男");
        System.out.println(old1);
        System.out.println(map);

        System.out.println("----------------------------------");

        String old2 = map.putIfAbsent("password", "qwe123");
        System.out.println(old2);
        System.out.println(map);
    }

    /**
     * 在给定的key不存在的情况下，如果Function返回的value不为null则将该key-value键值对放入map中，否则什么也不做，返回最新值
     */
    public static void computeIfAbsent() {
        Map<String, String> map = new HashMap<>();
        map.put("username", "张三");
        map.put("password", "123456");

        //由于sex key并不存在，所以参数中的Function会被执行
        String old1 = map.computeIfAbsent("sex", key -> {
            String sex = "男";
            System.out.println("get value from database：" + key + " = " + sex);
            return sex;
        });
        System.out.println(old1);
        System.out.println(map);

        System.out.println("---------------------------------------------------");

        //由于password key存在，所以参数中的Function不会被执行
        String old2 = map.computeIfAbsent("password", key -> {
            String password = "qwe123";
            System.out.println("get value from database：" + key + " = " + password);
            return password;
        });
        System.out.println(old2);
        System.out.println(map);

        System.out.println("---------------------------------------------------");

        //由于Function返回的为null，所以key=address,value=null的键值对不会被添加到map中去
        String old3 = map.computeIfAbsent("address", key -> {
            String address = null;
            System.out.println("get value from database：" + key + " = " + address);
            return address;
        });
        System.out.println(old3);
        System.out.println(map);
    }

    /**
     * 在给定key存在的情况下，如果BiFunction返回的值不为null则更新key对应的value，否则删除该key，返回最新值
     */
    public static void computeIfPresent() {
        Map<String, String> map = new HashMap<>();
        map.put("username", "张三");
        map.put("password", "123456");

        //由于sex key并不存在，所以参数中的BiFunction不会被执行
        String old1 = map.computeIfPresent("sex", (key, value) -> {
            String sex = "男";
            System.out.println("get value from database：" + key + " = " + sex);
            return sex;
        });
        System.out.println(old1);
        System.out.println(map);

        System.out.println("---------------------------------------------------");

        //由于password key存在，所以参数中的BiFunction会被执行
        String old2 = map.computeIfPresent("password", (key, value) -> {
            String password = "qwe123";
            System.out.println("get value from database：" + key + " = " + password);
            return password;
        });
        System.out.println(old2);
        System.out.println(map);

        System.out.println("---------------------------------------------------");

        //由于username key存在，所以参数中的BiFunction会被执行
        String old3 = map.computeIfPresent("username", (key, value) -> {
            String username = null;
            System.out.println("get value from database：" + key + " = " + username);
            return username;
        });
        System.out.println(old3);
        System.out.println(map);
    }

    /**
     * 如果BiFunction返回的newValue不为空则put(key, newValue)并返回newValue，否则删除旧的键值对并返回null
     */
    public static void compute() {
        Map<String, String> map = new HashMap<>();
        map.put("username", "张三");
        map.put("password", "123456");

        String old1 = map.compute("code", (key, value) -> "asd123");
        System.out.println(old1);
        System.out.println(map);

        System.out.println("---------------------------------------------------");

        String old2 = map.compute("code", (key, value) -> null);
        System.out.println(old2);
        System.out.println(map);
    }

    /**
     * 如果oldValue为null则value直接为newValue并返回newValue，否则newValue为BiFunction返回的；
     * newValue不为空则put(key, newValue)并返回newValue，否则删除旧的键值对并返回null
     */
    public static void merge() {
        Map<String, String> map = new HashMap<>();
        map.put("username", "张三");
        map.put("password", "123456");

        String old1 = map.merge("code", "asd123", (oldValue, newValue) -> newValue);
        System.out.println(old1);
        System.out.println(map);

        String old2 = map.merge("code", "asd123", (oldValue, newValue) -> null);
        System.out.println(old2);
        System.out.println(map);
    }

    public static void main(String[] args) {
        //forEach();
        //keySet();
        //values();
        //entrySet();
        //getOrDefault();
        //replaceAll();
        //putIfAbsent();
        //remove();
        //computeIfPresent();
        //computeIfAbsent();
        //compute();
        merge();
    }

}
