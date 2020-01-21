package com.penglecode.xmodule.common.serializer.protostuff;

import java.util.Set;

import org.springframework.util.Assert;

import com.penglecode.xmodule.BasePackage;
import com.penglecode.xmodule.common.serializer.ObjectSerializer;
import com.penglecode.xmodule.common.util.BeanUtils;
import com.penglecode.xmodule.common.util.ClassScanningUtils;

import io.protostuff.LinkedBuffer;
import io.protostuff.ProtostuffIOUtil;
import io.protostuff.Schema;
import io.protostuff.runtime.DefaultIdStrategy;
import io.protostuff.runtime.Delegate;
import io.protostuff.runtime.RuntimeEnv;
import io.protostuff.runtime.RuntimeSchema;
/**
 * 基于protostuff的对象序列化
 * 
 * @author 	pengpeng
 * @date	2019年1月28日 下午4:24:07
 */
@SuppressWarnings({"unchecked", "rawtypes"})
public class ProtostuffSerializer implements ObjectSerializer {

	public static final DefaultIdStrategy DEFAULT_STRATEGY = (DefaultIdStrategy) RuntimeEnv.ID_STRATEGY;
	
	public static final ProtostuffSerializer INSTANCE = new ProtostuffSerializer();
	
	public static final Schema<ObjectWrapper> SCHEMA;
	
	private static final int DEFAULT_BUFFER_SIZE = 5 * 1024;
	
	/**
	 * 经测试bufferSize的设置大小影响序列化效率(时间)
	 */
	private final int bufferSize;
	
	static {
		registerDelegates();
		SCHEMA = (Schema<ObjectWrapper>) RuntimeSchema.getSchema(ObjectWrapper.class);
	}
	
	public ProtostuffSerializer() {
		this(DEFAULT_BUFFER_SIZE);
	}

	public ProtostuffSerializer(int bufferSize) {
		super();
		Assert.isTrue(bufferSize > 0, "Parameter 'bufferSize' must be greater than zero!");
		this.bufferSize = bufferSize;
	}

	public byte[] serialize(Object object) {
		if(object == null){
			return null;
		}
		LinkedBuffer buffer = LinkedBuffer.allocate(DEFAULT_BUFFER_SIZE);
		try {
			return ProtostuffIOUtil.toByteArray(new ObjectWrapper(object), SCHEMA, buffer);
		} finally {
			buffer.clear();
		}
	}

	public <T> T deserialize(byte[] bytes) {
		if(bytes == null || bytes.length == 0){
			return null;
		}
		try {
			ObjectWrapper objectWrapper = new ObjectWrapper();
			ProtostuffIOUtil.mergeFrom(bytes, objectWrapper, SCHEMA);
			return (T) objectWrapper.getObject();
		} catch (Exception e) {
			throw new RuntimeException(e.getMessage(), e);
		}
	}
	
	public int getBufferSize() {
		return bufferSize;
	}

	protected static void registerDelegates() {
		Set<Class<? extends Delegate>> delegateClasses = ClassScanningUtils.scanPackageClasses(Delegate.class, BasePackage.class);
		delegateClasses.stream().map(BeanUtils::instantiateClass).forEach(DEFAULT_STRATEGY::registerDelegate);
    }
	
}