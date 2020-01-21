package com.penglecode.xmodule.java8.examples.newfeatures;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import com.penglecode.xmodule.java8.examples.model.Product;

public class OptionalExample {

	public static void main(String[] args) {
		testGetProductById();
	}
	
	public static void testGetProductById() {
		ProductService productService = new ProductServiceImpl();
		Optional<Product> product1 = productService.getProductById(Optional.ofNullable(null));
		product1.ifPresent(product -> {
			System.out.println("getProductById(null) = " + product);
		});
		System.out.println(product1.orElse(null));
		
		Optional<Product> product2 = productService.getProductById(Optional.ofNullable(123L));
		product2.ifPresent(product -> {
			System.out.println("getProductById(123) = " + product);
		});
		
		Optional<Product> product3 = productService.getProductById(Optional.ofNullable(2L));
		product3.ifPresent(product -> {
			System.out.println("getProductById(2) = " + product);
		});
	}
	
	interface ProductService {
		
		Optional<Product> getProductById(Optional<Long> productId);
		
		Optional<List<Product>> getProductListByType(Optional<String> productType);
		
	}
	
	static class ProductServiceImpl implements ProductService {

		private ProductRepository repository = new ProductRepository();
		
		public ProductRepository getRepository() {
			return repository;
		}

		public void setRepository(ProductRepository repository) {
			this.repository = repository;
		}

		public Optional<Product> getProductById(Optional<Long> productId) {
			return productId.flatMap(pId -> {
				return Optional.ofNullable(getRepository().getProductById(pId));
			});
		}

		public Optional<List<Product>> getProductListByType(Optional<String> productType) {
			return productType.flatMap(pType -> {
				return Optional.ofNullable(getRepository().getProductListByType(pType));
			});
		}
		
	}
	
	static class ProductRepository {
		
		private List<Product> ALL_PRODUCT_LIST = new ArrayList<Product>();
		
		{
			ALL_PRODUCT_LIST.add(new Product(1L, "华为 Mate 9 Pro", 4709.0, 13, "1"));
			ALL_PRODUCT_LIST.add(new Product(2L, "苹果 iPhone 7 Plus 5.5寸", 5056.0, 3, "1"));
			ALL_PRODUCT_LIST.add(new Product(3L, "苹果 iPhone 6 4.7寸", 2999.0, 0, "1"));
			ALL_PRODUCT_LIST.add(new Product(4L, "MyCard 台湾/香港 300台币游戏充值卡", 67.8, 11, "0"));
			ALL_PRODUCT_LIST.add(new Product(5L, "OPPO R9手机 全网通", 2168.0, 5, "1"));
			ALL_PRODUCT_LIST.add(new Product(6L, "Letv/乐视 乐Pro3全网通", 1799.0, 74, "1"));
			ALL_PRODUCT_LIST.add(new Product(7L, "Xiaomi/小米 5S Plus", 1599.0, 0, "1"));
			ALL_PRODUCT_LIST.add(new Product(8L, "中国移动充值卡100元", 98.0, 0, "0"));
		}
		
		public Product getProductById(Long productId) {
			return ALL_PRODUCT_LIST.stream().filter(product -> product.getProductId().equals(productId)).findFirst().orElse(null);
			//return getSqlSessionTemplate().selectOne(getMapperKey("getProductById"), productId); //使用mybatis的一种正式的写法
		}

		public List<Product> getProductListByType(String productType) {
			return ALL_PRODUCT_LIST.stream().filter(product -> product.getProductType().equals(productType)).collect(Collectors.toList());
			//return getSqlSessionTemplate().selectList(getMapperKey("getProductListByType"), productType); //使用mybatis的一种正式的写法
		}
		
	}

}
