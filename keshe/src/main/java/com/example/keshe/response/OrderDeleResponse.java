// 修改或确认 OrderDeleResponse 类如下：
package com.example.keshe.response;

public class OrderDeleResponse {
    private int code;
    private String message;

    public static OrderDeleResponse success() {
        OrderDeleResponse response = new OrderDeleResponse();
        response.setCode(200);
        response.setMessage("订单删除成功");
        return response;
    }
    //失败 时的响应
     public static OrderDeleResponse error(String message) {
         OrderDeleResponse response = new OrderDeleResponse();
          response.setCode(500);
           response.setMessage(message);
            return response;
     }

    // Getter 和 Setter
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
