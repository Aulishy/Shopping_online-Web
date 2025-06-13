package com.example.keshe.service;

import com.example.keshe.mapper.MApplyMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Date;
import java.util.UUID;

import com.example.keshe.entity.ApplyForm;

import javax.imageio.ImageIO;

@Service
public class MApplyService { // 文件名也应改为 MApplyService.java

    @Autowired
    private MApplyMapper mApplyMapper;

    @Transactional
    public void saveMApply(String shopName, String shopAddress, String legalRepresentative, String idNumber, String contactNumber,
                           MultipartFile businessLicense, String shopCategory, String shopDescription, MultipartFile idCardFront, MultipartFile idCardBack) {

        // 存储文件到服务器或对象存储，获取文件路径
        String businessLicensePath = saveFile(businessLicense);
        String idCardFrontPath = saveFile(idCardFront);
        String idCardBackPath = saveFile(idCardBack);

        // 创建商家申请对象
        ApplyForm applyForm = new ApplyForm();
        applyForm.setShopName(shopName);
        applyForm.setShopAddress(shopAddress);
        applyForm.setLegalRepresentative(legalRepresentative);
        applyForm.setIdNumber(idNumber);
        applyForm.setContactNumber(contactNumber);
        applyForm.setBusinessLicense(businessLicensePath);
        applyForm.setShopCategory(shopCategory);
        applyForm.setShopDescription(shopDescription);
        applyForm.setIdCardFront(idCardFrontPath);
        applyForm.setIdCardBack(idCardBackPath);
        applyForm.setApplyTime(new Date());

        // 插入数据库
        mApplyMapper.insertApply_Form(applyForm);
    }


    public String saveFile(MultipartFile file) {
        if (file.isEmpty()) {
            return null; // 文件为空则不处理
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