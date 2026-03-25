package com.example.attendance;

public class Student {
    private String name;
    private String studentId;
    private String className;
    // 无参构造（必须，Spring 序列化/反序列化需要）
    public Student() {}

    // 全参构造
    public Student(String name, String studentId, String className) {
        this.name = name;
        this.studentId = studentId;
        this.className = className;
    }

    // Getter & Setter
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public String getStudentId() { return studentId; }
    public void setStudentId(String studentId) { this.studentId = studentId; }
    public String getClassName() { return className; }
    public void setClassName(String className) { this.className = className; }
}