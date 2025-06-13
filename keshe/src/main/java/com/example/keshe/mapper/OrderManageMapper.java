package com.example.keshe.mapper;

import com.example.keshe.response.OrderManageResponse;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface OrderManageMapper {
    List<OrderManageResponse.OrderData> getAllOrdersByShopId(Long shopId);
     int deleteOrder(Long orderId);
     int updateOrderStatus(Long orderId, String logisticsCompany);
}
