package com.penglecode.xmodule.java8.examples.newfeatures;

import java.time.Duration;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Random;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.LongStream;
import java.util.stream.Stream;

import com.penglecode.xmodule.java8.examples.model.Product;
import com.penglecode.xmodule.java8.examples.model.StudentScore;

/**
 * java.util.stream.Stream示例
 * 
 * Stream只能使用一次，使用完了就会close掉，不能重复使用
 * 
 * 分并行流和串行流，默认使用的方式都是串行使用的
 * 并行化操作使用fork/join框架，在复杂批量操作时要大幅提高效率
 * 
 * @author 	pengpeng
 * @date	2018年12月28日 上午9:25:20
 */
public class StreamExample {

	private static final List<Product> ALL_PRODUCT_LIST = new ArrayList<Product>();
	
	static {
		ALL_PRODUCT_LIST.add(new Product(1L, "华为 Mate 9 Pro", 4709.0, 13, "1"));
		ALL_PRODUCT_LIST.add(new Product(2L, "苹果 iPhone 7 Plus 5.5寸", 5056.0, 3, "1"));
		ALL_PRODUCT_LIST.add(new Product(3L, "苹果 iPhone 6 4.7寸", 2999.0, 0, "1"));
		ALL_PRODUCT_LIST.add(new Product(4L, "MyCard 台湾/香港 300台币游戏充值卡", 67.8, 11, "0"));
		ALL_PRODUCT_LIST.add(new Product(5L, "OPPO R9手机 全网通", 2168.0, 5, "1"));
		ALL_PRODUCT_LIST.add(new Product(6L, "Letv/乐视 乐Pro3全网通", 1799.0, 74, "1"));
		ALL_PRODUCT_LIST.add(new Product(7L, "Xiaomi/小米 5S Plus", 1599.0, 0, "1"));
		ALL_PRODUCT_LIST.add(new Product(8L, "中国移动充值卡100元", 98.0, 0, "0"));
	}
	
	private static final List<StudentScore> ALL_STUDENTSCORE_LIST = new ArrayList<StudentScore>();
	
	static {
		ALL_STUDENTSCORE_LIST.add(new StudentScore("张三", "语文", 78.0));
		ALL_STUDENTSCORE_LIST.add(new StudentScore("张三", "数学", 89.0));
		ALL_STUDENTSCORE_LIST.add(new StudentScore("张三", "英语", 66.0));
		ALL_STUDENTSCORE_LIST.add(new StudentScore("李四", "语文", 67.0));
		ALL_STUDENTSCORE_LIST.add(new StudentScore("李四", "数学", 92.0));
		ALL_STUDENTSCORE_LIST.add(new StudentScore("李四", "英语", 56.0));
		ALL_STUDENTSCORE_LIST.add(new StudentScore("王五", "语文", 96.0));
		ALL_STUDENTSCORE_LIST.add(new StudentScore("王五", "数学", 58.0));
		ALL_STUDENTSCORE_LIST.add(new StudentScore("王五", "英语", 89.0));
	}
	
	/**
	 * stream基本的过滤、转换操作示例
	 */
	public static void streamFilter1() {
		System.out.println("---------------------stream.filter | stream.collect---------------------");
		List<Product> filtedProducts = ALL_PRODUCT_LIST.stream().filter(product -> product.getInventory() > 0).collect(Collectors.toList());
		System.out.println(filtedProducts);
		System.out.println("---------------------stream.forEach---------------------");
		ALL_PRODUCT_LIST.stream().filter(product -> product.getInventory() > 0).forEach(System.out::println);
		System.out.println("---------------------stream.count---------------------");
		long count = ALL_PRODUCT_LIST.stream().filter(product -> product.getInventory() > 0).count();
		System.out.println(count);
		System.out.println("---------------------stream.map---------------------");
		ALL_PRODUCT_LIST.stream().map(product -> {
			if(product.getInventory() <= 0){
				product.setInventoryDesc("暂时缺货");
			}else if(product.getInventory() > 0 && product.getInventory() <= 10){
				product.setInventoryDesc("库存紧张,手慢无");
			}else{
				product.setInventoryDesc("库存(" + product.getInventory() + ")件");
			}
			return product;
		}).forEach(System.out::println);
		System.out.println("---------------------stream.limit,stream.sorted---------------------");
		Random random = new Random();
		random.ints().limit(10).sorted().forEach(System.out::println);
		
		System.out.println("---------------------stream.flatMap---------------------");
		Stream<List<Integer>> leveledList = Stream.of(
			 Arrays.asList(1),
			 Arrays.asList(2, 3),
			 Arrays.asList(4, 5, 6)
		);
		List<Integer> allInList = leveledList.flatMap((childList) -> childList.stream()).collect(Collectors.toList());
		System.out.println(allInList);
	}
	
	/**
	 * stream的findAny()和findFirst()方法示例
	 */
	public static void streamFilter2() {
		List<Integer> list = Arrays.asList(12, 34, 15, -21, 19, -4, 50, 101, -91, -1);
		List<Integer> negativeList = list.stream().filter(i -> (i < 0)).collect(Collectors.toList());
		System.out.println(negativeList);
		//打印出任意一个负数，其结果可能与findFirst()一致
		list.stream().filter(i -> (i < 0)).findAny().ifPresent(System.out::println);
		//打印出第一个负数
		list.stream().filter(i -> (i < 0)).findFirst().ifPresent(System.out::println);
	}
	
	/**
	 * stream的map()转换方法示例
	 */
	public static void streamMap() {
		ALL_PRODUCT_LIST.stream().map(Product::getProductName).forEach(System.out::println);
	}
	
	/**
	 * stream的streamFlatMap()转换方法示例
	 */
	public static void streamFlatMap() {
		int[][] array = new int[][] {
			{1, 2, 3, 4, 5},
			{1, 2, 3, 4},
			{1, 2, 3}
		};
		int sum = Arrays.stream(array).flatMapToInt(x -> Arrays.stream(x)).sum();
		System.out.println(sum);
	}
	
	/**
	 * stream的groupBy分组求count操作
	 */
	public static void streamCollectWithGroupByCount() {
		List<String> items = Arrays.asList("apple", "apple", "banana", "apple", "orange", "banana", "papaya");
		Map<String,Long> groupBys = items.stream().collect(Collectors.groupingBy(Function.identity(), Collectors.counting()));
		System.out.println(groupBys);
	}
	
	/**
	 * stream的sort()排序操作
	 */
	public static void streamCollectWithSort() {
		List<String> items = Arrays.asList("apple", "apple", "banana", "apple", "orange", "banana", "papaya");
		Map<String,Long> groupBys = items.stream().collect(Collectors.groupingBy(Function.identity(), Collectors.counting()));
		System.out.println(groupBys);
		
		Map<String,Long> sortedGroupBys = new LinkedHashMap<String,Long>();
		
		groupBys.entrySet().stream().sorted(Map.Entry.<String, Long>comparingByValue().reversed())
				.forEachOrdered(e -> sortedGroupBys.put(e.getKey(), e.getValue()));
		
		System.out.println(sortedGroupBys);
	}
	
	/**
	 * java统计abacbacdadbc中的每个字母出现的次数，输出格式是：a(4)b(3)c(3)d(2)
	 */
	public static void streamCollectWithGroupByCountSort() {
		String s = "abacbacdadbc";
		StringBuilder sb = new StringBuilder();
		Stream.of(s.split("")).collect(Collectors.groupingBy(Function.identity(), Collectors.counting())).entrySet()
				.stream().sorted(Map.Entry.<String, Long>comparingByKey())
				.forEachOrdered(e -> sb.append(e.getKey() + "(" + e.getValue() + ")"));
		System.out.println(sb);
	}
	
	/**
	 * stream的groupBy分组求sum操作
	 */
	public static void streamCollectWithGroupBySum() {
		Map<String,Double> sumScores = ALL_STUDENTSCORE_LIST.stream().collect(Collectors.groupingBy(StudentScore::getName, Collectors.summingDouble(StudentScore::getScore)));
		System.out.println(sumScores);
	}
	
	/**
	 * stream的groupBy分组及转换操作
	 */
	public static void streamCollectWithGroupByMapping() {
		ALL_PRODUCT_LIST.stream().collect(Collectors.groupingBy(Product::getProductType)).forEach((name, groups) -> {
			System.out.println(name + " = " + groups);
		});
		
		System.out.println("------------------------------");
		
		Map<String, List<String>> productTypedNames = ALL_PRODUCT_LIST.stream().collect(Collectors.groupingBy(Product::getProductType, Collectors.mapping(p -> p.getProductName(), Collectors.toList())));
		productTypedNames.forEach((name, groups) -> {
			System.out.println(name + " = " + groups);
		});
	}
	
	/**
	 * stream处理null元素
	 */
	public static void streamWithNulls() {
		String[] data = new String[] {"java", "python", "go", null, "ruby", null, "c"};
		Stream<String> language = null;
		language = Stream.of(data);
		System.out.println(language.sorted(Comparator.nullsLast(Comparator.comparing(Function.identity()))).collect(Collectors.toList()));
		language = Stream.of(data);
		System.out.println(language.filter(Objects::nonNull).collect(Collectors.toList()));
	}
	
	/**
	 * 串行化计算平方和
	 */
	public static void sequentialSumOfSquares() {
		Instant start = Instant.now();
		long result = LongStream.rangeClosed(1, 1000000000).sequential().map(x -> x * x).sum();
		Instant end = Instant.now();
		System.out.println(String.format(">>> 串行化计算平方和计算结果：%s, 耗时：%s (ms)", result, Duration.between(start, end).toMillis()));
	}
	
	/**
	 * 并行化计算平方和
	 * (明显要比上面串行化执行的快很多)
	 */
	public static void parallelSumOfSquares() {
		Instant start = Instant.now();
		long result = LongStream.rangeClosed(1, 1000000000).parallel().map(x -> x * x).sum();
		Instant end = Instant.now();
		System.out.println(String.format(">>> 并行化计算平方和计算结果：%s, 耗时：%s (ms)", result, Duration.between(start, end).toMillis()));
	}
	
	/**
	 * stream去重
	 */
	public static void streamDistinct() {
		Stream<String> chars = Stream.of("a", "b", "e", "x", "m", "a", "x", "y");
		chars.distinct().sorted(Comparator.comparing(Function.identity())).forEach(System.out::println);
	}
	
	/**
	 * Map排序
	 */
	public static void streamSortMap() {
		final Map<String,Integer> result = new LinkedHashMap<String,Integer>();
		result.put("go", 3);
		Map<String,Integer> langHots = new HashMap<String,Integer>();
		langHots.put("java", 15);
		langHots.put("c", 14);
		langHots.put("python", 5);
		langHots.put("ruby", 2);
		langHots.put("go", 1);
		langHots.put("c++", 8);
		langHots.entrySet().stream().sorted(Map.Entry.comparingByKey()).collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue, (oldValue, newValue) -> {
			System.out.println("oldValue = " + oldValue + ", newValue = " + newValue);
			return newValue;
		}, () -> result));
		System.out.println(result);
	}
	
	/**
	 * List转Map
	 */
	public static void streamListToMap() {
		Map<String,Double> map1 = ALL_PRODUCT_LIST.stream().collect(Collectors.toMap(Product::getProductName, Product::getUnitPrice));
		System.out.println(map1);
		Map<String,Product> map2 = ALL_PRODUCT_LIST.stream().filter(e -> e.getProductName() != null).collect(Collectors.toMap(Product::getProductName, Function.identity()));
		System.out.println(map2);
	}
	
	/**
	 * 原始类型数组 转 List
	 */
	public static void primitiveArrayToList() {
		// Integer[] number = {1, 2, 3, 4, 5, 6, 7, 8, 9, 10};
		// Arrays.asList(number);
		int[] number = {1, 2, 3, 4, 5, 6, 7, 8, 9, 10};
		List<Integer> numList = Arrays.stream(number).boxed().collect(Collectors.toList());
		System.out.println(numList);
	}
	
	public static void streamAnyMatch() {
		String[] array = new String[] {"A", "B", "C", "D"};
		boolean containsC = Arrays.stream(array).allMatch(x -> x.equals("C"));
		System.out.println(containsC);
	}
	
	public static void main(String[] args) {
		//streamFilter1();
		//streamFilter2();
		//streamMap();
		//streamFlatMap();
		//streamCollectWithGroupByCount();
		//streamCollectWithGroupByCountSort();
		//streamCollectWithSort();
		//streamCollectWithGroupBySum();
		streamCollectWithGroupByMapping();
		//streamWithNulls();
		//sequentialSumOfSquares();
		//parallelSumOfSquares();
		//streamDistinct();
		//streamSortMap();
		//streamListToMap();
		//primitiveArrayToList();
		//streamAnyMatch();
	}

}
