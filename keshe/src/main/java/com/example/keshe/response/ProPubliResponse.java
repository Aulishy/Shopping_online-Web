package com.example.keshe.response;

import java.util.Date;
import java.util.List;

public class ProPubliResponse {
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

    private List<ProPubliData> auditrecords;
    public static class ProPubliData {
        private Long PAudit_id;
        private Long shop_id;
        private String product_name;
        private double price;
        private int stock;
        private String category;
        private String description;
        private  String image_url;
        private String status;
        private Date submit_time;
        private  String reject_reason;

        public Long getPAudit_id() {
            return PAudit_id;
        }

        public void setPAudit_id(Long PAudit_id) {
            this.PAudit_id = PAudit_id;
        }

        public Long getShop_id() {
            return shop_id;
        }

        public void setShop_id(Long shop_id) {
            this.shop_id = shop_id;
        }

        public String getProduct_name() {
            return product_name;
        }

        public void setProduct_name(String product_name) {
            this.product_name = product_name;
        }

        public double getPrice() {
            return price;
        }

        public void setPrice(double price) {
            this.price = price;
        }

        public int getStock() {
            return stock;
        }

        public void setStock(int stock) {
            this.stock = stock;
        }

        public String getCategory() {
            return category;
        }

        public void setCategory(String category) {
            this.category = category;
        }

        public String getDescription() {
            return description;
        }

        public void setDescription(String description) {
            this.description = description;
        }

        public String getImage_url() {
            return image_url;
        }

        public void setImage_url(String image_url) {
            this.image_url = image_url;
        }

        public String getStatus() {
            return status;
        }

        public void setStatus(String status) {
            this.status = status;
        }

        public Date getSubmit_time() {
            return submit_time;
        }

        public void setSubmit_time(Date submit_time) {
            this.submit_time = submit_time;
        }

        public String getReject_reason() {
            return reject_reason;
        }

        public void setReject_reason(String reject_reason) {
            this.reject_reason = reject_reason;
        }
    }

    public List<ProPubliData> getAuditrecords() {
        return auditrecords;
    }
     public void setAuditrecords(List<ProPubliData> auditrecords) {
        this.auditrecords = auditrecords;
    }
     public static ProPubliResponse success(List<ProPubliData> auditrecords){
        ProPubliResponse response = new ProPubliResponse();
        response.setCode(200);
         response.setMessage("查询成功");
          response.setAuditrecords(auditrecords);
           return response;
     }

      public static ProPubliResponse error(int code, String message){
        ProPubliResponse response = new ProPubliResponse();
        response.setCode(code);
         response.setMessage(message);
          return response;
     }

}
