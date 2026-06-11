package com.example.attendance;

import com.alibaba.excel.annotation.ExcelProperty;
import jakarta.persistence.*;
import java.time.LocalDate;
import java.time.LocalTime;

@Entity
@Table(name = "attendance") // 关键：映射到数据库的 attendance 表
public class Attendance {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ExcelProperty("学号")
    private String studentId;

    @ExcelProperty("课程号")
    private String courseId;

    @ExcelProperty("考勤日期")
    private LocalDate attendanceDate;

    @ExcelProperty("打卡时间")
    private LocalTime checkTime;

    @ExcelProperty("状态")
    private String status;

    // 无参构造（必须）
    public Attendance() {}

    // Getter & Setter
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getStudentId() { return studentId; }
    public void setStudentId(String studentId) { this.studentId = studentId; }

    public String getCourseId() { return courseId; }
    public void setCourseId(String courseId) { this.courseId = courseId; }

    public LocalDate getAttendanceDate() { return attendanceDate; }
    public void setAttendanceDate(LocalDate attendanceDate) { this.attendanceDate = attendanceDate; }

    public LocalTime getCheckTime() { return checkTime; }
    public void setCheckTime(LocalTime checkTime) { this.checkTime = checkTime; }

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
}