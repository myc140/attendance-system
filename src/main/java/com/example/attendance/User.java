package com.example.attendance;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.Collections;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

/**
 * 适配 Spring Security 的 User 实体类
 * 表字段：id, username, password, real_name, role, create_time
 */
public class User implements UserDetails {
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

    // --- 实现 UserDetails 接口的方法 ---
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        // 把 role 字段包装成 Spring Security 能识别的权限对象
        return Collections.singletonList(new SimpleGrantedAuthority(role));
    }

    @Override
    public boolean isAccountNonExpired() {
        return true; // 账号未过期
    }

    @Override
    public boolean isAccountNonLocked() {
        return true; // 账号未锁定
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true; // 凭证未过期
    }

    @Override
    public boolean isEnabled() {
        return true; // 账号已启用
    }

    // --- Getter/Setter（完全保留你原来的） ---
    public Integer getId() { return id; }
    public void setId(Integer id) { this.id = id; }

    @Override
    public String getUsername() { return username; }
    public void setUsername(String username) { this.username = username; }

    @Override
    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = password; }

    public String getRealName() { return realName; }
    public void setRealName(String realName) { this.realName = realName; }

    public String getRole() { return role; }
    public void setRole(String role) { this.role = role; }

    public LocalDateTime getCreateTime() { return createTime; }
    public void setCreateTime(LocalDateTime createTime) { this.createTime = createTime; }
}