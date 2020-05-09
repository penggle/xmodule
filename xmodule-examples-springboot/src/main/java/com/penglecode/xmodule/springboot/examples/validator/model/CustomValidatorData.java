package com.penglecode.xmodule.springboot.examples.validator.model;

import javax.validation.constraints.NotBlank;

import com.penglecode.xmodule.springboot.examples.validator.custom.DateTimePattern;
import com.penglecode.xmodule.springboot.examples.validator.custom.DateTimePattern.Type;

public class CustomValidatorData {

	@NotBlank(message="发布日期不能为空!")
	@DateTimePattern(type=Type.DATE, pattern="yyyy/MM/dd", message="发布日期必须是合法的yyyy/MM/dd格式")
	@DateTimePattern(type=Type.DATE, pattern="yyyy-MM-dd", message="发布日期必须是合法的yyyy-MM-dd格式")
	private String publishDate;
	
	@NotBlank(message="发布时间不能为空!")
	@DateTimePattern(type=Type.TIME, pattern="HH:mm:ss", message="发布时间必须是合法的HH:mm:ss格式")
	private String publishTime;
	
	@NotBlank(message="发布日期时间不能为空!")
	@DateTimePattern(type=Type.DATETIME, pattern="yyyy-MM-dd HH:mm:ss", message="发布日期时间必须是合法的yyyy-MM-dd HH:mm:ss格式")
	private String publishDateTime;

	public String getPublishDate() {
		return publishDate;
	}

	public void setPublishDate(String publishDate) {
		this.publishDate = publishDate;
	}

	public String getPublishTime() {
		return publishTime;
	}

	public void setPublishTime(String publishTime) {
		this.publishTime = publishTime;
	}

	public String getPublishDateTime() {
		return publishDateTime;
	}

	public void setPublishDateTime(String publishDateTime) {
		this.publishDateTime = publishDateTime;
	}
	
}
