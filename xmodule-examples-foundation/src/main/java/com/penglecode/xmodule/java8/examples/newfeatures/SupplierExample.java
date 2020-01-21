package com.penglecode.xmodule.java8.examples.newfeatures;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Supplier;

import com.penglecode.xmodule.java8.examples.model.Product;

public class SupplierExample {

	public static void main(String[] args) {
		testSupplier1();
	}
	
	public static void testSupplier1() {
		List<Product> productList = new ArrayList<Product>();
		productList.add(new Product(1L, "华为 Mate 9 Pro", 4709.0, 13, "1"));
		productList.add(new Product(2L, "苹果 iPhone 7 Plus 5.5寸", 5056.0, 3, "1"));
		productList.add(new Product(3L, "苹果 iPhone 6 4.7寸", 2999.0, 0, "1"));
		productList.add(new Product(4L, "MyCard 台湾/香港 300台币游戏充值卡", 67.8, 11, "0"));
		productList.add(new Product(5L, "OPPO R9手机 全网通", 2168.0, 5, "1"));
		productList.add(new Product(6L, "Letv/乐视 乐Pro3全网通", 1799.0, 74, "1"));
		productList.add(new Product(7L, "Xiaomi/小米 5S Plus", 1599.0, 0, "1"));
		productList.add(new Product(8L, "中国移动充值卡100元", 98.0, 0, "0"));
		
		productList.stream().forEach(product -> printProduct(() -> product));
	}
	
	protected static void printProduct(Supplier<Product> supplier) {
		System.out.println(supplier.get());
	}
	
}
