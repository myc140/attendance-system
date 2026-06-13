package com.example.attendance;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import java.time.LocalDate;

@Entity
@Table(name = "`student`")
public class Student {
    @NotBlank(message = "姓名不能为空")
    @Column(name = "name", length = 50)
    private String name;

    @Id
    @NotBlank(message = "学号不能为空")
    @Pattern(regexp = "^[0-9]{8}$", message = "学号必须是8位数字")
    @Column(name = "student_id", nullable = false, length = 50)
    private String studentId;

    @NotBlank(message = "班级不能为空")
    @Column(name = "class_name", length = 50)
    private String className;

    @Pattern(regexp = "^男$|^女$", message = "性别只能填「男」或「女」")
    @Column(name = "gender", length = 2)
    private String gender;

    @Past(message = "出生日期必须是过去的日期")
    @Column(name = "birth_date")
    private LocalDate birthDate;

    @Pattern(regexp = "^1\\d{10}$", message = "手机号必须是11位数字")
    @Column(name = "phone", length = 11)
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