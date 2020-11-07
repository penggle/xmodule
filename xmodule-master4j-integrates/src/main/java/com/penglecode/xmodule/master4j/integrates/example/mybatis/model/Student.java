package com.penglecode.xmodule.master4j.integrates.example.mybatis.model;

import com.penglecode.xmodule.common.support.BaseModel;

/**
 * @author pengpeng
 * @version 1.0
 * @date 2020/9/26 22:42
 */
public class Student implements BaseModel<Student> {

    private static final long serialVersionUID = 1L;

    private Long studentId;

    private String studentName;

    private Character studentSex;

    private Integer studentAge;

    private String createTime;

    private String updateTime;

    //以下属于辅助字段

    private String courseName;

    public Long getStudentId() {
        return studentId;
    }

    public void setStudentId(Long studentId) {
        this.studentId = studentId;
    }

    public String getStudentName() {
        return studentName;
    }

    public void setStudentName(String studentName) {
        this.studentName = studentName;
    }

    public Character getStudentSex() {
        return studentSex;
    }

    public void setStudentSex(Character studentSex) {
        this.studentSex = studentSex;
    }

    public Integer getStudentAge() {
        return studentAge;
    }

    public void setStudentAge(Integer studentAge) {
        this.studentAge = studentAge;
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
        return "Student{" +
                "studentId=" + studentId +
                ", studentName='" + studentName + '\'' +
                ", studentSex=" + studentSex +
                ", studentAge=" + studentAge +
                ", createTime='" + createTime + '\'' +
                ", updateTime='" + updateTime + '\'' +
                ", courseName='" + courseName + '\'' +
                '}';
    }
}
