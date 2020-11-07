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
public interface StudentMapper {

    /**
     * 插入学生信息
     * @param student
     * @return
     */
    public int insertStudent(Student student);

    /**
     * 根据ID更新学生信息
     * @param student
     * @return
     */
    public int updateStudentById(Student student);

    /**
     * 根据ID删除学生信息
     * @param studentId
     * @return
     */
    public int deleteStudentById(Long studentId);

    /**
     * 根据ID查询学生信息
     * @param studentId
     * @return
     */
    public Student selectStudentById(Long studentId);

    /**
     * 查询所有学生列表
     * @return
     */
    public List<Student> selectAllStudentList();

    /**
     * 获取某个学生的老师列表
     * @param studentId
     * @return
     */
    public List<Teacher> selectTeacherListOfStudent(Long studentId);

}
