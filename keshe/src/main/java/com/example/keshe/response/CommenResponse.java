package com.example.keshe.response;

public class CommenResponse {
    private int code;
     private String message;

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
    //成功的响应
    public static CommenResponse success() {
         CommenResponse response = new CommenResponse();
          response.setCode(200);
          response.setMessage("成功");
           return response;
    }
    // 失败的响应
     public static CommenResponse error(String message) {
         CommenResponse response = new CommenResponse();
         response.setCode(500);
          response.setMessage(message);
           return response;
     }
}
