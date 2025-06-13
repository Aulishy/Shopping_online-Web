package com.example.keshe.mapper;

import com.example.keshe.response.MDashResponse;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface MDashMapper {

    MDashResponse getDashboardInfo(String shopId);

    MDashResponse.ShopInfo getShopInfo(String shopId);

    MDashResponse.ShopStats getShopStats(String shopId);

    List<MDashResponse.ShopProducts> getShopProducts(String shopId);

    int updateShopProducts(String productId, int addAmount);

    int deleteShopProducts(String productId, String  shopId);
}
