package com.example.attendance;

public class Result<T> {
    // 状态码：200 表示成功，其他表示失败
    private Integer code;
    // 提示信息
    private String message;
    // 泛型数据：返回的具体业务数据
    private T data;

    // 无参构造
    public Result() {}

    // 全参构造
    public Result(Integer code, String message, T data) {
        this.code = code;
        this.message = message;
        this.data = data;
    }

    // 快速创建成功响应的静态方法
    public static <T> Result<T> success(T data) {
        return new Result<>(200, "操作成功", data);
    }

    // 快速创建失败响应的静态方法
    public static <T> Result<T> fail(String message) {
        return new Result<>(500, message, null);
    }

    // Getter & Setter
    public Integer getCode() { return code; }
    public void setCode(Integer code) { this.code = code; }
    public String getMessage() { return message; }
    public void setMessage(String message) { this.message = message; }
    public T getData() { return data; }
    public void setData(T data) { this.data = data; }
}