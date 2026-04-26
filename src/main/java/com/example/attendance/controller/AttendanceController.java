package com.example.attendance.controller;

import com.example.attendance.Attendance;
import com.example.attendance.Result;
import com.example.attendance.service.AttendanceService;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import java.time.LocalDate;

@RestController
@RequestMapping("/attendance")
public class AttendanceController {

    private final AttendanceService attendanceService;

    // 注入 Service
    public AttendanceController(AttendanceService attendanceService) {
        this.attendanceService = attendanceService;
    }

    @GetMapping("/list")
    public Result<Page<Attendance>> listAttendance(
            // 分页参数（默认第0页，每页10条）
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,

            // 排序参数（默认按日期升序）
            @RequestParam(defaultValue = "attendanceDate") String sortField,
            @RequestParam(defaultValue = "asc") String sortDirection,

            // 多条件查询参数（可选）
            @RequestParam(required = false) String studentId,
            @RequestParam(required = false) LocalDate startDate,
            @RequestParam(required = false) LocalDate endDate,
            @RequestParam(required = false) String status) {

        Page<Attendance> attendancePage = attendanceService.getAttendanceList(
                studentId, startDate, endDate, status,
                page, size, sortField, sortDirection);

        return Result.success(attendancePage);
    }
}