package com.example.keshe.controller;

import com.example.keshe.mapper.OrderManageMapper;
import com.example.keshe.response.OrderManageResponse;
import com.example.keshe.response.OrderDeleResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@CrossOrigin

public class OrderManageController {

    @Autowired
    private OrderManageMapper orderManageMapper;

    @PostMapping("/merchant/ordermanagement")
    public OrderManageResponse getOrdersByShopId(@RequestBody OrderManageRequestDTO requestDTO) {
        Long shopId = requestDTO.getShopId();

        List<OrderManageResponse.OrderData> orders = orderManageMapper.getAllOrdersByShopId(shopId);

        OrderManageResponse response = new OrderManageResponse();
        response.setCode(200);
        response.setMessage("获取订单列表成功");
        response.setData(orders); //

        return response;
    }

    private static class OrderManageRequestDTO {
        private Long shopId;

        public Long getShopId() {
            return shopId;
        }

        public void setShopId(Long shopId) {
            this.shopId = shopId;
        }
    }
//    const response = await axios.post('http://localhost:8081/merchant/deleteorder', {
//        orderId: order.orderId, // 传递订单ID
//                shopId: this.$route.query.id // 传递商家ID（根据后端需求）
//    });前端传过来的数据
    @PostMapping("/merchant/deleteorder")
     public OrderDeleResponse deleteOrder(@RequestBody OrderDeleRequestDTO requestDTO) {
        Long orderId = requestDTO.getOrderId();
         Long shopId = requestDTO.getShopId();
         orderManageMapper.deleteOrder(orderId);
         if (orderId != null) {
             return OrderDeleResponse.success();
         } else {
             return OrderDeleResponse.error("删除失败");
         }
    }
    private static class OrderDeleRequestDTO {
        private Long orderId;
        private Long shopId;

        public Long getOrderId() {
            return orderId;
        }

        public void setOrderId(Long orderId) {
            this.orderId = orderId;
        }

        public Long getShopId() {
            return shopId;
        }

        public void setShopId(Long shopId) {
            this.shopId = shopId;
        }
    }
//     const response = await axios.post('http://localhost:8081/order/ship', {
//        orderId: order.orderId, // 修改：使用 orderId
//                logisticsCompany: logisticsCompany
//    });//标记发货
    @PostMapping("/order/ship")
    public OrderDeleResponse shipOrder(@RequestBody ShipOrderRequestDTO requestDTO) {
        Long orderId = requestDTO.getOrderId();
        String logisticsCompany = requestDTO.getLogisticsCompany();
      try {
          // 更新订单状态为已发货
          orderManageMapper.updateOrderStatus(orderId, logisticsCompany);
          return OrderDeleResponse.success();
      } catch (Exception e) {
          return OrderDeleResponse.error("发货失败");
      }

    }
     private static class ShipOrderRequestDTO {
        private Long orderId;
        private String logisticsCompany;

        public Long getOrderId() {
            return orderId;
        }

        public void setOrderId(Long orderId) {
            this.orderId = orderId;
        }

        public String getLogisticsCompany() {
            return logisticsCompany;
        }

        public void setLogisticsCompany(String logisticsCompany) {
            this.logisticsCompany = logisticsCompany;
        }
    }

}
