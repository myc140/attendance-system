package com.example.attendance.controller;

import com.example.attendance.Attendance;
import com.example.attendance.Result;
import com.example.attendance.service.AttendanceService;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import jakarta.servlet.http.HttpServletResponse;
import java.time.LocalDate;
import java.time.LocalTime;

@RestController
@RequestMapping("/attendance")
public class AttendanceController {

    private final AttendanceService attendanceService;

    public AttendanceController(AttendanceService attendanceService) {
        this.attendanceService = attendanceService;
    }

    // 原有列表查询接口（已新增 courseId 参数）
    @GetMapping("/list")
    public Result<Page<Attendance>> listAttendance(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "attendanceDate") String sortField,
            @RequestParam(defaultValue = "asc") String sortDirection,
            @RequestParam(required = false) String studentId,
            @RequestParam(required = false) LocalDate startDate,
            @RequestParam(required = false) LocalDate endDate,
            @RequestParam(required = false) String status,
            @RequestParam(required = false) String courseId) {

        Page<Attendance> attendancePage = attendanceService.getAttendanceList(
                studentId, startDate, endDate, status, courseId,
                page, size, sortField, sortDirection);

        return Result.success(attendancePage);
    }

    // 新增：打卡接口
    @GetMapping("/checkin")
    public Result<String> checkIn(
            @RequestParam String studentId,
            @RequestParam String courseId,
            @RequestParam LocalDate date,
            @RequestParam LocalTime checkTime) {
        String msg = attendanceService.checkIn(studentId, courseId, date, checkTime);
        return Result.success(msg);
    }

    // 新增：导出Excel接口
    @GetMapping("/export")
    public void exportExcel(
            @RequestParam(required = false) String studentId,
            @RequestParam(required = false) LocalDate startDate,
            @RequestParam(required = false) LocalDate endDate,
            @RequestParam(required = false) String status,
            @RequestParam(required = false) String courseId,
            HttpServletResponse response) {
        attendanceService.exportExcel(studentId, startDate, endDate, status, courseId, response);
    }
}