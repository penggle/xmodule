package com.penglecode.xmodule.common.validation.support;

import com.penglecode.xmodule.common.util.ArrayUtils;
import com.penglecode.xmodule.common.util.CollectionUtils;
import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.util.Assert;

import javax.validation.ConstraintViolation;
import javax.validation.GroupSequence;
import javax.validation.Validator;
import javax.validation.executable.ExecutableValidator;
import javax.validation.metadata.BeanDescriptor;
import javax.validation.metadata.ConstraintDescriptor;
import javax.validation.metadata.PropertyDescriptor;
import java.util.Arrays;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Set;

/**
 * 自定义的Validator
 *
 * @author pengpeng
 * @version 1.0
 * @date 2020/8/6 13:19
 */
public class CustomValidator implements Validator {

    private final Validator delegate;

    public CustomValidator(Validator delegate) {
        Assert.notNull(delegate, "Property 'delegate' must be required!");
        this.delegate = delegate;
    }

    @Override
    public <T> Set<ConstraintViolation<T>> validate(T object, Class<?>... groups) {
        if(object != null) {
            //如果groups中没有javax.validation原生的顺序控制方式，并且使用了自定义的顺序控制逻辑
            if(!containsGroupSequence(groups) && containsValidationOrder(object)) {
                return validateBean(object, groups);
            }
        }
        return delegate.validate(object, groups);
    }

    /**
     * 自定义的bean校验逻辑
     *
     * @param object    - 被校验的bean
     * @param groups    - 在这个方法中，groups仅仅是起到业务分组的作用，不会涉及到排序，且永远不会为空，至少是{Default.class}
     * @param <T>
     * @return
     */
    protected <T> Set<ConstraintViolation<T>> validateBean(T object, Class<?>... groups) {
        BeanDescriptor beanDescriptor = getConstraintsForClass(object.getClass());
        Set<PropertyDescriptor> constrainedProperties = beanDescriptor.getConstrainedProperties();
        for(PropertyDescriptor constrainedDescriptor : constrainedProperties) {
            String propertyName = constrainedDescriptor.getPropertyName();
            Set<ConstraintDescriptor<?>> constraintDescriptors = constrainedDescriptor.getConstraintDescriptors();
            for(ConstraintDescriptor<?> constraintDescriptor : constraintDescriptors) {
                System.out.println(propertyName + " : " + constraintDescriptor);
            }
        }
        return null;
    }

    protected Set<String> getValidateProperties() {
        return null;
    }

    protected <T> Map<String,ValidationOrder> doGetValidateProperties(T object, Class<?>... groups) {
        Set<String> validateProperties = new LinkedHashSet<>(32);
        BeanDescriptor beanDescriptor = getConstraintsForClass(object.getClass());
        Set<PropertyDescriptor> constrainedProperties = beanDescriptor.getConstrainedProperties();
        String propertyName;
        Set<ConstraintDescriptor<?>> constraintDescriptors;
        for(PropertyDescriptor constrainedDescriptor : constrainedProperties) {
            propertyName = constrainedDescriptor.getPropertyName();
            constraintDescriptors = constrainedDescriptor.getConstraintDescriptors();
            if(!CollectionUtils.isEmpty(constraintDescriptors)) {
                boolean doValidate = false; //当前属性字段是否纳入校验之列
                outerLoop:
                for(ConstraintDescriptor<?> constraintDescriptor : constraintDescriptors) {
                    for(Class<?> groupClass : groups) {
                        doValidate = doValidate || constraintDescriptor.getGroups().contains(groupClass);
                        if(doValidate) {
                            break outerLoop;
                        }
                    }
                }
                if(doValidate) {

                }
            }
        }
        return null;
    }

    @Override
    public <T> Set<ConstraintViolation<T>> validateProperty(T object, String propertyName, Class<?>... groups) {
        return delegate.validateProperty(object, propertyName, groups);
    }

    @Override
    public <T> Set<ConstraintViolation<T>> validateValue(Class<T> beanType, String propertyName, Object value, Class<?>... groups) {
        return delegate.validateValue(beanType, propertyName, value, groups);
    }

    @Override
    public BeanDescriptor getConstraintsForClass(Class<?> clazz) {
        return delegate.getConstraintsForClass(clazz);
    }

    @Override
    public <T> T unwrap(Class<T> type) {
        return delegate.unwrap(type);
    }

    @Override
    public ExecutableValidator forExecutables() {
        return delegate.forExecutables();
    }

    protected boolean containsGroupSequence(Class<?>[] groups) {
        if(!ArrayUtils.isEmpty(groups)) {
            for(Class<?> group : groups) {
                return group != null && AnnotationUtils.isCandidateClass(group, GroupSequence.class);
            }
        }
        return false;
    }

    protected boolean containsValidationOrder(Object bean) {
        return AnnotationUtils.isCandidateClass(bean.getClass(), ValidationOrder.class);
    }

}
