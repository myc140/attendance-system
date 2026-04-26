package com.example.attendance.controller;

import com.example.attendance.User;
import com.example.attendance.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 用户管理的控制器
 * 提供对外的 RESTful API 接口
 */
@RestController // 标记为控制器，且返回JSON数据
@RequestMapping("/user") // 统一前缀：所有接口以 /user 开头
public class UserController {

    // 注入业务层接口
    @Autowired
    private UserService userService;

    /**
     * 1. 新增用户
     * 接口地址：POST http://localhost:8080/user/add
     */
    @PostMapping("/add")
    public String addUser(@RequestBody User user) {
        int result = userService.addUser(user);
        return result > 0 ? "新增用户成功！" : "新增用户失败！";
    }

    /**
     * 2. 根据ID查询用户
     * 接口地址：GET http://localhost:8080/user/get/1
     */
    @GetMapping("/get/{id}")
    public User getUserById(@PathVariable Integer id) {
        return userService.getUserById(id);
    }

    /**
     * 3. 根据用户名查询（登录接口）
     * 接口地址：GET http://localhost:8080/user/getByUsername/test001
     */
    @GetMapping("/getByUsername/{username}")
    public User getUserByUsername(@PathVariable String username) {
        return userService.getUserByUsername(username);
    }

    /**
     * 4. 查询所有教师
     * 接口地址：GET http://localhost:8080/user/teachers
     */
    @GetMapping("/teachers")
    public List<User> getAllTeachers() {
        return userService.getAllTeachers();
    }

    /**
     * 5. 更新用户
     * 接口地址：PUT http://localhost:8080/user/update
     */
    @PutMapping("/update")
    public String updateUser(@RequestBody User user) {
        int result = userService.updateUser(user);
        return result > 0 ? "更新用户成功！" : "更新用户失败！";
    }

    /**
     * 6. 删除用户
     * 接口地址：DELETE http://localhost:8080/user/delete/1
     */
    @DeleteMapping("/delete/{id}")
    public String deleteUserById(@PathVariable Integer id) {
        int result = userService.deleteUserById(id);
        return result > 0 ? "删除用户成功！" : "删除用户失败！";
    }
}