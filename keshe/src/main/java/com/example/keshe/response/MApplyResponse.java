package com.example.keshe.response;

public class MApplyResponse {

    private int code;
    private String message;
    private Data data;

    // 成功响应的构造方法
    public static MApplyResponse success(String applyId, String applyTime) {
        MApplyResponse response = new MApplyResponse();
        response.code = 200;
        response.message = "商家入驻申请提交成功，等待审核";
        response.data = new Data(applyId, applyTime);
        return response;
    }

    // 失败响应的构造方法
    public static MApplyResponse error(int code, String message) {
        MApplyResponse response = new MApplyResponse();
        response.code = code;
        response.message = message;
        response.data = null;
        return response;
    }

    // Getter 和 Setter 方法
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

    public Data getData() {
        return data;
    }

    public void setData(Data data) {
        this.data = data;
    }

    // 内部类用于存储附加数据
    public static class Data {
        private String applyId;
        private String applyTime;

        public Data(String applyId, String applyTime) {
            this.applyId = applyId;
            this.applyTime = applyTime;
        }

        public String getApplyId() {
            return applyId;
        }

        public void setApplyId(String applyId) {
            this.applyId = applyId;
        }

        public String getApplyTime() {
            return applyTime;
        }

        public void setApplyTime(String applyTime) {
            this.applyTime = applyTime;
        }
    }
}
