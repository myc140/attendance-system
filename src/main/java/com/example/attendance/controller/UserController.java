package com.example.attendance.controller;

import com.example.attendance.Result;
import com.example.attendance.User;
import com.example.attendance.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/user")
public class UserController {

    @Autowired
    private UserService userService;

    @Autowired
    private PasswordEncoder passwordEncoder;

    // ---------------------- 你原来的API接口（完全保留） ----------------------
    @PostMapping("/add")
    @ResponseBody
    public String addUser(@RequestBody User user) {
        int result = userService.addUser(user);
        return result > 0 ? "新增用户成功！" : "新增用户失败！";
    }

    @GetMapping("/get/{id}")
    @ResponseBody
    public User getUserById(@PathVariable Integer id) {
        return userService.getUserById(id);
    }

    @GetMapping("/getByUsername/{username}")
    @ResponseBody
    public User getUserByUsername(@PathVariable String username) {
        return userService.getUserByUsername(username);
    }

    @GetMapping("/teachers")
    @ResponseBody
    public List<User> getAllTeachers() {
        return userService.getAllTeachers();
    }

    @PutMapping("/update")
    @ResponseBody
    public String updateUser(@RequestBody User user) {
        int result = userService.updateUser(user);
        return result > 0 ? "更新用户成功！" : "更新用户失败！";
    }

    @DeleteMapping("/delete/{id}")
    @ResponseBody
    public String deleteUserById(@PathVariable Integer id) {
        int result = userService.deleteUserById(id);
        return result > 0 ? "删除用户成功！" : "删除用户失败！";
    }

    @PostMapping("/login")
    @ResponseBody
    public ResponseEntity<?> login(@RequestBody Map<String, String> request) {
        String username = request.get("username");
        String password = request.get("password");
        Result<?> result = userService.login(username, password);
        return result.isSuccess() ? ResponseEntity.ok(result) : ResponseEntity.badRequest().body(result);
    }

    @PostMapping("/register")
    @ResponseBody
    public ResponseEntity<?> register(@RequestBody User user) {
        Result<?> result = userService.register(user);
        return result.isSuccess() ? ResponseEntity.ok(result) : ResponseEntity.badRequest().body(result);
    }

    // ---------------------- 页面跳转接口（修复版） ----------------------
    @GetMapping("/register-page")
    public String registerPage() {
        return "register";
    }

    @PostMapping("/register-page")
    public String registerFromPage(
            @RequestParam String username,
            @RequestParam String password,
            @RequestParam String confirmPassword,
            Model model) {

        // 校验1：两次密码是否一致
        if (!password.equals(confirmPassword)) {
            model.addAttribute("error", "两次输入的密码不一致");
            return "register";
        }

        // 校验2：用户名是否已存在
        if (userService.getUserByUsername(username) != null) {
            model.addAttribute("error", "用户名已被占用");
            return "register";
        }

        // 关键修复：在这里初始化 User 对象
        User user = new User();
        user.setUsername(username);
        user.setPassword(passwordEncoder.encode(password));
        user.setRole("ROLE_USER");

        Result<?> result = userService.register(user);

        if (result.isSuccess()) {
            model.addAttribute("success", "注册成功！请登录");
            return "redirect:/user/login-page";
        } else {
            model.addAttribute("error", result.getMessage());
            return "register";
        }
    }

    @GetMapping("/login-page")
    public String loginPage(Model model, @RequestParam(required = false) String error) {
        if (error != null) {
            model.addAttribute("error", "用户名或密码错误");
        }
        return "login";
    }

    @GetMapping("/index")
    public String indexPage() {
        return "index";
    }
}