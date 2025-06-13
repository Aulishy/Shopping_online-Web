package com.example.keshe.service;

import com.example.keshe.mapper.UserHomeMapper;
import com.example.keshe.response.UserHomeResponse;
import com.example.keshe.response.UserInfoResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserHomeService {

    @Autowired
    private UserHomeMapper userHomeMapper;

    public List<UserHomeResponse.HotProduct> getHotProducts(String category) {
        return userHomeMapper.selectByCategory(category);
    }
    public List<UserInfoResponse.Addresses> getAddresses(int userId) {
        return userHomeMapper.selectUserAddress(userId);
    }
    public List<UserInfoResponse.Orders> getOrders(int  userId) {
        return userHomeMapper.selectUserOrder(userId);
    }
    public int insertAddress(int userId, String contact,String detail,String phone) {
        return userHomeMapper.insertAddress(userId,contact,detail,phone);
    }
    public int deleteAddress(int addressId,int userId) {
        return userHomeMapper.deleteAddress(addressId,userId);
    }
    public int confirmReceive(String orderId) {
        return userHomeMapper.updateAddress(orderId);
    }
    public int deleteOrder(String orderId) {
        return userHomeMapper.deleteOrder(orderId);
    }

    public int purchase(int userId, String productId, int quantity, int addressId, String note) {
        int a=userHomeMapper.purchase(userId, productId, quantity, addressId, note);
        int b=userHomeMapper.updateStock(productId, quantity);
        return a*b;
    }
}
