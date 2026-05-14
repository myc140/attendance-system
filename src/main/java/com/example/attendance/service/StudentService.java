package com.example.attendance.service;

import com.example.attendance.Student;
import java.util.List;

public interface StudentService {
    // 你原来的方法
    void addStudent(Student student);
    List<Student> findAllStudents();
    Student findStudentById(String studentId);
    void updateStudent(Student student);
    void deleteStudent(String studentId);

    // 新增：搜索+排序、批量删除
    List<Student> searchStudents(String keyword, String sortField, String sortOrder);
    void batchDeleteStudents(List<String> studentIds);
}