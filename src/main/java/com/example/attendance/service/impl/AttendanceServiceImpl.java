package com.example.attendance.service.impl;

import com.alibaba.excel.EasyExcel;
import com.alibaba.excel.context.AnalysisContext;
import com.alibaba.excel.event.AnalysisEventListener;
import com.alibaba.excel.write.style.column.LongestMatchColumnWidthStyleStrategy;
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

    // 构造注入 Repository
    public AttendanceServiceImpl(AttendanceRepository attendanceRepository) {
        this.attendanceRepository = attendanceRepository;
    }

    @Override
    public Page<Attendance> list(String studentId, String courseId, LocalDate startDate, LocalDate endDate, String status, int page, int size, String sortField, String sortDir) {
        // 动态条件查询
        Specification<Attendance> spec = (root, query, cb) -> {
            List<jakarta.persistence.criteria.Predicate> predicates = new ArrayList<>();
            if (studentId != null && !studentId.isBlank()) {
                predicates.add(cb.equal(root.get("studentId"), studentId));
            }
            if (courseId != null && !courseId.isBlank()) {
                predicates.add(cb.equal(root.get("courseId"), courseId));
            }
            if (startDate != null) {
                predicates.add(cb.greaterThanOrEqualTo(root.get("checkInTime"), startDate.atStartOfDay()));
            }
            if (endDate != null) {
                predicates.add(cb.lessThanOrEqualTo(root.get("checkInTime"), endDate.atTime(23, 59, 59)));
            }
            if (status != null && !status.isBlank()) {
                predicates.add(cb.equal(root.get("status"), status));
            }
            return cb.and(predicates.toArray(new jakarta.persistence.criteria.Predicate[0]));
        };

        // 分页+排序
        Sort sort = Sort.by(Sort.Direction.fromString(sortDir), sortField);
        PageRequest pageRequest = PageRequest.of(page, size, sort);
        return attendanceRepository.findAll(spec, pageRequest);
    }

    @Override
    public byte[] exportAttendanceExcel() {
        List<Attendance> records = attendanceRepository.findAll();
        try (java.io.ByteArrayOutputStream out = new java.io.ByteArrayOutputStream()) {
            EasyExcel.write(out, Attendance.class)
                    .registerWriteHandler(new LongestMatchColumnWidthStyleStrategy())
                    .sheet("attendance")
                    .doWrite(records);
            return out.toByteArray();
        } catch (Exception e) {
            throw new RuntimeException("导出 Excel 失败", e);
        }
    }

    @Override
    public String checkIn(String studentId, String courseId, LocalDate date, LocalTime checkTime) {
        String status = computeStatus(checkTime);
        Attendance attendance = new Attendance();
        attendance.setStudentId(studentId);
        attendance.setCourseId(courseId);
        attendance.setCheckInTime(date.atTime(checkTime));
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
                if (a.getCheckTime() != null) {
                    a.setCheckInTime(a.getAttendanceDate().atTime(a.getCheckTime()));
                }
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
            if (startDate != null) predicates.add(cb.greaterThanOrEqualTo(root.get("checkInTime"), startDate.atStartOfDay()));
            if (endDate != null) predicates.add(cb.lessThanOrEqualTo(root.get("checkInTime"), endDate.atTime(23, 59, 59)));
            return cb.and(predicates.toArray(new jakarta.persistence.criteria.Predicate[0]));
        });

        long normal = list.stream().filter(a -> "NORMAL".equals(a.getStatus())).count();
        long late = list.stream().filter(a -> "LATE".equals(a.getStatus())).count();
        long absent = list.stream().filter(a -> "ABSENT".equals(a.getStatus())).count();
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
            if (startDate != null) predicates.add(cb.greaterThanOrEqualTo(root.get("checkInTime"), startDate.atStartOfDay()));
            if (endDate != null) predicates.add(cb.lessThanOrEqualTo(root.get("checkInTime"), endDate.atTime(23, 59, 59)));
            return cb.and(predicates.toArray(new jakarta.persistence.criteria.Predicate[0]));
        });

        Map<LocalDate, Long> countMap = list.stream()
                .collect(Collectors.groupingBy(a -> a.getCheckInTime() != null ? a.getCheckInTime().toLocalDate() : null, Collectors.counting()));

        List<Map<String, Object>> result = new ArrayList<>();
        for (LocalDate d = startDate; !d.isAfter(endDate); d = d.plusDays(1)) {
            Map<String, Object> item = new HashMap<>();
            item.put("date", d);
            item.put("count", countMap.getOrDefault(d, 0L));
            result.add(item);
        }
        return result;
    }

    // 辅助方法：根据打卡时间判断状态
    private String computeStatus(LocalTime t) {
        if (t == null) return "ABSENT";
        if (t.isBefore(START_TIME)) return "NORMAL";
        if (t.isBefore(END_TIME)) return "LATE";
        return "EARLY";
    }
}