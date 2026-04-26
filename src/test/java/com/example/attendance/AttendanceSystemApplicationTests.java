package com.example.attendance;

import com.example.attendance.dao.UserDao;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class AttendanceSystemApplicationTests {

    @Autowired
    private UserDao userDao;

    @Test
    public void testUserDao() {
        // 1. 新建一个教师用户（create_time 由SQL的NOW()自动生成，不用传）
        User teacher = new User(
                null,
                "teacher_wang",
                "123456",
                "王老师",
                "TEACHER",
                null
        );

        // 2. 测试新增
        int insertResult = userDao.insert(teacher);
        System.out.println("新增成功，影响行数：" + insertResult);

        // 3. 测试按用户名查询（模拟登录）
        User loginUser = userDao.findByUsername("teacher_wang");
        System.out.println("登录查询成功：" + loginUser);

        // 4. 测试查询所有教师
        System.out.println("所有教师列表：" + userDao.findAllTeachers());
    }
}