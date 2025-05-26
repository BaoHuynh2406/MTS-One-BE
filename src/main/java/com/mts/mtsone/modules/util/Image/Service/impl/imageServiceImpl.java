package com.mts.mtsone.modules.util.Image.Service.impl;

import com.mts.mtsone.common.exception.BusinessException;
import com.mts.mtsone.modules.util.Image.Service.ImageService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.UUID;

@Service
public class ImageServiceImpl implements ImageService {

    @Value("${app.upload.dir}")
    private String uploadDir;

    @Override
    public String saveImage(MultipartFile file, String subFolder) throws IOException {
        // Kiểm tra file
        if (file.isEmpty()) {
            throw new BusinessException("File is empty");
        }

        // Kiểm tra định dạng file
        String contentType = file.getContentType();
        if (contentType == null || !contentType.startsWith("image/")) {
            throw new BusinessException("File must be an image");
        }

        // Tạo tên file ngẫu nhiên với UUID
        String originalFilename = file.getOriginalFilename();
        String extension = originalFilename != null ? originalFilename.substring(originalFilename.lastIndexOf(".")) : "";
        String newFileName = UUID.randomUUID() + extension;

        // Tạo đường dẫn thư mục
        String folderPath = uploadDir + subFolder;
        Path uploadPath = Paths.get(folderPath);
        
        // Tạo thư mục nếu chưa tồn tại
        if (!Files.exists(uploadPath)) {
            Files.createDirectories(uploadPath);
        }

        // Lưu file
        Path filePath = uploadPath.resolve(newFileName);
        Files.copy(file.getInputStream(), filePath);

        // Trả về đường dẫn tương đối
        return subFolder + "/" + newFileName;
    }

    @Override
    public byte[] getImage(String imagePath) throws IOException {
        Path path = Paths.get(uploadDir + imagePath);
        if (!Files.exists(path)) {
            throw new BusinessException("Image not found");
        }
        return Files.readAllBytes(path);
    }

    @Override
    public void deleteImage(String imagePath) throws IOException {
        Path path = Paths.get(uploadDir + imagePath);
        if (Files.exists(path)) {
            Files.delete(path);
        }
    }
}