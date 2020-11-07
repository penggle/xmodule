package com.penglecode.xmodule.master4j.integrates.example.mybatis.mapper;

import com.penglecode.xmodule.common.support.DefaultDatabase;
import com.penglecode.xmodule.master4j.integrates.example.mybatis.model.Student;
import com.penglecode.xmodule.master4j.integrates.example.mybatis.model.Teacher;

import java.util.List;

/**
 * @author pengpeng
 * @version 1.0
 * @date 2020/9/26 23:01
 */
@DefaultDatabase
public interface TeacherMapper {

    /**
     * 插入教师信息
     * @param teacher
     * @return
     */
    public int insertTeacher(Teacher teacher);

    /**
     * 根据ID更新教师信息
     * @param teacher
     * @return
     */
    public int updateTeacherById(Teacher teacher);

    /**
     * 根据ID删除教师信息
     * @param teacherId
     * @return
     */
    public int deleteTeacherById(Long teacherId);

    /**
     * 根据ID查询教师信息
     * @param teacherId
     * @return
     */
    public Teacher selectTeacherById(Long teacherId);

    /**
     * 查询所有教师列表
     * @return
     */
    public List<Teacher> selectAllTeacherList();

    /**
     * 查询某个教师某门课的学生列表
     * @param teacherId
     * @param courseName    - 可选
     * @return
     */
    public List<Student> selectStudentListOfTeacher(Long teacherId, String courseName);

}
