package com.example.attendance.service.impl;

import com.example.attendance.Result;
import com.example.attendance.User;
import com.example.attendance.dao.UserDao;
import com.example.attendance.service.UserService;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class UserServiceImpl implements UserService {

    private final UserDao userDao;

    public UserServiceImpl(UserDao userDao) {
        this.userDao = userDao;
    }

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


    // 登录逻辑 明文比对 适配你的数据库
    @Override
    public Result<?> login(String username, String password) {
        User user = userDao.findByUsername(username);
        // 用户名不存在
        if(user == null){
            return Result.fail("用户不存在");
        }
        // 密码不对
        if(!password.equals(user.getPassword())){
            return Result.fail("用户名或密码错误");
        }
        // 登录成功
        return Result.success(user);
    }


    // 注册逻辑
    @Override
    public Result<?> register(User user) {
        // 用户名已存在
        if(userDao.existsByUsername(user.getUsername())){
            return Result.fail("用户名已被占用");
        }
        // 插入用户，角色固定TEACHER
        int rows = userDao.insert(user);
        if(rows > 0){
            return Result.success("注册成功");
        }else{
            return Result.fail("注册失败");
        }
    }
}