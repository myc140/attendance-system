package com.example.attendance.service;

import  com.example.attendance.Result;
import com.example.attendance.User;
import java.util.List;

public interface UserService {
    // 新增用户
    int addUser(User user);
    // 根据ID查询用户
    User getUserById(Integer id);
    // 根据用户名查询用户（登录用）
    User getUserByUsername(String username);
    // 查询所有教师用户
    List<User> getAllTeachers();
    // 更新用户信息
    int updateUser(User user);
    // 根据ID删除用户
    int deleteUserById(Integer id);
    // 新增 登录、注册
    Result<?> login(String username, String password);
    Result<?> register(User user);
}