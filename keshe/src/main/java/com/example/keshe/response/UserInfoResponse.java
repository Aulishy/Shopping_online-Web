package com.example.keshe.response;

import java.util.List;

public class UserInfoResponse {
    private int code;
    private String message;

    private List<Addresses> addresses;
    private List<Orders> orders;

    public static class Addresses {

        private int addressId;
        private String recipientName;
        private String shippingAddress;
        private String recipientPhone;

        public int getAddressId() {
            return addressId;
        }

        public void setAddressId(int address_id) {
            this.addressId = address_id;
        }
        public String getRecipientName() {
            return recipientName;
        }

        public void setRecipientName(String recipientName) {
            this.recipientName = recipientName;
        }

        public String getShippingAddress() {
            return shippingAddress;
        }

        public void setShippingAddress(String shippingAddress) {
            this.shippingAddress = shippingAddress;
        }

        public String getRecipientPhone() {
            return recipientPhone;
        }

        public void setRecipientPhone(String recipientPhone) {
            this.recipientPhone = recipientPhone;
        }
    }

    public static class Orders {

        private String order_id;
        private String shop_id;
        private String status;
        private String create_time;
        private String product_name;
        private String product_id;
        private String product_buy;
        private String buy_info;
        private String total_amount;
        private String receiver_name;
        private String receiver_phone;
        private String receiver_address;
        private String payment_time;
        private String logistics_company;
        private String ship_time;
        private String receive_time;
        private String product_imageUrl;

        public String getOrder_id() {
            return order_id;
        }

        public void setOrder_id(String order_id) {
            this.order_id = order_id;
        }

        public String getShop_id() {
            return shop_id;
        }

        public void setShop_id(String shop_id) {
            this.shop_id = shop_id;
        }

        public String getStatus() {
            return status;
        }

        public void setStatus(String status) {
            this.status = status;
        }

        public String getCreate_time() {
            return create_time;
        }

        public void setCreate_time(String create_time) {
            this.create_time = create_time;
        }

        public String getProduct_name() {
            return product_name;
        }

        public void setProduct_name(String product_name) {
            this.product_name = product_name;
        }

        public String getProduct_id() {
            return product_id;
        }

        public void setProduct_id(String product_id) {
            this.product_id = product_id;
        }

        public String getProduct_buy() {
            return product_buy;
        }

        public void setProduct_buy(String product_buy) {
            this.product_buy = product_buy;
        }

        public String getBuy_info() {
            return buy_info;
        }

        public void setBuy_info(String buy_info) {
            this.buy_info = buy_info;
        }

        public String getTotal_amount() {
            return total_amount;
        }

        public void setTotal_amount(String total_amount) {
            this.total_amount = total_amount;
        }

        public String getReceiver_name() {
            return receiver_name;
        }

        public void setReceiver_name(String receiver_name) {
            this.receiver_name = receiver_name;
        }

        public String getReceiver_phone() {
            return receiver_phone;
        }

        public void setReceiver_phone(String receiver_phone) {
            this.receiver_phone = receiver_phone;
        }

        public String getReceiver_address() {
            return receiver_address;
        }

        public void setReceiver_address(String receiver_address) {
            this.receiver_address = receiver_address;
        }

        public String getPayment_time() {
            return payment_time;
        }

        public void setPayment_time(String payment_time) {
            this.payment_time = payment_time;
        }

        public String getLogistics_company() {
            return logistics_company;
        }

        public void setLogistics_company(String logistics_company) {
            this.logistics_company = logistics_company;
        }

        public String getShip_time() {
            return ship_time;
        }

        public void setShip_time(String ship_time) {
            this.ship_time = ship_time;
        }

        public String getReceive_time() {
            return receive_time;
        }

        public void setReceive_time(String receive_time) {
            this.receive_time = receive_time;
        }

        public String getProduct_imageUrl() {
            return product_imageUrl;
        }

        public void setProduct_imageUrl(String product_imageUrl) {
            this.product_imageUrl = product_imageUrl;
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



    public List<Addresses> getAddresses() {
        return addresses;
    }

    public void setAddresses(List<Addresses> addresses) {
        this.addresses = addresses;
    }

    public List<Orders> getOrders() {
        return orders;
    }

    public void setOrders(List<Orders> orders) {
        this.orders = orders;
    }
    //成功的响应
    public static UserInfoResponse success(List<Addresses> addresses,List<Orders> orders) {
        UserInfoResponse response = new UserInfoResponse();
        response.setCode(200);
        response.setMessage("成功");
        response.setAddresses(addresses);
        response.setOrders(orders);
        return response;
    }

    public static UserInfoResponse error(String message) {
        UserInfoResponse response = new UserInfoResponse();
        response.setCode(400);
        response.setMessage(message);
        return response;
    }
}
