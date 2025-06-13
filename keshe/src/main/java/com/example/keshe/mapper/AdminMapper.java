package com.example.keshe.mapper;

import com.example.keshe.response.AdminResponse;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface AdminMapper {
    List<AdminResponse.ProductAudit> getProductAudit();

}
