package com.example.attendance.dao;

import com.example.attendance.Student;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Repository
public class StudentDao {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    // ========== 你原来的 insertStudent 方法，只改了SQL语句，适配新字段 ==========
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

    // ========== 新增：查询所有学生 ==========
    public List<Student> findAllStudents() {
        String sql = "SELECT name, student_id, class_name, gender, birth_date, phone FROM student";
        return jdbcTemplate.query(sql, new StudentRowMapper());
    }

    // ========== 新增：按学号查询单个学生 ==========
    public Student findStudentById(String studentId) {
        String sql = "SELECT name, student_id, class_name, gender, birth_date, phone FROM student WHERE student_id = ?";
        try {
            return jdbcTemplate.queryForObject(sql, new StudentRowMapper(), studentId);
        } catch (Exception e) {
            return null;
        }
    }

    // ========== 新增：更新学生信息 ==========
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

    // ========== 新增：删除学生 ==========
    public void deleteStudent(String studentId) {
        String sql = "DELETE FROM student WHERE student_id = ?";
        jdbcTemplate.update(sql, studentId);
    }

    // ========== 新增：把数据库字段映射成 Student 对象 ==========
    private static class StudentRowMapper implements RowMapper<Student> {
        @Override
        public Student mapRow(ResultSet rs, int rowNum) throws SQLException {
            Student s = new Student();
            s.setName(rs.getString("name"));
            s.setStudentId(rs.getString("student_id"));
            s.setClassName(rs.getString("class_name"));
            s.setGender(rs.getString("gender"));
            s.setBirthDate(rs.getDate("birth_date").toLocalDate());
            s.setPhone(rs.getString("phone"));
            return s;
        }
    }
    // 1. 按姓名/学号搜索 + 排序学生
    public List<Student> searchStudents(String keyword, String sortField, String sortOrder) {
        StringBuilder sql = new StringBuilder(
                "SELECT name, student_id, class_name, gender, birth_date, phone FROM student WHERE 1=1 "
        );
        List<Object> params = new ArrayList<>();

        // 模糊搜索：姓名/学号包含keyword
        if (keyword != null && !keyword.trim().isEmpty()) {
            sql.append(" AND (name LIKE ? OR student_id LIKE ?)");
            params.add("%" + keyword + "%");
            params.add("%" + keyword + "%");
        }

        // 排序：仅允许按student_id或name排序，防止SQL注入
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

    // 2. 批量删除学生
    public void batchDeleteStudents(List<String> studentIds) {
        // 生成?占位符，比如3个id → ?,?,?
        String placeholders = String.join(",", Collections.nCopies(studentIds.size(), "?"));
        String sql = "DELETE FROM student WHERE student_id IN (" + placeholders + ")";
        jdbcTemplate.update(sql, studentIds.toArray());
    }
}