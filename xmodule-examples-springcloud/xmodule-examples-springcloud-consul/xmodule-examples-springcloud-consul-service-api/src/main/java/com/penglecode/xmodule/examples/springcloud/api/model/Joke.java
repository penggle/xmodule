package com.penglecode.xmodule.examples.springcloud.api.model;

import com.penglecode.xmodule.common.support.BaseModel;

/**
 * 笑话 - 数据模型
 * 
 * @author 	pengpeng
 * @date	2019年11月13日 上午10:52:39
 */
public class Joke implements BaseModel<Joke> {

	private static final long serialVersionUID = 1L;

	private String sid;
	
	private String uid;
	
	private String name;
	
	private String text;
	
	private String type;
	
	private String header;
	
	private String thumbnail;
	
	private String video;
	
	private String images;
	
	private String up;
	
	private String down;
	
	private String forward;
	
	private String comment;
	
	private String passtime;

	public String getSid() {
		return sid;
	}

	public void setSid(String sid) {
		this.sid = sid;
	}

	public String getUid() {
		return uid;
	}

	public void setUid(String uid) {
		this.uid = uid;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getText() {
		return text;
	}

	public void setText(String text) {
		this.text = text;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getHeader() {
		return header;
	}

	public void setHeader(String header) {
		this.header = header;
	}

	public String getThumbnail() {
		return thumbnail;
	}

	public void setThumbnail(String thumbnail) {
		this.thumbnail = thumbnail;
	}

	public String getVideo() {
		return video;
	}

	public void setVideo(String video) {
		this.video = video;
	}

	public String getImages() {
		return images;
	}

	public void setImages(String images) {
		this.images = images;
	}

	public String getUp() {
		return up;
	}

	public void setUp(String up) {
		this.up = up;
	}

	public String getDown() {
		return down;
	}

	public void setDown(String down) {
		this.down = down;
	}

	public String getForward() {
		return forward;
	}

	public void setForward(String forward) {
		this.forward = forward;
	}

	public String getComment() {
		return comment;
	}

	public void setComment(String comment) {
		this.comment = comment;
	}

	public String getPasstime() {
		return passtime;
	}

	public void setPasstime(String passtime) {
		this.passtime = passtime;
	}
	
}