package com.penglecode.xmodule.validator.examples.sequence;

import com.penglecode.xmodule.common.validation.validator.Enums;
import com.penglecode.xmodule.common.validation.validator.ValidationGroups.*;

import javax.validation.constraints.*;

public class Customer {

	@Null(message="客户ID必须为空!", groups={Create.class})
	@NotNull(message="客户ID不能为空!", groups={Update.class})
	private Long customerId;
	
	@NotBlank(message="客户名称不能为空!", groups={Create.class, Update.class, Order1.class})
	@Size(max=4, message="客户名称最多4个字符!", groups={Create.class, Update.class, Order2.class})
	@Pattern(regexp="[\\u4e00-\\u9fa5]+", message="客户名称只能为中文!", groups={Create.class, Update.class, Order3.class})
	private String customerName;

	@Enums(values={"0", "1", "2"}, message = "客户类型必须是{values}中的一个!", groups={Create.class, Update.class})
	private Integer customerType;
	
	@NotBlank(message="生日不能为空!", groups={Create.class, Update.class})
	@Pattern(regexp="\\d{4}-\\d{2}-\\d{2}", message="生日格式不正确!", groups={Create.class, Update.class})
	private String birthday;

	public Long getCustomerId() {
		return customerId;
	}

	public void setCustomerId(Long customerId) {
		this.customerId = customerId;
	}

	public String getCustomerName() {
		return customerName;
	}

	public void setCustomerName(String customerName) {
		this.customerName = customerName;
	}

	public Integer getCustomerType() {
		return customerType;
	}

	public void setCustomerType(Integer customerType) {
		this.customerType = customerType;
	}

	public String getBirthday() {
		return birthday;
	}

	public void setBirthday(String birthday) {
		this.birthday = birthday;
	}

}
