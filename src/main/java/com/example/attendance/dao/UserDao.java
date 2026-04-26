package com.example.attendance.dao;

import com.example.attendance.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class UserDao {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    /**
     * 新增教师用户（固定 role 为 TEACHER，create_time 自动设为当前时间）
     */
    public int insert(User user) {
        String sql = "INSERT INTO user(username, password, real_name, role, create_time) VALUES(?, ?, ?, ?, NOW())";
        return jdbcTemplate.update(
                sql,
                user.getUsername(),
                user.getPassword(),
                user.getRealName(),
                "TEACHER"
        );
    }

    /**
     * 根据ID查询用户
     */
    public User findById(Integer id) {
        String sql = "SELECT * FROM user WHERE id = ?";
        return jdbcTemplate.queryForObject(sql, new BeanPropertyRowMapper<>(User.class), id);
    }

    /**
     * 根据用户名查询（登录验证用）
     */
    public User findByUsername(String username) {
        String sql = "SELECT * FROM user WHERE username = ?";
        List<User> list = jdbcTemplate.query(sql, new BeanPropertyRowMapper<>(User.class), username);
        return list.isEmpty() ? null : list.get(0);
    }

    /**
     * 查询所有教师用户
     */
    public List<User> findAllTeachers() {
        String sql = "SELECT * FROM user WHERE role = 'TEACHER'";
        return jdbcTemplate.query(sql, new BeanPropertyRowMapper<>(User.class));
    }

    /**
     * 更新用户信息
     */
    public int update(User user) {
        String sql = "UPDATE user SET username=?, password=?, real_name=?, role=? WHERE id=?";
        return jdbcTemplate.update(
                sql,
                user.getUsername(),
                user.getPassword(),
                user.getRealName(),
                user.getRole(),
                user.getId()
        );
    }

    /**
     * 根据ID删除用户
     */
    public int deleteById(Integer id) {
        String sql = "DELETE FROM user WHERE id = ?";
        return jdbcTemplate.update(sql, id);
    }
}