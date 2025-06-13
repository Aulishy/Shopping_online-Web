package com.example.keshe.mapper;
import com.example.keshe.response.ProPubliResponse;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.web.multipart.MultipartFile;

import java.util.Date;
import java.util.List;

@Mapper
public interface ProPubliMapper {
    List<ProPubliResponse.ProPubliData> getProPubliByShopId(Long shopId);
    int deleteAuditRecord(Long shopId,  Long auditId);
    int insertAuditRecord(String product_name, String image_url, double price, int stock, String category, String description, Long shopId, String submit_time);
}
