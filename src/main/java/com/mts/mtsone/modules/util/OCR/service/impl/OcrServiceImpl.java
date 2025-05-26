package com.mts.mtsone.modules.util.OCR.service.impl;

import com.mts.mtsone.common.exception.BusinessException;
import com.mts.mtsone.modules.util.OCR.service.OcrService;
import lombok.extern.slf4j.Slf4j;
import net.sourceforge.tess4j.Tesseract;
import net.sourceforge.tess4j.TesseractException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;

@Slf4j
@Service
public class OcrServiceImpl implements OcrService {

    @Value("${app.upload.dir}")
    private String uploadDir;

    @Override
    public String performOcr(String imagePath) throws IOException {
        try {
            // Khởi tạo Tesseract
            Tesseract tesseract = new Tesseract();
            
            // Thiết lập ngôn ngữ cho OCR (có thể thêm nhiều ngôn ngữ)
            tesseract.setLanguage("vie+eng"); // Vietnamese + English
            
            // Đường dẫn đầy đủ đến file ảnh
            Path fullPath = Paths.get(uploadDir, imagePath);
            File imageFile = fullPath.toFile();
            
            if (!imageFile.exists()) {
                throw new BusinessException("Image file not found: " + imagePath);
            }

            // Thực hiện OCR
            String result = tesseract.doOCR(imageFile);
            
            // Clean up kết quả (loại bỏ khoảng trắng thừa, ký tự đặc biệt)
            result = result.trim().replaceAll("\\s+", " ");
            
            return result;

        } catch (TesseractException e) {
            log.error("Error performing OCR on image: " + imagePath, e);
            throw new BusinessException("Failed to perform OCR: " + e.getMessage());
        }
    }
}
