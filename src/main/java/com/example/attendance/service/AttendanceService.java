package com.example.attendance.service;

import com.example.attendance.Attendance;
import org.springframework.data.domain.Page;
import java.time.LocalDate;

public interface AttendanceService {
    Page<Attendance> getAttendanceList(
            String studentId,
            LocalDate startDate,
            LocalDate endDate,
            String status,
            int page,
            int size,
            String sortField,
            String sortDirection);
}