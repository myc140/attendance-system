package com.example.attendance.service;

import com.example.attendance.Attendance;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.data.domain.Page;
import java.time.LocalDate;
import java.time.LocalTime;

public interface AttendanceService {

    Page<Attendance> getAttendanceList(
            String studentId,
            LocalDate startDate,
            LocalDate endDate,
            String status,
            String courseId,
            int page,
            int size,
            String sortField,
            String sortDirection);

    String checkIn(String studentId, String courseId, LocalDate date, LocalTime checkTime);

    void exportExcel(String studentId, LocalDate startDate, LocalDate endDate,
                     String status, String courseId, HttpServletResponse response);
}