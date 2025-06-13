package com.example.keshe.response;

public class MDashDeleteResponse {
    private int code;
    private String message;

    public static MDashDeleteResponse success() {
        MDashDeleteResponse response = new MDashDeleteResponse();
        response.setCode(200);
        response.setMessage("商品下架成功");
        return response;
    }

    public static MDashDeleteResponse error(String message) {
        MDashDeleteResponse response = new MDashDeleteResponse();
        response.setCode(500);
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
