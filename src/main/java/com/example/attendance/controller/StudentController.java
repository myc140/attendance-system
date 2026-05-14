package com.example.attendance.controller;

import com.example.attendance.Result;
import com.example.attendance.Student;
import com.example.attendance.service.StudentService;
import jakarta.validation.Valid;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
public class StudentController {
    private final StudentService studentService;

    // 构造器注入，消除黄色警告
    public StudentController(StudentService studentService) {
        this.studentService = studentService;
    }

    // ========== 你原来的接口，完全保留 ==========
    @GetMapping("/student/info/{studentId}")
    public Result<Student> getStudentInfo(@PathVariable String studentId) {
        Student student = studentService.findStudentById(studentId);
        return Result.success(student);
    }

    @PostMapping("/student/attendance")
    public String checkAttendance(@RequestBody String studentId) {
        return "学号为 " + studentId + " 的学生打卡成功！";
    }

    @GetMapping("/student/courses")
    public Result<List<String>> getCourses() {
        return Result.success(List.of("Java 程序设计","Spring Boot 开发","计算机网络","数据库原理","数据结构与算法"));
    }

    // ========== 新增：带表单验证的新增学生接口 ==========
    @PostMapping("/student/add")
    public Result<String> addStudent(@Valid @RequestBody Student student, BindingResult result) {
        // 表单验证不通过，返回错误信息
        if (result.hasErrors()) {
            String errorMsg = result.getFieldErrors().stream()
                    .map(e -> e.getField() + "：" + e.getDefaultMessage())
                    .collect(Collectors.joining("；"));
            // 改成你 Result 类里的 fail 方法
            return Result.fail(errorMsg);
        }
        try {
            studentService.addStudent(student);
            return Result.success("新增学生成功");
        } catch (Exception e) {
            return Result.fail("新增失败：" + e.getMessage());
        }
    }

    // 新增：查询所有学生
    @GetMapping("/student/list")
    public Result<List<Student>> listStudent() {
        return Result.success(studentService.findAllStudents());
    }

    // 新增：更新学生信息
    @PutMapping("/student/update")
    public Result<String> updateStudent(@Valid @RequestBody Student student, BindingResult result) {
        if (result.hasErrors()) {
            String errorMsg = result.getFieldErrors().stream()
                    .map(e -> e.getField() + "：" + e.getDefaultMessage())
                    .collect(Collectors.joining("；"));
            return Result.fail(errorMsg);
        }
        studentService.updateStudent(student);
        return Result.success("更新学生成功");
    }

    // 新增：删除学生
    @DeleteMapping("/student/delete/{studentId}")
    public Result<String> deleteStudent(@PathVariable String studentId) {
        studentService.deleteStudent(studentId);
        return Result.success("删除学生成功");
    }
    // ========== 作业要求：带搜索+排序的学生列表接口 ==========
    @GetMapping("/student/search")
    public Result<List<Student>> searchStudents(
            @RequestParam(required = false) String keyword,
            @RequestParam(defaultValue = "student_id") String sortField,
            @RequestParam(defaultValue = "asc") String sortOrder) {
        List<Student> list = studentService.searchStudents(keyword, sortField, sortOrder);
        return Result.success(list);
    }

    // ========== 作业要求：批量删除接口 ==========
    @DeleteMapping("/student/batchDelete")
    public Result<String> batchDeleteStudents(@RequestBody List<String> studentIds) {
        if (studentIds == null || studentIds.isEmpty()) {
            return Result.fail("请选择要删除的学生");
        }
        studentService.batchDeleteStudents(studentIds);
        return Result.success("批量删除成功");
    }
}