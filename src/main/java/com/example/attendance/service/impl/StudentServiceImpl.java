package com.example.attendance.service.impl;

import com.example.attendance.Student;
import com.example.attendance.dao.StudentDao;
import com.example.attendance.service.StudentService;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class StudentServiceImpl implements StudentService {

    private final StudentDao studentDao;

    public StudentServiceImpl(StudentDao studentDao) {
        this.studentDao = studentDao;
    }

    // 你原来的实现方法，保留不动
    @Override
    public void addStudent(Student student) {
        if (student.getStudentId() == null || student.getStudentId().trim().isEmpty()) {
            throw new IllegalArgumentException("学号不能为空");
        }
        studentDao.insertStudent(student);
    }

    @Override
    public List<Student> findAllStudents() {
        return studentDao.findAllStudents();
    }

    @Override
    public Student findStudentById(String studentId) {
        return studentDao.findStudentById(studentId);
    }

    @Override
    public void updateStudent(Student student) {
        studentDao.updateStudent(student);
    }

    @Override
    public void deleteStudent(String studentId) {
        studentDao.deleteStudent(studentId);
    }

    // 新增：实现接口方法
    @Override
    public List<Student> searchStudents(String keyword, String sortField, String sortOrder) {
        return studentDao.searchStudents(keyword, sortField, sortOrder);
    }

    @Override
    public void batchDeleteStudents(List<String> studentIds) {
        studentDao.batchDeleteStudents(studentIds);
    }
}