package com.example.keshe.response;

public class AuthResponse {

    private int status;
    private String message;
    private Object pdata;

    public AuthResponse(int status, String message, Object pdata) {

        this.status = status;
        this.message = message;
        this.pdata = pdata;
    }

    // 成功响应
    public static AuthResponse success(Object pdata) {
        return new AuthResponse(200, "操作成功", pdata);
    }



    // 失败响应
    public static AuthResponse fail(int status, String message) {
        return new AuthResponse(status, message, null);
    }

    // Getter 方法

    public int getStatus() {
        return status;
    }

    public String getMessage() {
        return message;
    }

    public Object getPdata() {
        return pdata;
    }
}