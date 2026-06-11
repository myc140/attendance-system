package com.example.attendance.service.impl;

import com.alibaba.excel.EasyExcel;
import com.alibaba.excel.context.AnalysisContext;
import com.alibaba.excel.event.AnalysisEventListener;
import com.example.attendance.Attendance;
import com.example.attendance.dao.AttendanceRepository;
import com.example.attendance.service.AttendanceService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class AttendanceServiceImpl implements AttendanceService {

    private final AttendanceRepository attendanceRepository;
    private static final LocalTime START_TIME = LocalTime.of(8, 0);
    private static final LocalTime END_TIME = LocalTime.of(17, 0);

    public AttendanceServiceImpl(AttendanceRepository attendanceRepository) {
        this.attendanceRepository = attendanceRepository;
    }

    @Override
    public Page<Attendance> list(String studentId, String courseId, LocalDate startDate, LocalDate endDate, String status, int page, int size, String sortField, String sortDir) {
        Specification<Attendance> spec = (root, query, cb) -> {
            List<jakarta.persistence.criteria.Predicate> predicates = new ArrayList<>();
            if (studentId != null && !studentId.isBlank()) {
                predicates.add(cb.equal(root.get("studentId"), studentId));
            }
            if (courseId != null && !courseId.isBlank()) {
                predicates.add(cb.equal(root.get("courseId"), courseId));
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
            return cb.and(predicates.toArray(new jakarta.persistence.criteria.Predicate[0]));
        };
        Sort sort = Sort.by(Sort.Direction.fromString(sortDir), sortField);
        PageRequest pageRequest = PageRequest.of(page, size, sort);
        return attendanceRepository.findAll(spec, pageRequest);
    }

    @Override
    public String checkIn(String studentId, String courseId, LocalDate date, LocalTime checkTime) {
        String status = computeStatus(checkTime);
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
    public String importAttendance(MultipartFile file) throws Exception {
        List<Attendance> list = new ArrayList<>();
        EasyExcel.read(file.getInputStream(), Attendance.class, new AnalysisEventListener<Attendance>() {
            @Override
            public void invoke(Attendance a, AnalysisContext context) {
                a.setStatus(computeStatus(a.getCheckTime()));
                list.add(a);
            }

            @Override
            public void doAfterAllAnalysed(AnalysisContext context) {}
        }).sheet().doRead();
        attendanceRepository.saveAll(list);
        return "导入成功，共 " + list.size() + " 条数据";
    }

    @Override
    public Map<String, Object> statisticsByCourse(String courseId, LocalDate startDate, LocalDate endDate) {
        List<Attendance> list = attendanceRepository.findAll((root, query, cb) -> {
            List<jakarta.persistence.criteria.Predicate> predicates = new ArrayList<>();
            if (courseId != null && !courseId.isBlank()) predicates.add(cb.equal(root.get("courseId"), courseId));
            if (startDate != null) predicates.add(cb.greaterThanOrEqualTo(root.get("attendanceDate"), startDate));
            if (endDate != null) predicates.add(cb.lessThanOrEqualTo(root.get("attendanceDate"), endDate));
            return cb.and(predicates.toArray(new jakarta.persistence.criteria.Predicate[0]));
        });

        long normal = list.stream().filter(a -> "正常".equals(a.getStatus())).count();
        long late = list.stream().filter(a -> "迟到".equals(a.getStatus())).count();
        long absent = list.stream().filter(a -> "缺勤".equals(a.getStatus())).count();
        double rate = list.isEmpty() ? 0 : (normal + late) * 100.0 / list.size();

        Map<String, Object> map = new HashMap<>();
        map.put("courseId", courseId);
        map.put("normal", normal);
        map.put("late", late);
        map.put("absent", absent);
        map.put("attendanceRate", String.format("%.2f%%", rate));
        return map;
    }

    @Override
    public List<Map<String, Object>> statisticsByDate(String courseId, LocalDate startDate, LocalDate endDate) {
        List<Attendance> list = attendanceRepository.findAll((root, query, cb) -> {
            List<jakarta.persistence.criteria.Predicate> predicates = new ArrayList<>();
            if (courseId != null && !courseId.isBlank()) predicates.add(cb.equal(root.get("courseId"), courseId));
            if (startDate != null) predicates.add(cb.greaterThanOrEqualTo(root.get("attendanceDate"), startDate));
            if (endDate != null) predicates.add(cb.lessThanOrEqualTo(root.get("attendanceDate"), endDate));
            return cb.and(predicates.toArray(new jakarta.persistence.criteria.Predicate[0]));
        });

        Map<LocalDate, Long> countMap = list.stream()
                .collect(Collectors.groupingBy(Attendance::getAttendanceDate, Collectors.counting()));

        List<Map<String, Object>> result = new ArrayList<>();
        for (LocalDate d = startDate; !d.isAfter(endDate); d = d.plusDays(1)) {
            Map<String, Object> item = new HashMap<>();
            item.put("date", d);
            item.put("count", countMap.getOrDefault(d, 0L));
            result.add(item);
        }
        return result;
    }

    private String computeStatus(LocalTime t) {
        if (t == null) return "缺勤";
        if (t.isBefore(START_TIME)) return "正常";
        if (t.isBefore(END_TIME)) return "迟到";
        return "早退";
    }
}