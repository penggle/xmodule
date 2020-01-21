package com.penglecode.xmodule.examples.springcloud.api.model;

import com.penglecode.xmodule.common.support.BaseModel;

/**
 * 新闻数据模型
 * 
 * @author 	pengpeng
 * @date 	2020年1月18日 下午5:23:10
 */
public class News implements BaseModel<News> {

	private static final long serialVersionUID = 1L;

	private String title;
	
	private String path;
	
	private String image;
	
	private String passtime;

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getPath() {
		return path;
	}

	public void setPath(String path) {
		this.path = path;
	}

	public String getImage() {
		return image;
	}

	public void setImage(String image) {
		this.image = image;
	}

	public String getPasstime() {
		return passtime;
	}

	public void setPasstime(String passtime) {
		this.passtime = passtime;
	}
	
}
