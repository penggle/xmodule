package com.penglecode.xmodule.validator.examples.sequence;

import com.penglecode.xmodule.common.util.CollectionUtils;
import com.penglecode.xmodule.common.util.ReflectionUtils;
import com.penglecode.xmodule.common.validation.validator.ValidationGroups.Create;
import com.penglecode.xmodule.common.validation.validator.ValidationGroups.OrderSequence;
import com.penglecode.xmodule.common.validation.validator.ValidationGroups.Update;
import com.penglecode.xmodule.validator.examples.AbstractValidatorExample;
import org.junit.Test;

import javax.validation.ConstraintViolation;
import javax.validation.metadata.BeanDescriptor;
import javax.validation.metadata.PropertyDescriptor;
import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.util.Set;

public class CustomerValidateExample extends AbstractValidatorExample {

	public CustomerValidateExample() {
		super(true);
	}

	@Test
	public void validate4Create() {
		Customer customer = new Customer();
		customer.setCustomerId(null);
		customer.setCustomerName("张三qwe");
		customer.setCustomerType(1);
		customer.setBirthday("2012-12-12");

		Set<ConstraintViolation<Customer>> results = getValidator().validate(customer, Create.class, OrderSequence.class);
		if(!CollectionUtils.isEmpty(results)) {
			results.forEach(System.out::println);
		}
	}

	@Test
	public void validate4Update() {
		Customer customer = new Customer();
		customer.setCustomerId(1L);
		customer.setCustomerName("张三123");
		customer.setCustomerType(2);
		customer.setBirthday("2012-12-12");

		BeanDescriptor beanDescriptor = getValidator().getConstraintsForClass(Customer.class);
		Set<PropertyDescriptor> constrainedProperties = beanDescriptor.getConstrainedProperties();
		for(PropertyDescriptor constrainedProperty : constrainedProperties) {
			System.out.println(constrainedProperty.getPropertyName());
		}

		System.out.println("=======================================================================");
		Set<Field> fields = ReflectionUtils.getAllFields(Customer.class);
		for(Field field : fields) {
			System.out.println(field.getName());
			System.out.println("------------------------------------------");
			for(Annotation anno : field.getAnnotations()) {
				System.out.println(anno);
			}
		}
		
		Set<ConstraintViolation<Customer>> results = getValidator().validate(customer, Update.class);
		if(!CollectionUtils.isEmpty(results)) {
			results.forEach(System.out::println);
		}
	}
	
}
