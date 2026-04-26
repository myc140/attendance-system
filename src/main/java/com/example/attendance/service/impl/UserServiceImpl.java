package com.example.attendance.service.impl;

import com.example.attendance.User;
import com.example.attendance.dao.UserDao;
import com.example.attendance.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * UserService 接口的实现类
 * 调用 UserDao 完成数据库操作，实现业务逻辑
 */
@Service // 必须加这个注解，让Spring管理这个类
public class UserServiceImpl implements UserService {

    // 注入 UserDao（已经完成的DAO层）
    @Autowired
    private UserDao userDao;

    /**
     * 新增用户：直接调用 UserDao 的 insert 方法
     */
    @Override
    public int addUser(User user) {
        return userDao.insert(user);
    }

    /**
     * 根据ID查询用户：直接调用 UserDao 的 findById 方法
     */
    @Override
    public User getUserById(Integer id) {
        return userDao.findById(id);
    }

    /**
     * 根据用户名查询用户：直接调用 UserDao 的 findByUsername 方法
     */
    @Override
    public User getUserByUsername(String username) {
        return userDao.findByUsername(username);
    }

    /**
     * 查询所有教师用户：直接调用 UserDao 的 findAllTeachers 方法
     */
    @Override
    public List<User> getAllTeachers() {
        return userDao.findAllTeachers();
    }

    /**
     * 更新用户信息：直接调用 UserDao 的 update 方法
     */
    @Override
    public int updateUser(User user) {
        return userDao.update(user);
    }

    /**
     * 根据ID删除用户：直接调用 UserDao 的 deleteById 方法
     */
    @Override
    public int deleteUserById(Integer id) {
        return userDao.deleteById(id);
    }
}