package com.example.attendance.controller;

import com.example.attendance.Result;
import com.example.attendance.Student;
import com.example.attendance.service.StudentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
public class StudentController {
    // 注入 Service 层
    @Autowired
    private StudentService studentService;

    /**
     * 学生信息查询接口（路径参数）
     * 访问路径：/student/info/{studentId}（GET 请求）
     * @param studentId 学生学号（路径变量）
     * @return 统一格式的学生信息
     */
    @GetMapping("/student/info/{studentId}")
    public Result<Student> getStudentInfo(@PathVariable String studentId) {
        // 模拟查询到的学生数据
        Student student = new Student();
        student.setName("莫宇晨");
        student.setStudentId(studentId);
        student.setClassName("计算机科学与技术2024级3班");

        // 封装为统一响应并返回
        return Result.success(student);
    }
    /**
     * 学生考勤打卡接口
     * 访问路径：/student/attendance（POST 请求）
     * @param studentId 学生学号（通过请求体传递）
     * @return 打卡成功消息
     */
    @PostMapping("/student/attendance")
    public String checkAttendance(@RequestBody String studentId) {
        return "学号为 " + studentId + " 的学生打卡成功！";
    }
    /**
     * 获取学生课程列表接口
     * 访问路径：/student/courses（GET 请求）
     * @return 统一格式的课程名称列表
     */
    @GetMapping("/student/courses")
    public Result<List<String>> getCourses() {
        List<String> courses = new ArrayList<>();
        courses.add("Java 程序设计");
        courses.add("Spring Boot 开发");
        courses.add("计算机网络");
        courses.add("数据库原理");
        courses.add("数据结构与算法");

        return Result.success(courses);
    }
}
