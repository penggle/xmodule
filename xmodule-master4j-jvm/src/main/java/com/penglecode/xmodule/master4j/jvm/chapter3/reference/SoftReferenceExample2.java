package com.penglecode.xmodule.master4j.jvm.chapter3.reference;

import java.lang.ref.ReferenceQueue;
import java.lang.ref.SoftReference;
import java.util.Random;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

/**
 * 使用软引用来构建一个缓存系统的示例
 * 
 * @author 	pengpeng
 * @date 	2020年6月19日 下午4:54:03
 */
public class SoftReferenceExample2 {

	private static final Random RANDOM = new Random();
	
	protected Product getProductById(String productId) {
		Product product = new Product();
		product.setProductId(productId);
		product.setProductName("Apple/苹果 iPhone 11 4G全网通智能手机正品苏宁易购官方旗舰店苹果11" + productId);
		product.setInventory(RANDOM.nextInt(1000));
		product.setUnitPrice(4999.0);
		return product;
	}
	
	/**
	 * 运行此方法时：
	 * 1、如果不设置-Xmx参数约束堆内存大小的话，是不会打印clean()方法中的"clean cache reference."
	 * 2、如果设置了-Xmx20m来约束堆内存大小的话，就会立马打印clean()方法中的"clean cache reference."，
	 * 	  因为软引用在堆内存吃紧(即将发生OOM)时，GC将会强制回收一部分软引用(策略是LRU最近最少使用的那些，见SoftReference实现)所引用的对象来释放堆空间避免OOM
	 */
	public void cacheTest() {
		Cache<String,Product> productCache = new Cache<String,Product>(){
			@Override
			public Product load(String key) {
				return getProductById(key);
			}
		};
		
		for(int i = 1;; i++) {
			Product product = getProductById(String.valueOf(i));
			productCache.add(product);
		}
	}
	
	public static void main(String[] args) {
		SoftReferenceExample2 example = new SoftReferenceExample2();
		example.cacheTest();
	}
	
	public static class Product implements Cacheable<String> {

		private String productId;
		
		private String productName;
		
		private Double unitPrice;
		
		private Integer inventory;
		
		public String getProductId() {
			return productId;
		}

		public void setProductId(String productId) {
			this.productId = productId;
		}

		public String getProductName() {
			return productName;
		}

		public void setProductName(String productName) {
			this.productName = productName;
		}

		public Double getUnitPrice() {
			return unitPrice;
		}

		public void setUnitPrice(Double unitPrice) {
			this.unitPrice = unitPrice;
		}

		public Integer getInventory() {
			return inventory;
		}

		public void setInventory(Integer inventory) {
			this.inventory = inventory;
		}

		@Override
		public String key() {
			return productId;
		}
		
	}
	
	@SuppressWarnings("unchecked")
	public static abstract class Cache<K, V extends Cacheable<K>> {
		
		/**
		 * ReferenceQueue存储的是被GC回收的对象V的软引用
		 * 当对象即被回收时，整个reference对象( 而不是被回收的对象 )会被放到queue里面，然后外部程序即可通过监控这个queue拿到相应的数据了。
		 * ReferenceQueue是作为 JVM GC与上层Reference对象管理之间的一个消息传递方式，它使得我们可以对所监听的对象引用可达发生变化时做一些处理
		 */
		private final ReferenceQueue<V> referenceQueue = new ReferenceQueue<>();
		
		private final ConcurrentMap<K,CacheReference> referenceMap = new ConcurrentHashMap<>();
		
		private int cacheReferenceCleanThreshold = 5000;
		
		public abstract V load(K key);
		
		public void add(V o) {
			if(referenceMap.size() >= cacheReferenceCleanThreshold) {
				clean();
			}
			CacheReference reference = new CacheReference(o, referenceQueue);
			reference = referenceMap.put(reference.key, reference);
			if(reference != null) { //旧的覆盖新的?则需要手动回收旧的对象
				reference.clear();
			}
		}
		
		public V get(K key) {
			V o = null;
			CacheReference reference = referenceMap.get(key);
			if(reference != null) {
				o = reference.get();
				if(o != null) {
					return o;
				}
			}
			o = load(key);
			add(o);
			return o;
		}
		
		public void remove(K key) {
			CacheReference reference = referenceMap.remove(key);
			if(reference != null) {
				reference.clear();
			}
		}
		
		public void clean() {
			CacheReference reference = null;
			boolean collected = false;
			while((reference = (Cache<K, V>.CacheReference) referenceQueue.poll()) != null) {
				referenceMap.remove(reference.key);
				collected = true;
			}
			if(collected) {
				System.out.println("clean cache reference.");
			}
		}
		
		public void clear() {
			referenceMap.clear();
			System.gc();
			System.runFinalization();
		}
		
		/**
		 * 此软引用的子类是为clean方法服务的，clean方法执行时需要根据key删除软引用本身，
		 * 而此时软引用里面所持引用对象(即SoftReference#get())必定是null，无法获得key，所以此处必须记录key
		 */
		class CacheReference extends SoftReference<V> {

			private final K key;
			
			public CacheReference(V referent, ReferenceQueue<? super V> q) {
				super(referent, q);
				this.key = referent.key();
			}
			
		}
		
	}
	
	
	public static interface Cacheable<K> {
		
		public K key();
		
	}
	
}
