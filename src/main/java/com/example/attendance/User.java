package com.example.attendance;

import java.time.LocalDateTime;

/**
 * 完全适配你现有 user 表结构的实体类
 * 表字段：id, username, password, real_name, role, create_time
 */
public class User {
    private Integer id;
    private String username;
    private String password;
    private String realName;  // 对应表字段 real_name（下划线转驼峰）
    private String role;
    private LocalDateTime createTime;  // 对应表字段 create_time

    // 无参构造（JdbcTemplate 必须）
    public User() {}

    // 全参构造
    public User(Integer id, String username, String password, String realName, String role, LocalDateTime createTime) {
        this.id = id;
        this.username = username;
        this.password = password;
        this.realName = realName;
        this.role = role;
        this.createTime = createTime;
    }

    // 完整 Getter/Setter（BeanPropertyRowMapper 必须，少一个都报错）
    public Integer getId() { return id; }
    public void setId(Integer id) { this.id = id; }
    public String getUsername() { return username; }
    public void setUsername(String username) { this.username = username; }
    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = password; }
    public String getRealName() { return realName; }
    public void setRealName(String realName) { this.realName = realName; }
    public String getRole() { return role; }
    public void setRole(String role) { this.role = role; }
    public LocalDateTime getCreateTime() { return createTime; }
    public void setCreateTime(LocalDateTime createTime) { this.createTime = createTime; }
}