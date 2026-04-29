package com.example.attendance.controller;

import com.example.attendance.Result;
import com.example.attendance.User;
import com.example.attendance.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/user")
public class UserController {

    @Autowired
    private UserService userService;

    // 你原来的所有接口，完全保留
    @PostMapping("/add")
    public String addUser(@RequestBody User user) {
        int result = userService.addUser(user);
        return result > 0 ? "新增用户成功！" : "新增用户失败！";
    }

    @GetMapping("/get/{id}")
    public User getUserById(@PathVariable Integer id) {
        return userService.getUserById(id);
    }

    @GetMapping("/getByUsername/{username}")
    public User getUserByUsername(@PathVariable String username) {
        return userService.getUserByUsername(username);
    }

    @GetMapping("/teachers")
    public List<User> getAllTeachers() {
        return userService.getAllTeachers();
    }

    @PutMapping("/update")
    public String updateUser(@RequestBody User user) {
        int result = userService.updateUser(user);
        return result > 0 ? "更新用户成功！" : "更新用户失败！";
    }

    @DeleteMapping("/delete/{id}")
    public String deleteUserById(@PathVariable Integer id) {
        int result = userService.deleteUserById(id);
        return result > 0 ? "删除用户成功！" : "删除用户失败！";
    }

    // 新增登录接口
    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody Map<String, String> request) {
        String username = request.get("username");
        String password = request.get("password");
        Result<?> result = userService.login(username, password);
        return result.isSuccess() ? ResponseEntity.ok(result) : ResponseEntity.badRequest().body(result);
    }

    // 新增注册接口
    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody User user) {
        Result<?> result = userService.register(user);
        return result.isSuccess() ? ResponseEntity.ok(result) : ResponseEntity.badRequest().body(result);
    }
}