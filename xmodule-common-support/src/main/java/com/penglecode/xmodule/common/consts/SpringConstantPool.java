package com.penglecode.xmodule.common.consts;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.penglecode.xmodule.common.util.ObjectUtils;
import com.penglecode.xmodule.common.util.SpringUtils;
import com.penglecode.xmodule.common.util.StringUtils;

public class SpringConstantPool<T> implements ConstantPool<T> {

	private static final Logger LOGGER = LoggerFactory.getLogger(SpringConstantPool.class);
	
	@Override
	public T get(Constant<T> constant) {
		if (constant instanceof SpringBeanConstant) {
			T bean = null;
			try {
				if(!StringUtils.isEmpty(constant.name)) {
					bean = SpringUtils.getBean(constant.name);
				} else {
					bean = SpringUtils.getBean(constant.type);
				}
			} catch (Exception e) {
				LOGGER.error(e.getMessage());
			}
			return ObjectUtils.defaultIfNull(bean, constant.defaultValue);
		} else {
			return SpringUtils.getEnvProperty(constant.name, constant.type, constant.defaultValue);
		}
	}
	
}
