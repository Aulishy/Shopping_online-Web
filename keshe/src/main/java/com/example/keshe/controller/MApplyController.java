package com.example.keshe.controller;
import com.example.keshe.response.MApplyResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import com.example.keshe.service.MApplyService;

import java.util.Date;

@RestController
@CrossOrigin
public class MApplyController {

    @Autowired
    private MApplyService mApplyService;

    @PostMapping("/merchant/apply")
    public MApplyResponse submitMApply(
            @RequestParam("shopName") String shopName,
            @RequestParam("shopAddress") String shopAddress,
            @RequestParam("legalRepresentative") String legalRepresentative,
            @RequestParam("idNumber") String idNumber,
            @RequestParam("contactNumber") String contactNumber,
            @RequestParam("businessLicense") MultipartFile businessLicense,
            @RequestParam("shopCategory") String shopCategory,
            @RequestParam("shopDescription") String shopDescription,
            @RequestParam("idCardFront") MultipartFile idCardFront,
            @RequestParam("idCardBack") MultipartFile idCardBack) {

        try {
           mApplyService.saveMApply(
                    shopName, shopAddress, legalRepresentative, idNumber, contactNumber,
                    businessLicense, shopCategory, shopDescription, idCardFront, idCardBack);
            String applyId = String.valueOf(System.currentTimeMillis()); // 模拟 ID
            String applyTimeString = new Date().toString(); // 模拟申请时间

            return MApplyResponse.success(applyId, applyTimeString);
        } catch (Exception e) {
            e.printStackTrace();

            return MApplyResponse.error(500,"申请提交失败：" + e.getMessage());
        }
    }
}