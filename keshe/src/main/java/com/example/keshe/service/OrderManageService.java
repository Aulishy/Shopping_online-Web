package com.example.keshe.service;

import com.example.keshe.mapper.OrderManageMapper;
import com.example.keshe.response.OrderManageResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;

@Service
public class OrderManageService {

    @Autowired
    private OrderManageMapper orderManageMapper;

    public OrderManageResponse getAllOrdersByShopId(Long shopId){
        OrderManageResponse response = new OrderManageResponse();
        OrderManageResponse.OrderData data = new OrderManageResponse.OrderData();
        response.setData(List.of(data));

        return response;
    }

    public OrderManageResponse deleteOrder(@RequestBody OrderManageResponse.OrderData data){
        OrderManageResponse response = new OrderManageResponse();
        response.setCode(200);
        response.setMessage("删除成功");
        return response;
    }
    public OrderManageResponse updateOrderStatus(@RequestBody OrderManageResponse.OrderData data){
        OrderManageResponse response = new OrderManageResponse();
        response.setCode(200);
        response.setMessage("更新成功");
        return response;
    }
}
