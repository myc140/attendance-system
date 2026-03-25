package com.example.attendance.dao;

import com.example.attendance.Student;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

// 必须有这个注解，让 Spring 把它当成 Bean 管理
@Repository
public class StudentDao {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    // 方法名必须是 insertStudent，和 Service 里调用的一致
    public void insertStudent(Student student) {
        String sql = "INSERT INTO student(name, student_id, class_name) VALUES (?, ?, ?)";
        jdbcTemplate.update(sql,
                student.getName(),
                student.getStudentId(),
                student.getClassName()
        );
    }
}