package com.example.keshe.service;

import com.example.keshe.mapper.AdminMapper;
import com.example.keshe.response.AdminResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AdminService {
    @Autowired
    private AdminMapper adminMapper;

    public List<AdminResponse.ProductAudit> getPendingProducts(){
        return adminMapper.getProductAudit();
    }

}
