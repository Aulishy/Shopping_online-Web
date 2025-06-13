package com.example.keshe.controller;


import com.example.keshe.response.CommenResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import com.example.keshe.mapper.ProPubliMapper;
  import com.example.keshe.response.ProPubliResponse;
  import com.example.keshe.response.CencelAuditResponse;
import org.springframework.web.multipart.MultipartFile;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.UUID;



@RestController
@CrossOrigin
public class ProPubliController {
    //    const response = await axios.post('http://localhost:8081/merchant/audit-records', {
//        shopId: this.$route.query.id // 传递当前商家ID（从路由参数获取）
//    });//这是前端往后端传送的数据
    @Autowired
    private ProPubliMapper proPubliMapper;

    @PostMapping("/merchant/audit-records")
    public ProPubliResponse getProPubliByShopId(@RequestBody ProPubliRequestDTO requestDTO) {
        Long shopId = requestDTO.getShopId();
        List<ProPubliResponse.ProPubliData> proPublis = proPubliMapper.getProPubliByShopId(shopId);
        ProPubliResponse response = new ProPubliResponse();
        response.setCode(200);
        response.setMessage("获取商品审核记录成功");
        response.setAuditrecords(proPublis);
        return response;
    }

    private static class ProPubliRequestDTO {
        private Long shopId;

        public Long getShopId() {
            return shopId;
        }

        public void setShopId(Long shopId) {
            this.shopId = shopId;
        }
    }

    //    const response = await axios.post('http://localhost:8081/merchant/cancel-audit', {
//        auditId: auditItem.paudit_id,
//                shopId: this.$route.query.id  // 新增：传递当前商家ID
//    });//撤销审核
    @PostMapping("/merchant/cancel-audit")
    public CencelAuditResponse cancelAudit(@RequestBody CencelAuditRequestDTO requestDTO) {
        Long auditId = requestDTO.getAuditId();
        Long shopId = requestDTO.getShopId();
        int deletedRows = proPubliMapper.deleteAuditRecord(shopId, auditId);
        CencelAuditResponse response = new CencelAuditResponse();
        if (deletedRows > 0) {
            response.setCode(200);
            response.setMessage("删除审核记录成功");
        } else {
            response.setCode(500);
            response.setMessage("删除审核记录失败");
        }
        return response;
    }

    private static class CencelAuditRequestDTO {
        private Long auditId;
        private Long shopId;

        public Long getAuditId() {
            return auditId;
        }

        public void setAuditId(Long auditId) {
            this.auditId = auditId;
        }

        public Long getShopId() {
            return shopId;
        }

        public void setShopId(Long shopId) {
            this.shopId = shopId;
        }
    }
    // formData.append('product_name', this.productForm.productName);
    //  formData.append('image', this.productForm.imageFile);  // 上传文件
    //  formData.append('price', this.productForm.price);
    //  formData.append('stock', this.productForm.stock);
    //  formData.append('category', this.productForm.category);
    //  formData.append('description', this.productForm.description);
    //  //获取shopId
    //  const shopId = this.$route.query.id;
    //  //获取提交时间
    //  const submitTime = new Date().toLocaleString('zh-CN');
    //  formData.append('shopId', shopId);
    //  formData.append('submitTime', submitTime);
    //  //提交表格
    //  const response=await axios.post('http://localhost:8081/merchant/propublish', formData, {
    //    headers: { 'Content-Type': 'multipart/form-data' }
    //  });//发布产品，将数据插入到product_audit表

    @PostMapping("/merchant/propublish")
    public CommenResponse publishProduct(@RequestParam("product_name") String product_name,
                                         @RequestParam("image") MultipartFile image,
                                         @RequestParam("price") double price,
                                         @RequestParam("stock") int stock,
                                         @RequestParam("category") String category,
                                         @RequestParam("description") String description,
                                         @RequestParam("shopId") Long shopId,
                                         @RequestParam("submitTime") String submit_time
    ) {
        try {
            String image_url = saveFile(image);
            proPubliMapper.insertAuditRecord(product_name, image_url, price, stock, category, description, shopId, submit_time);
            return CommenResponse.success();
        } catch (Exception e) {
            e.printStackTrace();
            return CommenResponse.error("失败");
        }



    }
    public String saveFile(MultipartFile file) {
        if (file.isEmpty()) {
            return null;
        }

        try {

            BufferedImage originalImage = ImageIO.read(file.getInputStream());

            // 设置目标宽高（例如：最大宽度为 800px，等比缩放）
            int targetWidth = 200;
            int targetHeight = (int) Math.round(
                    (double) targetWidth / originalImage.getWidth() * originalImage.getHeight()
            );

            // 创建缩略图图像
            Image scaledImage = originalImage.getScaledInstance(targetWidth, targetHeight, Image.SCALE_SMOOTH);

            // 将缩略图转回 BufferedImage
            BufferedImage outputImage = new BufferedImage(targetWidth, targetHeight, BufferedImage.TYPE_INT_RGB);
            Graphics2D g2d = outputImage.createGraphics();
            g2d.drawImage(scaledImage, 0, 0, null);
            g2d.dispose();
            String rootPath = System.getProperty("user.dir");

            Path parentPath = Paths.get(rootPath).getParent();

            // 拼接前端项目的 public/assets 目录：D:\Users\杨\Desktop\web-sc\shoppingonline\public\assets
            String uploadDir = parentPath
                    .resolve("shoppingonline")  // 前端项目名
                    .resolve("public")          // public 目录
                    .resolve("assets")          // assets 子目录
                    .toString();

            Path uploadPath = Paths.get(uploadDir);

            // 目录不存在则创建
            if (!Files.exists(uploadPath)) {
                Files.createDirectories(uploadPath);
            }

            // 生成唯一文件名（保持原有逻辑）
            String originalFilename = file.getOriginalFilename();
            String fileExtension = "";
            if (originalFilename != null && originalFilename.contains(".")) {
                fileExtension = originalFilename.substring(originalFilename.lastIndexOf("."));
            }
            String uniqueFileName = UUID.randomUUID() + fileExtension;

            // 写入文件（保持原有逻辑）
            Path filePath = uploadPath.resolve(uniqueFileName);
            file.transferTo(filePath);

            // 返回前端可访问的相对路径（与 public/assets 目录对应）
            return "/assets/" + uniqueFileName;

        } catch (IOException e) {
            throw new RuntimeException("文件上传失败：" + e.getMessage());
        }
    }
}