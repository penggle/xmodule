package com.penglecode.xmodule.master4j.integrates.example.mybatis.model;

import com.penglecode.xmodule.common.support.BaseModel;

/**
 * @author pengpeng
 * @version 1.0
 * @date 2020/9/26 22:42
 */
public class Teacher implements BaseModel<Teacher> {

    private static final long serialVersionUID = 1L;

    private Long teacherId;

    private String teacherName;

    private Character teacherSex;

    private Integer teacherAge;

    private String createTime;

    private String updateTime;

    //以下属于辅助字段

    private String courseName;

    public Long getTeacherId() {
        return teacherId;
    }

    public void setTeacherId(Long teacherId) {
        this.teacherId = teacherId;
    }

    public String getTeacherName() {
        return teacherName;
    }

    public void setTeacherName(String teacherName) {
        this.teacherName = teacherName;
    }

    public Character getTeacherSex() {
        return teacherSex;
    }

    public void setTeacherSex(Character teacherSex) {
        this.teacherSex = teacherSex;
    }

    public Integer getTeacherAge() {
        return teacherAge;
    }

    public void setTeacherAge(Integer teacherAge) {
        this.teacherAge = teacherAge;
    }

    public String getCreateTime() {
        return createTime;
    }

    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }

    public String getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(String updateTime) {
        this.updateTime = updateTime;
    }

    public String getCourseName() {
        return courseName;
    }

    public void setCourseName(String courseName) {
        this.courseName = courseName;
    }

    @Override
    public String toString() {
        return "Teacher{" +
                "teacherId=" + teacherId +
                ", teacherName='" + teacherName + '\'' +
                ", teacherSex=" + teacherSex +
                ", teacherAge=" + teacherAge +
                ", createTime='" + createTime + '\'' +
                ", updateTime='" + updateTime + '\'' +
                ", courseName='" + courseName + '\'' +
                '}';
    }
}
