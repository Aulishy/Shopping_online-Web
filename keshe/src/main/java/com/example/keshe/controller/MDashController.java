package com.example.keshe.controller;

import com.example.keshe.response.MDashDeleteResponse;
import com.example.keshe.response.MDashResponse;
import com.example.keshe.response.MDashStockResponse;
import com.example.keshe.service.MDashService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@CrossOrigin
public class MDashController {

    @Autowired
    private MDashService mDashService;

    @PostMapping("/merchant/dashboard")
    public MDashResponse getDashboardInfo(@RequestBody DashboardRequestDTO requestDTO) {
        String shopId = requestDTO.getId();
        return mDashService.getDashboardInfo(shopId);
    }

    // 专门用于 /dashboard 请求的 DTO
    private static class DashboardRequestDTO {
        private String id;

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }
    }

    @PostMapping("/merchant/updateProducts")
    public MDashStockResponse updateShopProducts(@RequestBody UpdateProductsRequestDTO requestDTO) {
        String productId = requestDTO.getProductId();
        int addAmount = requestDTO.getAddAmount();
        int result = mDashService.updateShopProducts(productId, addAmount);
        if (result == 1) {
            return MDashStockResponse.success();
        } else {
            return MDashStockResponse.error("更新失败");
        }
    }
    private static class UpdateProductsRequestDTO {
        private String productId;
        private int addAmount;

        public String getProductId() {
            return productId;
        }

        public void setProductId(String productId) {
            this.productId = productId;
        }

        public int getAddAmount() {
            return addAmount;
        }

        public void setAddAmount(int addAmount) {
            this.addAmount = addAmount;
        }
    }
    @PostMapping("/merchant/deleteProducts")
    public MDashDeleteResponse deleteShopProducts(@RequestBody DeleteProductsRequestDTO requestDTO) {
        String productId = requestDTO.getProductId();
        String shopId = requestDTO.getShopId();
        mDashService.deleteShopProducts(productId, shopId);
        return MDashDeleteResponse.success();
    }
    private static class DeleteProductsRequestDTO {
        private String productId;
        private String shopId;

        public String getProductId() {
            return productId;
        }

        public void setProductId(String productId) {
            this.productId = productId;
        }

        public String getShopId() {
            return shopId;
        }

        public void setShopId(String shopId) {
            this.shopId = shopId;
        }
    }
}
