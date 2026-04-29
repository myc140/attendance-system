package com.example.attendance.service.impl;

import com.example.attendance.Result;
import com.example.attendance.User;
import com.example.attendance.dao.UserDao;
import com.example.attendance.service.UserService;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import java.time.LocalDateTime;
import java.util.List;

@Service
public class UserServiceImpl implements UserService {

    private final UserDao userDao;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;

    // 构造注入，解决 AuthenticationManager 找不到的问题
    public UserServiceImpl(UserDao userDao,
                           PasswordEncoder passwordEncoder,
                           AuthenticationManager authenticationManager) {
        this.userDao = userDao;
        this.passwordEncoder = passwordEncoder;
        this.authenticationManager = authenticationManager;
    }

    // 你原来的方法，完全保留
    @Override
    public int addUser(User user) {
        return userDao.insert(user);
    }

    @Override
    public User getUserById(Integer id) {
        return userDao.findById(id);
    }

    @Override
    public User getUserByUsername(String username) {
        return userDao.findByUsername(username);
    }

    @Override
    public List<User> getAllTeachers() {
        return userDao.findAllTeachers();
    }

    @Override
    public int updateUser(User user) {
        return userDao.update(user);
    }

    @Override
    public int deleteUserById(Integer id) {
        return userDao.deleteById(id);
    }

    // 新增登录方法
    @Override
    public Result<?> login(String username, String password) {
        try {
            UsernamePasswordAuthenticationToken authToken =
                    new UsernamePasswordAuthenticationToken(username, password);
            Authentication authentication = authenticationManager.authenticate(authToken);
            SecurityContextHolder.getContext().setAuthentication(authentication);
            User user = (User) authentication.getPrincipal();
            return Result.success("登录成功，用户：" + user.getUsername() + "，角色：" + user.getRole());
        } catch (Exception e) {
            return Result.fail("登录失败：" + e.getMessage());
        }
    }

    // 新增注册方法（解决了空指针异常）
    @Override
    public Result<?> register(User user) {
        if (user == null || user.getUsername() == null || user.getPassword() == null) {
            return Result.fail("用户名或密码不能为空");
        }
        if (userDao.existsByUsername(user.getUsername())) {
            return Result.fail("用户名已存在");
        }
        // 密码加密
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        // 设置创建时间（兼容你原来的insert方法，这里也可以不设）
        if (user.getCreateTime() == null) {
            user.setCreateTime(LocalDateTime.now());
        }
        // 注册时默认角色为学生，和addUser的教师区分开
        if (user.getRole() == null) {
            user.setRole("STUDENT");
        }
        userDao.insert(user);
        return Result.success("注册成功");
    }
}