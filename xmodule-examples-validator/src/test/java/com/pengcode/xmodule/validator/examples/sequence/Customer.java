package com.pengcode.xmodule.validator.examples.sequence;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Null;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

import com.penglecode.xmodule.common.validator.DateTimePattern;
import com.penglecode.xmodule.common.validator.DateTimePattern.Type;
import com.penglecode.xmodule.common.validator.ValidationGroups.*;

public class Customer {

	@Null(message="客户ID必须为空!", groups={Create.class})
	@NotNull(message="客户ID不能为空!", groups={Update.class})
	private Long customerId;
	
	@NotBlank(message="客户名称不能为空!", groups={Create.class, Update.class, Constraint1.class})
	@Size(max=4, message="客户名称最多4个字符!", groups={Create.class, Update.class, Constraint3.class})
	@Pattern(regexp="[\\u4e00-\\u9fa5]+", message="客户名称只能为中文!", groups={Create.class, Update.class, Constraint2.class})
	private String customerName;
	
	private Integer customerType;
	
	@NotBlank(message="生日不能为空!", groups={Create.class, Update.class, Constraint1.class})
	@Pattern(regexp="\\d{4}\\-\\d{2}\\-\\d{2}", message="生日格式不正确!", groups={Create.class, Update.class, Constraint3.class})
	@DateTimePattern(type=Type.DATE, pattern="yyyy-MM-dd", message="生日格式不合法!(yyyy-MM-dd)", groups={Create.class, Update.class, Constraint2.class})
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
