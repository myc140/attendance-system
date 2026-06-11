package com.example.attendance.controller;

import com.example.attendance.Attendance;
import com.example.attendance.AuditLog;
import com.example.attendance.Result;
import com.example.attendance.dao.AuditLogRepository;
import com.example.attendance.service.AttendanceService;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/attendance")
public class AttendanceController {

    private final AttendanceService attendanceService;
    private final AuditLogRepository auditLogRepository;

    public AttendanceController(AttendanceService attendanceService, AuditLogRepository auditLogRepository) {
        this.attendanceService = attendanceService;
        this.auditLogRepository = auditLogRepository;
    }

    @GetMapping("/list")
    public Result<Page<Attendance>> list(
            @RequestParam(required = false) String studentId,
            @RequestParam(required = false) String courseId,
            @RequestParam(required = false) LocalDate startDate,
            @RequestParam(required = false) LocalDate endDate,
            @RequestParam(required = false) String status,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "attendanceDate") String sortField,
            @RequestParam(defaultValue = "desc") String sortDir) {
        return Result.success(attendanceService.list(studentId, courseId, startDate, endDate, status, page, size, sortField, sortDir));
    }

    @PostMapping("/checkin")
    public Result<String> checkIn(
            @RequestParam String studentId,
            @RequestParam String courseId,
            @RequestParam LocalDate date,
            @RequestParam LocalTime checkTime) {
        String message = attendanceService.checkIn(studentId, courseId, date, checkTime);
        auditLogRepository.save(new AuditLog("student", "CHECK_IN", studentId, "课程=" + courseId + "，打卡记录"));
        return Result.success(message);
    }

    @GetMapping("/export")
    public ResponseEntity<byte[]> exportAttendance() {
        byte[] data = attendanceService.exportAttendanceExcel();
        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=attendance-export.xlsx")
                .header(HttpHeaders.CONTENT_TYPE, "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet")
                .body(data);
    }

    @PostMapping("/import")
    public Result<String> importAttendance(@RequestParam("file") MultipartFile file) {
        try {
            return Result.success(attendanceService.importAttendance(file));
        } catch (Exception e) {
            return Result.fail("导入失败：" + e.getMessage());
        }
    }

    @GetMapping("/statistics/course")
    public Result<Map<String, Object>> statisticsByCourse(
            @RequestParam(required = false) String courseId,
            @RequestParam(required = false) LocalDate startDate,
            @RequestParam(required = false) LocalDate endDate) {
        return Result.success(attendanceService.statisticsByCourse(courseId, startDate, endDate));
    }

    @GetMapping("/statistics/date")
    public Result<List<Map<String, Object>>> statisticsByDate(
            @RequestParam(required = false) String courseId,
            @RequestParam(required = false) LocalDate startDate,
            @RequestParam(required = false) LocalDate endDate) {
        return Result.success(attendanceService.statisticsByDate(courseId, startDate, endDate));
    }
}