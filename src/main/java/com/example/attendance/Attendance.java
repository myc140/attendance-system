package com.example.attendance;

import com.alibaba.excel.annotation.ExcelProperty;
import jakarta.persistence.*;
import java.time.LocalDate;
import java.time.LocalTime;

@Entity
@Table(name = "`attendance`")
public class Attendance {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer id;

    @ExcelProperty("学号")
    @Column(name = "student_id", nullable = false, length = 20)
    private String studentId;

    @ExcelProperty("学生姓名")
    @Column(name = "student_name", length = 50)
    private String studentName;

    @ExcelProperty("课程编号")
    @Column(name = "course_id", nullable = false, length = 20)
    private String courseId;

    @ExcelProperty("签到时间")
    @Column(name = "check_in_time", nullable = false, columnDefinition = "datetime")
    private java.time.LocalDateTime checkInTime;

    @ExcelProperty("座位行")
    @Column(name = "seat_row")
    private Integer seatRow;

    @ExcelProperty("座位列")
    @Column(name = "seat_col")
    private Integer seatCol;

    @ExcelProperty("状态")
    @Column(name = "status", nullable = false, length = 20, columnDefinition = "varchar(20) default 'NORMAL'")
    private String status;

    @ExcelProperty("IP地址")
    @Column(name = "ip", length = 15)
    private String ip;

    @ExcelProperty("创建时间")
    @Column(name = "create_time", columnDefinition = "datetime default CURRENT_TIMESTAMP")
    private java.time.LocalDateTime createTime = java.time.LocalDateTime.now();

    @ExcelProperty("打卡时间")
    @Column(name = "check_time")
    private LocalTime checkTime;

    // 无参构造（必须）
    public Attendance() {}

    // Getter & Setter
    public Integer getId() { return id; }
    public void setId(Integer id) { this.id = id; }

    public String getStudentId() { return studentId; }
    public void setStudentId(String studentId) { this.studentId = studentId; }

    public String getCourseId() { return courseId; }
    public void setCourseId(String courseId) { this.courseId = courseId; }

    public String getStudentName() { return studentName; }
    public void setStudentName(String studentName) { this.studentName = studentName; }

    public java.time.LocalDateTime getCheckInTime() { return checkInTime; }
    public void setCheckInTime(java.time.LocalDateTime checkInTime) { this.checkInTime = checkInTime; }

    public Integer getSeatRow() { return seatRow; }
    public void setSeatRow(Integer seatRow) { this.seatRow = seatRow; }

    public Integer getSeatCol() { return seatCol; }
    public void setSeatCol(Integer seatCol) { this.seatCol = seatCol; }

    public String getIp() { return ip; }
    public void setIp(String ip) { this.ip = ip; }

    public java.time.LocalDateTime getCreateTime() { return createTime; }
    public void setCreateTime(java.time.LocalDateTime createTime) { this.createTime = createTime; }

    public LocalTime getCheckTime() { return checkTime; }
    public void setCheckTime(LocalTime checkTime) { this.checkTime = checkTime; }

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }

    public LocalDate getAttendanceDate() { return checkInTime != null ? checkInTime.toLocalDate() : null; }
    public void setAttendanceDate(LocalDate attendanceDate) {
        if (attendanceDate != null && checkInTime != null) {
            this.checkInTime = attendanceDate.atTime(checkInTime.toLocalTime());
        } else if (attendanceDate != null) {
            this.checkInTime = attendanceDate.atStartOfDay();
        }
    }
}