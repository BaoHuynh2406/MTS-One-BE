package com.mts.mtsone.modules.util.OCR.service.impl;

import com.mts.mtsone.common.exception.BaseException;
import com.mts.mtsone.common.exception.ResourceNotFoundException;
import com.mts.mtsone.modules.util.OCR.service.OcrService;
import lombok.extern.slf4j.Slf4j;
import net.sourceforge.tess4j.Tesseract;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.io.File;
import java.nio.file.Path;
import java.nio.file.Paths;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.awt.image.RescaleOp;
import java.awt.image.ConvolveOp;
import java.awt.image.Kernel;

// Thêm import cho JavaCV/OpenCV
import org.bytedeco.opencv.global.opencv_imgcodecs;
import org.bytedeco.opencv.global.opencv_imgproc;
import org.bytedeco.opencv.global.opencv_core;
import org.bytedeco.opencv.opencv_core.*;
import org.bytedeco.opencv.opencv_imgproc.Vec4iVector;

import static org.bytedeco.opencv.global.opencv_core.CV_8UC3;

@Slf4j
@Service
public class OcrServiceImpl implements OcrService {

    @Value("${app.upload.dir}")
    private String uploadDir;

    @Override
    public String performOcr(String imagePath)  {
        try {
            // Tạo đối tượng Tesseract
            Tesseract tesseract = new Tesseract();
            tesseract.setLanguage("vie");

            Path fullPath = Paths.get(uploadDir, imagePath);
            File imageFile = fullPath.toFile();

            if (!imageFile.exists()) {
                throw new ResourceNotFoundException("Image file not found: ", "path",imagePath);
            }



            // Tiền xử lý ảnh (nếu cần)
            // Ví dụ: Chuyển đổi sang ảnh xám, làm mịn, v.v.

            // Thực hiện OCR
            String result = tesseract.doOCR(imageFile);
            return result;

        } catch (Exception e) {
            log.error("Error during OCR processing", e);
            throw new BaseException("e",e.getMessage() ,HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }


    private BufferedImage preprocessImage(BufferedImage image) {
        // Chuyển đổi ảnh sang đen trắng
        BufferedImage grayImage = new BufferedImage(image.getWidth(), image.getHeight(), BufferedImage.TYPE_BYTE_GRAY);
        grayImage.getGraphics().drawImage(image, 0, 0, null);

        // Cải thiện độ tương phản
        RescaleOp rescaleOp = new RescaleOp(1.5f, 0, null);
        BufferedImage contrastImage = rescaleOp.filter(grayImage, null);

        // Làm mịn ảnh
        float[] kernel = {0.0625f, 0.125f, 0.0625f,
                          0.125f, 0.25f, 0.125f,
                          0.0625f, 0.125f, 0.0625f};
        Kernel convolveKernel = new Kernel(3, 3, kernel);
        ConvolveOp convolveOp = new ConvolveOp(convolveKernel);
        BufferedImage smoothedImage = convolveOp.filter(contrastImage, null);
         //Debug lưu ảnh lại
        try {
            ImageIO.write(smoothedImage, "png", new File("preprocessed_image.png"));
        } catch (java.io.IOException e) {
            log.error("Failed to write preprocessed image", e);
        }
        return smoothedImage;
    }
}
