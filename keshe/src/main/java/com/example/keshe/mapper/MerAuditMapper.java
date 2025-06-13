package com.example.keshe.mapper;

import com.example.keshe.response.MerAuditResponse;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;
@Mapper
public interface MerAuditMapper {
    List<MerAuditResponse.Audit> getMerAuditByMerId();
    int updateMerAuditStatus(int merchantId);
    int insertMerAudit(int merchantId);
    int rejectMerAuditStatus(int merchantId,String reason);
    int updateProAudit(int pauditId);
    int insertProAudit(int pauditId);
    int rejectProAuditStatus(int pauditId,String reason);
}
