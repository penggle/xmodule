package com.penglecode.xmodule.java8.examples.newfeatures;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.function.BiPredicate;
import java.util.function.Predicate;

import com.penglecode.xmodule.java8.examples.model.Product;

public class PredicateExample {

	public static void main(String[] args) {
		//testPredicate1();
		testPredicate2();
	}
	
	public static void testPredicate2() {
		List<Product> productList = new ArrayList<Product>();
		productList.add(new Product(1L, "华为 Mate 9 Pro", 4709.0, 13, "1"));
		productList.add(new Product(2L, "苹果 iPhone 7 Plus 5.5寸", 5056.0, 3, "1"));
		productList.add(new Product(3L, "苹果 iPhone 6 4.7寸", 2999.0, 0, "1"));
		productList.add(new Product(4L, "MyCard 台湾/香港 300台币游戏充值卡", 67.8, 11, "0"));
		productList.add(new Product(5L, "OPPO R9手机 全网通", 2168.0, 5, "1"));
		productList.add(new Product(6L, "Letv/乐视 乐Pro3全网通", 1799.0, 74, "1"));
		productList.add(new Product(7L, "Xiaomi/小米 5S Plus", 1599.0, 0, "1"));
		productList.add(new Product(8L, "中国移动充值卡100元", 98.0, 0, "0"));
		productList.stream().filter(product -> product.getInventory() > 0 && "1".equals(product.getProductType())).forEach(System.out::println);
	}
	
	public static void testPredicate1() {
		List<Product> productList = new ArrayList<Product>();
		productList.add(new Product(1L, "华为 Mate 9 Pro", 4709.0, 13, "1"));
		productList.add(new Product(2L, "苹果 iPhone 7 Plus 5.5寸", 5056.0, 3, "1"));
		productList.add(new Product(3L, "苹果 iPhone 6 4.7寸", 2999.0, 0, "1"));
		productList.add(new Product(4L, "MyCard 台湾/香港 300台币游戏充值卡", 67.8, 11, "0"));
		productList.add(new Product(5L, "OPPO R9手机 全网通", 2168.0, 5, "1"));
		productList.add(new Product(6L, "Letv/乐视 乐Pro3全网通", 1799.0, 74, "1"));
		productList.add(new Product(7L, "Xiaomi/小米 5S Plus", 1599.0, 0, "1"));
		productList.add(new Product(8L, "中国移动充值卡100元", 98.0, 0, "0"));
		
		System.out.println("-----------------查询全部--------------");
		filterProduct1(productList, product -> true); //查询全部结果集
		System.out.println("-----------------查询ID为偶数的结果集--------------");
		filterProduct1(productList, product -> product.getProductId() % 2 == 0); //查询ID为偶数的结果集
		System.out.println("-----------------查询库存大于0的结果集--------------");
		filterProduct1(productList, product -> product.getInventory() > 0); //查询库存大于0的结果集
		System.out.println("-----------------查询商品类型为实物商品的结果集--------------");
		filterProduct1(productList, product -> "1".equals(product.getProductType())); //查询商品类型为实物商品的结果集
		System.out.println("-----------------查询商品名称中含有'iPhone' && 库存大于0的结果集--------------");
		filterProduct2(productList, (productName, inventory) -> productName.contains("iPhone") && inventory > 0);
	}
	
	public static void filterProduct1(List<Product> productList, Predicate<Product> predicate) {
		Objects.requireNonNull(productList, "Parameter 'productList' can not be null!");
		for(Product product : productList){
			if(predicate.test(product)){
				System.out.println(product);
			}
		}
	}
	
	public static void filterProduct2(List<Product> productList, BiPredicate<String,Integer> predicate) {
		Objects.requireNonNull(productList, "Parameter 'productList' can not be null!");
		for(Product product : productList){
			if(predicate.test(product.getProductName(), product.getInventory())){
				System.out.println(product);
			}
		}
	}
}
