package com.penglecode.xmodule.master4j.integrates.example.mybatis.model;

import com.penglecode.xmodule.common.support.BaseModel;

/**
 * @author pengpeng
 * @version 1.0
 * @date 2020/9/26 22:48
 */
public class StudentCourse implements BaseModel<StudentCourse> {

    private static final long serialVersionUID = 1L;

    private Long teacherId;

    private Long studentId;

    private String courseName;

    public Long getTeacherId() {
        return teacherId;
    }

    public void setTeacherId(Long teacherId) {
        this.teacherId = teacherId;
    }

    public Long getStudentId() {
        return studentId;
    }

    public void setStudentId(Long studentId) {
        this.studentId = studentId;
    }

    public String getCourseName() {
        return courseName;
    }

    public void setCourseName(String courseName) {
        this.courseName = courseName;
    }

}
