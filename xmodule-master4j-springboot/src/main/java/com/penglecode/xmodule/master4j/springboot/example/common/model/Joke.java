package com.penglecode.xmodule.master4j.springboot.example.common.model;

import com.penglecode.xmodule.common.support.BaseModel;

/**
 * 笑话段子
 *
 * @author pengpeng
 * @version 1.0
 * @date 2020/10/3 13:13
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

    private String images;

    private Integer comment;

    private Integer forward;

    private Integer up;

    private Integer down;

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

    public String getImages() {
        return images;
    }

    public void setImages(String images) {
        this.images = images;
    }

    public Integer getComment() {
        return comment;
    }

    public void setComment(Integer comment) {
        this.comment = comment;
    }

    public Integer getForward() {
        return forward;
    }

    public void setForward(Integer forward) {
        this.forward = forward;
    }

    public Integer getUp() {
        return up;
    }

    public void setUp(Integer up) {
        this.up = up;
    }

    public Integer getDown() {
        return down;
    }

    public void setDown(Integer down) {
        this.down = down;
    }

    public String getPasstime() {
        return passtime;
    }

    public void setPasstime(String passtime) {
        this.passtime = passtime;
    }

    @Override
    public String toString() {
        return "Joke{" +
                "sid='" + sid + '\'' +
                ", uid='" + uid + '\'' +
                ", name='" + name + '\'' +
                ", text='" + text + '\'' +
                ", type='" + type + '\'' +
                ", header='" + header + '\'' +
                ", thumbnail='" + thumbnail + '\'' +
                ", images='" + images + '\'' +
                ", comment=" + comment +
                ", forward=" + forward +
                ", up=" + up +
                ", down=" + down +
                ", passtime='" + passtime + '\'' +
                '}';
    }
}
