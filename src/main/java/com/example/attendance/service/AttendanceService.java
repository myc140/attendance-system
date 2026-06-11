package com.example.attendance.service;

import com.example.attendance.Attendance;
import org.springframework.data.domain.Page;
import org.springframework.web.multipart.MultipartFile;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.Map;

public interface AttendanceService {
    Page<Attendance> list(String studentId, String courseId, LocalDate startDate, LocalDate endDate, String status, int page, int size, String sortField, String sortDir);

    byte[] exportAttendanceExcel();

    String checkIn(String studentId, String courseId, LocalDate date, LocalTime checkTime);

    String importAttendance(MultipartFile file) throws Exception;

    Map<String, Object> statisticsByCourse(String courseId, LocalDate startDate, LocalDate endDate);

    List<Map<String, Object>> statisticsByDate(String courseId, LocalDate startDate, LocalDate endDate);
}