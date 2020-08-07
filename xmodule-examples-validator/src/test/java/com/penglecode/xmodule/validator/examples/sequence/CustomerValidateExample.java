package com.penglecode.xmodule.validator.examples.sequence;

import com.penglecode.xmodule.common.util.CollectionUtils;
import com.penglecode.xmodule.common.validation.validator.ValidationGroups.Create;
import com.penglecode.xmodule.common.validation.validator.ValidationGroups.Update;
import com.penglecode.xmodule.validator.examples.AbstractValidatorExample;
import org.junit.Test;

import javax.validation.ConstraintViolation;
import javax.validation.groups.Default;
import javax.validation.metadata.BeanDescriptor;
import javax.validation.metadata.ConstraintDescriptor;
import javax.validation.metadata.PropertyDescriptor;
import java.util.Set;

public class CustomerValidateExample extends AbstractValidatorExample {

	public CustomerValidateExample() {
		super(false);
	}

	@Test
	public void validate4Create() {
		Customer customer = new Customer();
		customer.setCustomerId(null);
		customer.setCustomerName("张三qwe");
		customer.setCustomerType(5);
		customer.setBirthday("2012-12-12");

		BeanDescriptor beanDescriptor = getValidator().getConstraintsForClass(customer.getClass());
		Set<PropertyDescriptor> constrainedProperties = beanDescriptor.getConstrainedProperties();

		for(PropertyDescriptor constrainedDescriptor : constrainedProperties) {
			System.out.println("===============================================================================");
			String propertyName = constrainedDescriptor.getPropertyName();
			Set<ConstraintDescriptor<?>> constraintDescriptors = constrainedDescriptor.getConstraintDescriptors();
			for(ConstraintDescriptor<?> constraintDescriptor : constraintDescriptors) {
				System.out.println(propertyName + " : " + constraintDescriptor);
			}
		}

		Set<ConstraintViolation<Customer>> results = getValidator().validateProperty(customer, "customerName");
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
		
		Set<ConstraintViolation<Customer>> results = getValidator().validate(customer, Update.class);
		if(!CollectionUtils.isEmpty(results)) {
			results.forEach(System.out::println);
		}
	}
	
}
