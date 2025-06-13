package com.example.keshe.response;
public class MDashStockResponse {
    private int code;
    private String message;

    // 成功时的响应（无 data）
    public static MDashStockResponse success() {
        MDashStockResponse response = new MDashStockResponse();
        response.setCode(200);
        response.setMessage("库存更新成功");
        return response;
    }

    // 失败时的响应
    public static MDashStockResponse error(String message) {
        MDashStockResponse response = new MDashStockResponse();
        response.setCode(500); // 或者使用其他自定义错误码
        response.setMessage(message);
        return response;
    }

    // Getter and Setter
    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
