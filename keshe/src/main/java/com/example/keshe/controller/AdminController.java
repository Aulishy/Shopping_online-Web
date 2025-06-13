package com.example.keshe.controller;

import com.example.keshe.response.AdminResponse;
import com.example.keshe.service.AdminService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@CrossOrigin
public class AdminController {
    @Autowired
    private AdminService adminService;
    //const res = await axios.post('http://localhost:8081/admin/pending-products', {});
    @PostMapping("/admin/pending-products")
    public AdminResponse getPendingProducts(){
        try{
            List<AdminResponse.ProductAudit> response = adminService.getPendingProducts();
            return AdminResponse.success(response);
        }catch (Exception e){
            return AdminResponse.error("获取待审核商品失败");
        }

    }
}
