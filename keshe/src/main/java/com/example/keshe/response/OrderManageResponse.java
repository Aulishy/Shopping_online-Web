package com.example.keshe.response;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;


public class OrderManageResponse {
    // 响应状态码
    private int code;
    // 响应消息
    private String message;

    // 订单数据列表
    private List<OrderData> data;

    // 内部类，用于存储订单数据
    public static class OrderData {
        // 订单ID
        private String orderId;
        // 创建时间
        private Date createTime;
        // 总金额
        private BigDecimal totalAmount;
        // 订单状态（英文）
        private String status;
        // 订单状态文本（中文）
        private String statusText;
        // 用户ID
        private Integer userId;
        // 收货人姓名
        private String receiverName;
        // 收货人电话
        private String receiverPhone;
        // 收货地址
        private String receiverAddress;
        // 商品名称
        private String productName;
        // 商品ID
        private String productId;
        // 购买数量
        private Integer productBuy;
        // 购买信息
        private String buyInfo;
        // 商品图片URL
        private String productImageUrl;

        private Long shopId;  // 添加这一行

        private Date paymentTime;

        private Date shipTime;

        // 修改字段名拼写错误
        private Date receiveTime;

        // Getter 和 Setter
        public Date getReceiveTime() {
            return receiveTime;
        }

        public void setReceiveTime(Date receiveTime) {
            this.receiveTime = receiveTime;
        }

        public Date getShipTime() {
            return shipTime;
        }

        public void setShipTime(Date shipTime) {
            this.shipTime = shipTime;
        }

        // 在 OrderData 类中添加如下内容：
        private String logisticsCompany;

        public String getLogisticsCompany() {
            return logisticsCompany;
        }

        public void setLogisticsCompany(String logisticsCompany) {
            this.logisticsCompany = logisticsCompany;
        }


        public Date getPaymentTime() {
            return paymentTime;
        }

        public void setPaymentTime(Date paymentTime) {
            this.paymentTime = paymentTime;
        }

        // Getter 和 Setter
        public Long getShopId() {
            return shopId;
        }

        public void setShopId(Long shopId) {
            this.shopId = shopId;
        }

        // Getters and Setters
        public String getOrderId() {
            return orderId;
        }

        public void setOrderId(String orderId) {
            this.orderId = orderId;
        }

        public Date getCreateTime() {
            return createTime;
        }

        public void setCreateTime(Date createTime) {
            this.createTime = createTime;
        }

        public BigDecimal getTotalAmount() {
            return totalAmount;
        }

        public void setTotalAmount(BigDecimal totalAmount) {
            this.totalAmount = totalAmount;
        }

        public String getStatus() {
            return status;
        }

        public void setStatus(String status) {
            this.status = status;
        }

        public String getStatusText() {
            return statusText;
        }

        public void setStatusText(String statusText) {
            this.statusText = statusText;
        }

        public Integer getUserId() {
            return userId;
        }

        public void setUserId(Integer userId) {
            this.userId = userId;
        }

        public String getReceiverName() {
            return receiverName;
        }

        public void setReceiverName(String receiverName) {
            this.receiverName = receiverName;
        }

        public String getReceiverPhone() {
            return receiverPhone;
        }

        public void setReceiverPhone(String receiverPhone) {
            this.receiverPhone = receiverPhone;
        }

        public String getReceiverAddress() {
            return receiverAddress;
        }

        public void setReceiverAddress(String receiverAddress) {
            this.receiverAddress = receiverAddress;
        }

        public String getProductName() {
            return productName;
        }

        public void setProductName(String productName) {
            this.productName = productName;
        }

        public String getProductId() {
            return productId;
        }

        public void setProductId(String productId) {
            this.productId = productId;
        }

        public Integer getProductBuy() {
            return productBuy;
        }

        public void setProductBuy(Integer productBuy) {
            this.productBuy = productBuy;
        }

        public String getBuyInfo() {
            return buyInfo;
        }

        public void setBuyInfo(String buyInfo) {
            this.buyInfo = buyInfo;
        }

        public String getProductImageUrl() {
            return productImageUrl;
        }

        public void setProductImageUrl(String productImageUrl) {
            this.productImageUrl = productImageUrl;
        }
    }

    // Getters and Setters
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

    public List<OrderData> getData() {
        return data;
    }

    public void setData(List<OrderData> data) {
        this.data = data;
    }
}