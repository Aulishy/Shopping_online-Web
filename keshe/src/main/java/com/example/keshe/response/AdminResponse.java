package com.example.keshe.response;

import java.util.List;

public class AdminResponse {
    private String code;
    private String message;
    private List<ProductAudit> data;

    public static class ProductAudit {
        //PAudit_id
        //shop_id
        //product_name
        //price
        //stock
        //category
        //description
        //image_url
        //status
        //submit_time
        private String PAudit_id;
        private String shop_id;
        private String product_name;
        private String price;
        private String stock;
        private String category;
        private String description;
        private String image_url;
        private String submit_time;

        public String getPAudit_id() {
            return PAudit_id;
        }

        public void setPAudit_id(String PAudit_id) {
            this.PAudit_id = PAudit_id;
        }

        public String getShop_id() {
            return shop_id;
        }

        public void setShop_id(String shop_id) {
            this.shop_id = shop_id;
        }

        public String getProduct_name() {
            return product_name;
        }

        public void setProduct_name(String product_name) {
            this.product_name = product_name;
        }

        public String getPrice() {
            return price;
        }

        public void setPrice(String price) {
            this.price = price;
        }

        public String getStock() {
            return stock;
        }

        public void setStock(String stock) {
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

        public String getSubmit_time() {
            return submit_time;
        }

        public void setSubmit_time(String submit_time) {
            this.submit_time = submit_time;
        }
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public List<ProductAudit> getData() {
        return data;
    }

    public void setData(List<ProductAudit> data) {
        this.data = data;
    }

    // 成功响应
    public static AdminResponse success(List<ProductAudit> data) {
        AdminResponse response = new AdminResponse();
        response.setCode("200");
        response.setMessage("成功");
        response.setData(data);
        return response;
    }

    // 失败响应
    public static AdminResponse error(String message) {
        AdminResponse response = new AdminResponse();
        response.setCode("500");
        response.setMessage(message);
        return response;
    }
}
