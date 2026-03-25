package com.example.attendance.service.impl;

import com.example.attendance.Student;
import com.example.attendance.dao.StudentDao;
import com.example.attendance.service.StudentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class StudentServiceImpl implements StudentService {

    @Autowired
    private StudentDao studentDao;

    @Override
    public void addStudent(Student student) {
        // 业务校验：学号不能为空
        if (student.getStudentId() == null || student.getStudentId().trim().isEmpty()) {
            throw new IllegalArgumentException("学号不能为空");
        }
        // 调用 Dao 层插入数据库
        studentDao.insertStudent(student);
    }
}