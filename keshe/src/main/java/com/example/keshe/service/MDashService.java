package com.example.keshe.service;

import com.example.keshe.mapper.MDashMapper;
import com.example.keshe.response.MDashResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.util.List;

@Service
public class MDashService {

    @Autowired
    private MDashMapper mDashMapper;

    /**
     * 获取店铺综合信息，包括店铺详情、统计数据和商品列表
     *
     * @param shopId 店铺ID
     * @return 包含完整店铺信息的MDashResponse对象
     */
    public MDashResponse getDashboardInfo(String shopId) {
        MDashResponse response = new MDashResponse();

        // 获取店铺基本信息

        MDashResponse.ShopInfo shopInfo = mDashMapper.getShopInfo(shopId);
        System.out.println("shopInfo: " + shopInfo);
        response.setShopInfo(shopInfo);

        // 获取店铺统计信息
        MDashResponse.ShopStats shopStats = mDashMapper.getShopStats(shopId);
        response.setShopStats(shopStats);

        // 获取店铺商品列表
        List<MDashResponse.ShopProducts> shopProducts = mDashMapper.getShopProducts(shopId);
        response.setShopProducts(shopProducts);

        return response;
    }
    // 更新商品库存
    public int updateShopProducts(String productId, int amount) {
        return mDashMapper.updateShopProducts(productId, amount);
    }

    //下架商品
    public int deleteShopProducts(String productId,  String shopId) {
        return mDashMapper.deleteShopProducts(productId,  shopId);
    }
}
