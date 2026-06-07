package com.example.attendance.dao;

import com.example.attendance.Student;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Repository
public class StudentDao {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    public void insertStudent(Student student) {
        String sql = "INSERT INTO student(name, student_id, class_name, gender, birth_date, phone) VALUES (?, ?, ?, ?, ?, ?)";
        jdbcTemplate.update(sql,
                student.getName(),
                student.getStudentId(),
                student.getClassName(),
                student.getGender(),
                student.getBirthDate(),
                student.getPhone()
        );
    }

    public List<Student> findAllStudents() {
        String sql = "SELECT name, student_id, class_name, gender, birth_date, phone FROM student";
        return jdbcTemplate.query(sql, new StudentRowMapper());
    }

    public Student findStudentById(String studentId) {
        String sql = "SELECT name, student_id, class_name, gender, birth_date, phone FROM student WHERE student_id = ?";
        try {
            return jdbcTemplate.queryForObject(sql, new StudentRowMapper(), studentId);
        } catch (Exception e) {
            return null;
        }
    }

    public void updateStudent(Student student) {
        String sql = "UPDATE student SET name=?, class_name=?, gender=?, birth_date=?, phone=? WHERE student_id=?";
        jdbcTemplate.update(sql,
                student.getName(),
                student.getClassName(),
                student.getGender(),
                student.getBirthDate(),
                student.getPhone(),
                student.getStudentId()
        );
    }

    public void deleteStudent(String studentId) {
        String sql = "DELETE FROM student WHERE student_id = ?";
        jdbcTemplate.update(sql, studentId);
    }

    // 修复：空指针安全的日期映射
    private static class StudentRowMapper implements RowMapper<Student> {
        @Override
        public Student mapRow(ResultSet rs, int rowNum) throws SQLException {
            Student s = new Student();
            s.setName(rs.getString("name"));
            s.setStudentId(rs.getString("student_id"));
            s.setClassName(rs.getString("class_name"));
            s.setGender(rs.getString("gender"));

            // 关键修复：判断日期是否为null
            if (rs.getDate("birth_date") != null) {
                s.setBirthDate(rs.getDate("birth_date").toLocalDate());
            } else {
                s.setBirthDate(null);
            }

            s.setPhone(rs.getString("phone"));
            return s;
        }
    }

    // 修复：删除了错误的SQL语句
    public List<Student> searchStudents(String keyword, String sortField, String sortOrder) {
        StringBuilder sql = new StringBuilder(
                "SELECT name, student_id, class_name, gender, birth_date, phone FROM student WHERE 1=1 "
        );
        List<Object> params = new ArrayList<>();

        if (keyword != null && !keyword.trim().isEmpty()) {
            sql.append(" AND (name LIKE ? OR student_id LIKE ?)");
            params.add("%" + keyword + "%");
            params.add("%" + keyword + "%");
        }

        if (sortField != null && List.of("student_id", "name").contains(sortField)) {
            sql.append(" ORDER BY ").append(sortField);
            if ("desc".equalsIgnoreCase(sortOrder)) {
                sql.append(" DESC");
            } else {
                sql.append(" ASC");
            }
        }

        return jdbcTemplate.query(sql.toString(), new StudentRowMapper(), params.toArray());
    }

    public void batchDeleteStudents(List<String> studentIds) {
        String placeholders = String.join(",", Collections.nCopies(studentIds.size(), "?"));
        String sql = "DELETE FROM student WHERE student_id IN (" + placeholders + ")";
        jdbcTemplate.update(sql, studentIds.toArray());
    }
}