package com.example.attendance;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@SpringBootApplication
@RestController
public class AttendanceSystemApplication {

    public static void main(String[] args) {
        SpringApplication.run(AttendanceSystemApplication.class, args);
    }

    // 健康检查接口，用来验证项目是否启动成功
    @GetMapping("/check")
    public String check() {
        return "✅ 项目启动成功！接口可正常访问！";
    }

    // 你的hello接口
    @GetMapping("/hello")
    public String hello() {
        return "欢迎来到班级考勤管理系统！";
    }

    // 你的about接口
    @GetMapping("/about")
    public String about() {
        return "姓名：莫宇晨，专业：计算机科学与技术";
    }
}