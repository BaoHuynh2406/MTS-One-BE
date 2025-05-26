package com.mts.mtsone.modules.util.Image.Service.impl;

import com.mts.mtsone.common.exception.BaseException;
import com.mts.mtsone.common.exception.BusinessException;
import com.mts.mtsone.common.exception.ResourceNotFoundException;
import com.mts.mtsone.modules.util.Image.Service.imageService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.UUID;

@Service
public class imageServiceImpl implements imageService {

    @Value("${app.upload.dir}")
    private String uploadDir;

    @Override
    public String saveImage(MultipartFile file, String subFolder) throws IOException {
        // Kiểm tra file
        if (file.isEmpty()) {
            throw new ResourceNotFoundException("File is empty", "file", file);
        }

        // Kiểm tra định dạng file
        String contentType = file.getContentType();
        if (contentType == null || !contentType.startsWith("image/")) {
            throw new BaseException("File must be an image", "file", HttpStatus.CONFLICT);
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
            throw new ResourceNotFoundException("Image not found", "Image", imagePath);
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