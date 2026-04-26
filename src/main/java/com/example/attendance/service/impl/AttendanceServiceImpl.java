package com.example.attendance.service.impl;

import com.example.attendance.Attendance;
import com.example.attendance.dao.AttendanceRepository;
import com.example.attendance.service.AttendanceService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import jakarta.persistence.criteria.Predicate;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Service
public class AttendanceServiceImpl implements AttendanceService {

    private final AttendanceRepository attendanceRepository;

    public AttendanceServiceImpl(AttendanceRepository attendanceRepository) {
        this.attendanceRepository = attendanceRepository;
    }

    @Override
    public Page<Attendance> getAttendanceList(
            String studentId,
            LocalDate startDate,
            LocalDate endDate,
            String status,
            int page,
            int size,
            String sortField,
            String sortDirection) {

        // 构建动态查询条件
        Specification<Attendance> spec = (root, query, cb) -> {
            List<Predicate> predicates = new ArrayList<>();

            // 学号条件（非空才生效）
            if (studentId != null && !studentId.isBlank()) {
                predicates.add(cb.equal(root.get("studentId"), studentId));
            }

            // 日期范围条件（非空才生效）
            if (startDate != null) {
                predicates.add(cb.greaterThanOrEqualTo(root.get("attendanceDate"), startDate));
            }
            if (endDate != null) {
                predicates.add(cb.lessThanOrEqualTo(root.get("attendanceDate"), endDate));
            }

            // 状态条件（非空才生效）
            if (status != null && !status.isBlank()) {
                predicates.add(cb.equal(root.get("status"), status));
            }

            return cb.and(predicates.toArray(new Predicate[0]));
        };

        // 构建排序
        Sort sort = "desc".equalsIgnoreCase(sortDirection)
                ? Sort.by(Sort.Direction.DESC, sortField)
                : Sort.by(Sort.Direction.ASC, sortField);

        // 构建分页请求
        PageRequest pageRequest = PageRequest.of(page, size, sort);

        // 执行查询
        return attendanceRepository.findAll(spec, pageRequest);
    }
}