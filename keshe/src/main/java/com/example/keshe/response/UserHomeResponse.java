package com.example.keshe.response;

import java.util.List;

public class UserHomeResponse {

    private int code;
     private String message;
    private List<HotProduct> data;

    public static class HotProduct {
         private String product_id;
          private String shop_id;
          private int sold;
          private int stock;
           private double income;
           private double price;
           private String imageUrl;
           private String product_name;
            private String category;

        public String getProduct_id() {
            return product_id;
        }

        public void setProduct_id(String product_id) {
            this.product_id = product_id;
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

        public int getSold() {
            return sold;
        }

        public void setSold(int sold) {
            this.sold = sold;
        }

        public int getStock() {
            return stock;
        }

        public void setStock(int stock) {
            this.stock = stock;
        }

        public double getIncome() {
            return income;
        }

        public void setIncome(double income) {
            this.income = income;
        }

        public double getPrice() {
            return price;
        }

        public void setPrice(double price) {
            this.price = price;
        }

        public String getImageUrl() {
            return imageUrl;
        }

        public void setImageUrl(String imageUrl) {
            this.imageUrl = imageUrl;
        }



        public String getCategory() {
            return category;
        }

        public void setCategory(String category) {
            this.category = category;
        }
    }
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

    public List<HotProduct> getData() {
        return data;
    }

    public void setData(List<HotProduct> data) {
        this.data = data;
    }

    //成功的响应
     public static UserHomeResponse success(String message,List<HotProduct> hotProducts) {
         UserHomeResponse response = new UserHomeResponse();
         response.setCode(200);
         response.setMessage("成功");
         response.setData(hotProducts);
          return response;
     }
     // 失败的响应
      public static UserHomeResponse error(String message) {
         UserHomeResponse response = new UserHomeResponse();
          response.setCode(500);
           response.setMessage(message);
            return response;

      }
}
