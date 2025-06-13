package com.example.keshe.controller;

import com.example.keshe.response.CommenResponse;
import com.example.keshe.response.MerAuditResponse;
import com.example.keshe.mapper.MerAuditMapper;
import com.example.keshe.service.MerAuditService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@CrossOrigin
public class MerAuditController {


    @Autowired
    private MerAuditService MerAuditService;


    @PostMapping("/admin/pending-merchants")
    public MerAuditResponse approveMerchant() {

        return MerAuditService.getMerAuditByMerId();
    }

    @PostMapping("/admin/approve-merchant")
    public CommenResponse approveMerchant(@RequestBody MerAuditDataDTO requestDTO) {
        int merchantId = requestDTO.getMerchantId();
        try {
            if(MerAuditService.approveMerchant(merchantId)==1){
                return CommenResponse.success();
            }else{
                return CommenResponse.error("审核商户失败");
            }
        } catch (Exception e) {
            e.printStackTrace();
            return CommenResponse.error("审核商户失败：" + e.getMessage());
        }
    }

    public static class MerAuditDataDTO {
        private int merchantId;

        public int getMerchantId() {
            return merchantId;
        }

        public void setMerchantId(int merchantId) {
            this.merchantId = merchantId;
        }
    }

    //await axios.post('http://localhost:8081/admin/reject-merchant', {
    //          merchantId,
    //          reason

    @PostMapping("/admin/reject-merchant")
    public CommenResponse rejectMerchant(@RequestBody MerRejectDataDTO requestDTO) {
        int merchantId = requestDTO.getMerchantId();
        String reason = requestDTO.getReason();
        try {
            MerAuditService.rejectMerchant(merchantId,reason);
                return CommenResponse.success();

        } catch (Exception e) {
            e.printStackTrace();
            return CommenResponse.error("拒绝商户失败：" + e.getMessage());
        }
    }
    public static class MerRejectDataDTO {
        private int merchantId;
        private String reason;

        public int getMerchantId() {
            return merchantId;
        }

        public void setMerchantId(int merchantId) {
            this.merchantId = merchantId;
        }

        public String getReason() {
            return reason;
        }

        public void setReason(String reason) {
            this.reason = reason;
        }
    }


    @PostMapping("/admin/approve-product")
    public CommenResponse approveProduct(@RequestBody ProAuditDataDTO requestDTO) {
        int pauditId = requestDTO.getPauditId();
        try {
            if(MerAuditService.approveProduct(pauditId)==1){
                return CommenResponse.success();
            }else{
                return CommenResponse.error("审核商品失败");
            }
        } catch (Exception e) {
            e.printStackTrace();
            return CommenResponse.error("审核商品失败：" + e.getMessage());
        }
    }
    public static class ProAuditDataDTO {
        private int pauditId;

        public int getPauditId() {
            return pauditId;
        }

        public void setPauditId(int pauditId) {
            this.pauditId = pauditId;
        }
    }

    //await axios.post('http://localhost:8081/admin/reject-product', {
    //          productId,
    //          reason

    @PostMapping("/admin/reject-product")
    public CommenResponse rejectProduct(@RequestBody ProRejectDataDTO requestDTO) {
        int pauditId = requestDTO.getPauditId();
        String reason = requestDTO.getReason();
        try {
            MerAuditService.rejectProduct(pauditId,reason);
                return CommenResponse.success();

        } catch (Exception e) {
            e.printStackTrace();
            return CommenResponse.error("拒绝商品失败：" + e.getMessage());
        }
    }
    public static class ProRejectDataDTO {
        private int pauditId;
        private String reason;

        public int getPauditId() {
            return pauditId;
        }

        public void setPauditId(int pauditId) {
            this.pauditId = pauditId;
        }

        public String getReason() {
            return reason;
        }

        public void setReason(String reason) {
            this.reason = reason;
        }
    }
}
