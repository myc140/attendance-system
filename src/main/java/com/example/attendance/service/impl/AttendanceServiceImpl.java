package com.example.attendance.service.impl;

import com.alibaba.excel.EasyExcel;
import com.example.attendance.Attendance;
import com.example.attendance.dao.AttendanceRepository;
import com.example.attendance.service.AttendanceService;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import java.io.IOException;
import java.net.URLEncoder;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

@Service
public class AttendanceServiceImpl implements AttendanceService {

    private final AttendanceRepository attendanceRepository;
    private static final LocalTime START_TIME = LocalTime.of(8, 0);
    private static final LocalTime END_TIME = LocalTime.of(17, 0);

    public AttendanceServiceImpl(AttendanceRepository attendanceRepository) {
        this.attendanceRepository = attendanceRepository;
    }

    @Override
    public Page<Attendance> getAttendanceList(
            String studentId,
            LocalDate startDate,
            LocalDate endDate,
            String status,
            String courseId,
            int page,
            int size,
            String sortField,
            String sortDirection) {

        Specification<Attendance> spec = (root, query, cb) -> {
            List<jakarta.persistence.criteria.Predicate> predicates = new ArrayList<>();

            if (studentId != null && !studentId.isBlank()) {
                predicates.add(cb.equal(root.get("studentId"), studentId));
            }
            if (startDate != null) {
                predicates.add(cb.greaterThanOrEqualTo(root.get("attendanceDate"), startDate));
            }
            if (endDate != null) {
                predicates.add(cb.lessThanOrEqualTo(root.get("attendanceDate"), endDate));
            }
            if (status != null && !status.isBlank()) {
                predicates.add(cb.equal(root.get("status"), status));
            }
            if (courseId != null && !courseId.isBlank()) {
                predicates.add(cb.equal(root.get("courseId"), courseId));
            }

            return cb.and(predicates.toArray(new jakarta.persistence.criteria.Predicate[0]));
        };

        Sort sort = Sort.by(Sort.Direction.fromString(sortDirection), sortField);
        PageRequest pageRequest = PageRequest.of(page, size, sort);
        return attendanceRepository.findAll(spec, pageRequest);
    }

    @Override
    public String checkIn(String studentId, String courseId, LocalDate date, LocalTime checkTime) {
        String status;
        if (checkTime.isBefore(START_TIME)) {
            status = "正常";
        } else if (checkTime.isBefore(END_TIME)) {
            status = "迟到";
        } else {
            status = "早退";
        }

        Attendance attendance = new Attendance();
        attendance.setStudentId(studentId);
        attendance.setCourseId(courseId);
        attendance.setAttendanceDate(date);
        attendance.setCheckTime(checkTime);
        attendance.setStatus(status);

        attendanceRepository.save(attendance);
        return "打卡成功：" + status;
    }

    @Override
    public void exportExcel(String studentId, LocalDate startDate, LocalDate endDate,
                            String status, String courseId, HttpServletResponse response) {
        try {
            List<Attendance> list = attendanceRepository.findAll((root, query, cb) -> {
                List<jakarta.persistence.criteria.Predicate> listP = new ArrayList<>();
                if (studentId != null) listP.add(cb.equal(root.get("studentId"), studentId));
                if (startDate != null) listP.add(cb.greaterThanOrEqualTo(root.get("attendanceDate"), startDate));
                if (endDate != null) listP.add(cb.lessThanOrEqualTo(root.get("attendanceDate"), endDate));
                if (status != null) listP.add(cb.equal(root.get("status"), status));
                if (courseId != null) listP.add(cb.equal(root.get("courseId"), courseId));
                return cb.and(listP.toArray(new jakarta.persistence.criteria.Predicate[0]));
            });

            response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
            response.setCharacterEncoding("utf-8");
            String fileName = URLEncoder.encode("考勤列表", "UTF-8").replace("+", "%20");
            response.setHeader("Content-Disposition", "attachment;filename=" + fileName + ".xlsx");
            EasyExcel.write(response.getOutputStream(), Attendance.class).sheet("考勤数据").doWrite(list);

        } catch (IOException e) {
            throw new RuntimeException("导出失败：" + e.getMessage());
        }
    }
}