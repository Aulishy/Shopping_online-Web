// 修改后的 UserHomeComtroller.java
package com.example.keshe.controller;


import com.example.keshe.response.CommenResponse;
import com.example.keshe.response.UserHomeResponse; // 导入外部类
import com.example.keshe.response.UserInfoResponse;
import com.example.keshe.service.UserHomeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@CrossOrigin

public class UserHomeComtroller {

    @Autowired
    private UserHomeService userHomeService;

    @PostMapping( "/user/home")
    public UserHomeResponse getHotProducts(@RequestBody UserHomeRequestDTO requestDTO) {
        String category = requestDTO.getCurrentCategory();
        try {
            List<UserHomeResponse.HotProduct> hotProducts = userHomeService.getHotProducts(category);
            return UserHomeResponse.success("获取热销商品成功", hotProducts);
        } catch (Exception e) {
            return UserHomeResponse.error("获取热销商品失败：" + e.getMessage());
        }
    }

    // 请求 DTO（内部类）
    public static class UserHomeRequestDTO {
        private String currentCategory;

        public String getCurrentCategory() {
            return currentCategory;
        }

        public void setCurrentCategory(String currentCategory) {
            this.currentCategory = currentCategory;
        }
    }

    @PostMapping("/user/info")
    public UserInfoResponse getUserInfo(@RequestBody UserInfoRequestDTO requestDTO) {
        int userId = requestDTO.getUserId();
        try {
            // 假设接口返回addresses数组（原address字段改为addresses）
            List<UserInfoResponse.Addresses> addresses = userHomeService.getAddresses(userId);
            List<UserInfoResponse.Orders> orders = userHomeService.getOrders(userId);
            return UserInfoResponse.success( addresses, orders);
        } catch (Exception e) {
            return UserInfoResponse.error("获取用户信息失败：" + e.getMessage());
        }
    }

    public static class UserInfoRequestDTO {
        private int userId;

        public int getUserId() {
            return userId;
        }

        public void setUserId(int userId) {
            this.userId = userId;
        }
    }
    @PostMapping("/user/insertaddress")
    public CommenResponse insertAddress(@RequestBody InsertAddressRequestDTO requestDTO) {
        int userId = requestDTO.getUserId();
        String detail = requestDTO.getDetail();
        String contact = requestDTO.getContact();
        String phone = requestDTO.getPhone();
        try {
            int addressId = userHomeService.insertAddress(userId,contact,detail,phone);
            return CommenResponse.success();
        } catch (Exception e) {
            return CommenResponse.error("添加地址失败：" + e.getMessage());
        }
    }
    public static class InsertAddressRequestDTO {
        private int userId;
        private String detail;
        private String contact;
        private String phone;

        public int getUserId() {
            return userId;
        }

        public void setUserId(int userId) {
            this.userId = userId;
        }

        public String getDetail() {
            return detail;
        }

        public void setDetail(String detail) {
            this.detail = detail;
        }

        public String getContact() {
            return contact;
        }
        public void setContact(String contact) {
            this.contact = contact;
        }
        public String getPhone() {
            return phone;
        }
        public void setPhone(String phone) {
            this.phone = phone;
        }
    }
    // try {
    //    await axios.post('http://localhost:8081/user/deleteaddress',
    //      {
    //        userId: this.$route.query.id,  // 用户ID
    //        addressId: addrId             // 地址ID
    //      },
    //      {
    //        headers: {
    //          'Content-Type': 'application/json' // 明确指定JSON格式
    //        }
    //      }
    //    );

    @PostMapping("/user/deleteaddress")
    public CommenResponse deleteAddress(@RequestBody DeleteAddressRequestDTO requestDTO) {
        int userId = requestDTO.getUserId();
        int addressId = requestDTO.getAddressId();
        try {
            userHomeService.deleteAddress(userId,addressId);
            return CommenResponse.success();
        } catch (Exception e) {
            return CommenResponse.error("删除地址失败：" + e.getMessage());
        }
    }
    public static class DeleteAddressRequestDTO {
        private int userId;
        private int addressId;

        public int getUserId() {
            return userId;
        }

        public void setUserId(int userId) {
            this.userId = userId;
        }

        public int getAddressId() {
            return addressId;
        }

        public void setAddressId(int addressId) {
            this.addressId = addressId;
        }
    }
    //const response = await axios.post('http://localhost:8081/order/confirmReceive', {
    //          orderId: order.orderId  // 传递订单ID
    //        }, {
    //          headers: { 'Content-Type': 'application/json' }
    //        });

    @PostMapping("/user/confirmReceive")
    public CommenResponse confirmReceive(@RequestBody ConfirmReceiveRequestDTO requestDTO) {
        String orderId = requestDTO.getOrderId();
        try {
            userHomeService.confirmReceive(orderId);
            return CommenResponse.success();
        } catch (Exception e) {
            return CommenResponse.error("确认收货失败：" + e.getMessage());
        }
    }
    public static class ConfirmReceiveRequestDTO {
        private String orderId;
        public String getOrderId() {
            return orderId;
        }
        public void setOrderId(String orderId) {
            this.orderId = orderId;
        }
    }



    @PostMapping("/user/deleteorder")
    public CommenResponse deleteOrder(@RequestBody DeleteOrderRequestDTO requestDTO) {
        String orderId = requestDTO.getOrderId();
        int userId = requestDTO.getUserId();
        try {
            userHomeService.deleteOrder(orderId);
            return CommenResponse.success();
        } catch (Exception e) {
            return CommenResponse.error("删除订单失败：" + e.getMessage());
        }
    }
    public static class DeleteOrderRequestDTO {
        private String orderId;
        private int userId;
        public String getOrderId() {
            return orderId;
        }
        public void setOrderId(String orderId) {
            this.orderId = orderId;
        }
        public int getUserId() {
            return userId;
        }
        public void setUserId(int userId) {
            this.userId = userId;
        }
    }

    // const response = await axios.post('http://localhost:8081/user/purchase', {
    //          productId: this.selectedProduct.product_id,
    //          quantity: this.purchaseQuantity,
    //          userId: this.$route.query.id,
    //          // 传递完整地址信息或 addressId（根据后端需求）
    //          addressId: this.selectedAddress.addressId,
    //          note: this.purchaseNote
    //        });

    @PostMapping("/user/purchase")
    public CommenResponse purchase(@RequestBody PurchaseRequestDTO requestDTO) {
        int userId = requestDTO.getUserId();
        String productId = requestDTO.getProductId();
        int quantity = requestDTO.getQuantity();
        int addressId = requestDTO.getAddressId();
        String note = requestDTO.getNote();
        try {
            if(userHomeService.purchase(userId, productId, quantity, addressId, note)!=0)
                return CommenResponse.success();
            return CommenResponse.error("购买失败：");
        } catch (Exception e) {
            return CommenResponse.error("购买失败：" + e.getMessage());
        }
    }
    public static class PurchaseRequestDTO {
        private int userId;
        private String productId;
        private int quantity;
        private int addressId;
        private String note;
        public int getUserId() {
            return userId;
        }
        public void setUserId(int userId) {
            this.userId = userId;
        }
        public String getProductId() {
            return productId;
        }
        public void setProductId(String productId) {
            this.productId = productId;
        }
        public int getQuantity() {
            return quantity;
        }
        public void setQuantity(int quantity) {
            this.quantity = quantity;
        }
        public int getAddressId() {
            return addressId;
        }
        public void setAddressId(int addressId) {
            this.addressId = addressId;
        }
        public String getNote() {
            return note;
        }
        public void setNote(String note) {
            this.note = note;
        }
    }
}
