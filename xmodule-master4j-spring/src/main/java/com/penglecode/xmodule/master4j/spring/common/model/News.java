package com.penglecode.xmodule.master4j.spring.common.model;

import com.penglecode.xmodule.common.support.BaseModel;

/**
 * 公共的示例News模型
 *
 * @author pengpeng
 * @version 1.0
 * @date 2020/9/10 22:32
 */
public class News implements BaseModel<News> {

	private static final long serialVersionUID = 1L;

	private String id;
	
	private String title;
	
	private String url;
	
	private String img;
	
	private String category1;
	
	private String category2;
	
	private String intro;
	
	private String tags;
	
	private String source;
	
	private String publishTime;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public String getImg() {
		return img;
	}

	public void setImg(String img) {
		this.img = img;
	}

	public String getCategory1() {
		return category1;
	}

	public void setCategory1(String category1) {
		this.category1 = category1;
	}

	public String getCategory2() {
		return category2;
	}

	public void setCategory2(String category2) {
		this.category2 = category2;
	}

	public String getIntro() {
		return intro;
	}

	public void setIntro(String intro) {
		this.intro = intro;
	}

	public String getTags() {
		return tags;
	}

	public void setTags(String tags) {
		this.tags = tags;
	}

	public String getSource() {
		return source;
	}

	public void setSource(String source) {
		this.source = source;
	}

	public String getPublishTime() {
		return publishTime;
	}

	public void setPublishTime(String publishTime) {
		this.publishTime = publishTime;
	}

	@Override
	public String toString() {
		return "{id=" + id + ", title=" + title + ", url=" + url + ", img=" + img + ", category1=" + category1
				+ ", category2=" + category2 + ", intro=" + intro + ", tags=" + tags + ", source=" + source
				+ ", publishTime=" + publishTime + "}";
	}
	
}
