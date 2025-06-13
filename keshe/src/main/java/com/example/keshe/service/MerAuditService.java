package com.example.keshe.service;

import com.example.keshe.mapper.MerAuditMapper;
import com.example.keshe.response.MerAuditResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
@Service
public class MerAuditService {

    @Autowired
    private MerAuditMapper merAuditMapper;

    public MerAuditResponse getMerAuditByMerId() {
        List<MerAuditResponse.Audit> data = merAuditMapper.getMerAuditByMerId();

        MerAuditResponse response = new MerAuditResponse();
        response.setCode(200);
        response.setMessage("查询成功");
        response.setData(data);

        return response;
    }
    public int approveMerchant(int merchantId) {
        int a=merAuditMapper.insertMerAudit(merchantId);
        int b=merAuditMapper.updateMerAuditStatus(merchantId);
        return a*b;
    }

    public int rejectMerchant(int merchantId,String reason) {
        return merAuditMapper.rejectMerAuditStatus(merchantId,reason);
    }

    public int approveProduct(int pauditId) {
        int a=merAuditMapper.insertProAudit(pauditId);
        int b=merAuditMapper.updateProAudit(pauditId);
        return a*b;
    }
    public int rejectProduct(int pauditId,String reason) {
        return merAuditMapper.rejectProAuditStatus(pauditId,reason);
    }
}
