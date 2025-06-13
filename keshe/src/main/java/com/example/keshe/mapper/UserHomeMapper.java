package com.example.keshe.mapper;

import com.example.keshe.response.UserHomeResponse;
import com.example.keshe.response.UserInfoResponse;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface UserHomeMapper {
    List<UserHomeResponse.HotProduct> selectByCategory(String category);
    List<UserInfoResponse.Addresses> selectUserAddress(int userId);
    List<UserInfoResponse.Orders> selectUserOrder(int userId);

    int insertAddress(int userId,String contact,String detail,String phone);
    int deleteAddress(int addressId,int userId);
    int updateAddress(String orderId);
    int deleteOrder(String orderId);
    int purchase(int userId, String productId, int quantity, int addressId, String note);
    int updateStock(String productId, int quantity);
}
