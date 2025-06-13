package com.example.keshe.service;

import com.example.keshe.response.CommenResponse;
import com.example.keshe.response.ProPubliResponse;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ProPubliService {
    public ProPubliResponse getProPubliByShopId(Long shopId){
        ProPubliResponse response = new ProPubliResponse();
         response.setCode(200);
         response.setMessage("成功");
       ProPubliResponse.ProPubliData data = new ProPubliResponse.ProPubliData();
       response.setAuditrecords( List.of (data));
          return response;
    }

     public ProPubliResponse deleteAuditRecord(Long shopId, Long auditId){
        ProPubliResponse response = new ProPubliResponse();
        response.setCode(200);
        response.setMessage("失败");
        return response;
    }
    public CommenResponse insertAuditRecord(String product_name, String image_url,double price, int stock,String category,String description, Long shopId, String submit_time){
        CommenResponse  response = new CommenResponse();
        response.setCode(200);
        response.setMessage("成功");
        return response;
    }


}
