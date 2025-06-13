package com.example.keshe.entity;

import com.example.keshe.response.MDashResponse;

import java.util.List;
import java.util.Date;


public class MDash {
        private MDashResponse.ShopInfo shopInfo;
        private MDashResponse.ShopStats shopStats;
        private List<MDashResponse.ShopProducts> shopProducts;

        // Getters and Setters

        public static class ShopInfo { // 修改为 public
                private String shopId;
                private String name;
                private String address;
                private String category;
                private String establishedDate;
                // 其他字段、getter/setter

                // getters and setters
                public String getShopId() {
                        return shopId;
                }

                public void setShopId(String shopId) {
                        this.shopId = shopId;
                }

                public String getName() {
                        return name;
                }

                public void setName(String name) {
                        this.name = name;
                }

                public String getAddress() {
                        return address;
                }

                public void setAddress(String address) {
                        this.address = address;
                }
                public String getCategory() {
                        return category;
                }
                public void setCategory(String category) {
                        this.category = category;
                }
                public String getEstablishedDate() {
                        return establishedDate;
                }
                public void setEstablishedDate(String establishedDate) {
                        this.establishedDate = establishedDate;
                }
        }

        public static class ShopStats { // 修改为 public
                private int totalOrders;
                private double totalIncome;

                public int getTotalOrders() {
                        return totalOrders;
                }

                public void setTotalOrders(int totalOrders) {
                        this.totalOrders = totalOrders;
                }

                public double getTotalIncome() {
                        return totalIncome;
                }

                public void setTotalIncome(double totalIncome) {
                        this.totalIncome = totalIncome;
                }
        }

        public static class ShopProducts { // 修改为 public
                private String id;
                private String imageUrl;
                private double name;
                private double price;
                private int sold;
                private int stock;

                public String getId() {
                        return id;
                }

                public void setId(String id) {
                        this.id = id;
                }

                public String getImageUrl() {
                        return imageUrl;
                }

                public void setImageUrl(String imageUrl) {
                        this.imageUrl = imageUrl;
                }

                public double getName() {
                        return name;
                }

                public void setName(double name) {
                        this.name = name;
                }

                public double getPrice() {
                        return price;
                }

                public void setPrice(double price) {
                        this.price = price;
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
        }

        // Getter and Setter methods for the response fields
        public MDashResponse.ShopInfo getShopInfo() {
                return shopInfo;
        }

        public void setShopInfo(MDashResponse.ShopInfo shopInfo) {
                this.shopInfo = shopInfo;
        }

        public MDashResponse.ShopStats getShopStats() {
                return shopStats;
        }

        public void setShopStats(MDashResponse.ShopStats shopStats) {
                this.shopStats = shopStats;
        }

        public List<MDashResponse.ShopProducts> getShopProducts() {
                return shopProducts;
        }

        public void setShopProducts(List<MDashResponse.ShopProducts> shopProducts) {
                this.shopProducts = shopProducts;
        }
}
