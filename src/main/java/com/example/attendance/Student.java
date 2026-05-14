package com.example.attendance;

import jakarta.validation.constraints.*;
import java.time.LocalDate;

public class Student {
    // 原有字段（保留不变，加了验证注解）
    @NotBlank(message = "姓名不能为空")
    private String name;

    @NotBlank(message = "学号不能为空")
    @Pattern(regexp = "^[0-9]{10}$", message = "学号必须是10位数字")
    private String studentId;

    @NotBlank(message = "班级不能为空")
    private String className;

    // ========== 新增字段 ==========
    @Pattern(regexp = "^男$|^女$", message = "性别只能填「男」或「女」")
    private String gender;

    @Past(message = "出生日期必须是过去的日期")
    private LocalDate birthDate;

    @Pattern(regexp = "^1[3-9]\\d{9}$", message = "手机号格式不正确")
    private String phone;

    // 无参构造（必须保留，Spring 需要）
    public Student() {}

    // 全参构造（可选，不影响运行）
    public Student(String name, String studentId, String className, String gender, LocalDate birthDate, String phone) {
        this.name = name;
        this.studentId = studentId;
        this.className = className;
        this.gender = gender;
        this.birthDate = birthDate;
        this.phone = phone;
    }

    // ========== Getter & Setter ==========
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public String getStudentId() { return studentId; }
    public void setStudentId(String studentId) { this.studentId = studentId; }
    public String getClassName() { return className; }
    public void setClassName(String className) { this.className = className; }
    public String getGender() { return gender; }
    public void setGender(String gender) { this.gender = gender; }
    public LocalDate getBirthDate() { return birthDate; }
    public void setBirthDate(LocalDate birthDate) { this.birthDate = birthDate; }
    public String getPhone() { return phone; }
    public void setPhone(String phone) { this.phone = phone; }
}