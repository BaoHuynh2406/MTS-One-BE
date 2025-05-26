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
            // Khởi tạo Tesseract
            Tesseract tesseract = new Tesseract();
            tesseract.setLanguage("vie");
            tesseract.setOcrEngineMode(1); // OEM_LSTM_ONLY
            tesseract.setPageSegMode(3);   // PSM_AUTO
            tesseract.setTessVariable("preserve_interword_spaces", "1");
            tesseract.setTessVariable("user_defined_dpi", "300");

            Path fullPath = Paths.get(uploadDir, imagePath);
            File imageFile = fullPath.toFile();

            if (!imageFile.exists()) {
                throw new ResourceNotFoundException("Image file not found: ", "path",imagePath);
            }

            // Đọc ảnh gốc
            Mat src = opencv_imgcodecs.imread(fullPath.toString());
            if (src.empty()) {
                throw new BaseException("Failed to read image file: ", imagePath, HttpStatus.BAD_REQUEST);
            }

            // Chuyển sang grayscale
            Mat gray = new Mat();
            opencv_imgproc.cvtColor(src, gray, opencv_imgproc.COLOR_BGR2GRAY);

            // Làm mờ nhẹ để giảm nhiễu
            Mat blurred = new Mat();
            opencv_imgproc.GaussianBlur(gray, blurred, new Size(3,3), 0);

            // Phát hiện cạnh bằng Canny
            Mat edges = new Mat();
            opencv_imgproc.Canny(blurred, edges, 50, 150);

            // Phát hiện đường thẳng bằng HoughLinesP
            Vec4iVector lines = new Vec4iVector();
            opencv_imgproc.HoughLinesP(edges, lines, 1, Math.PI/180, 50, 100, 10);

            // Tạo ảnh debug để vẽ các đường thẳng
            Mat lineDebugMat = src.clone();
            
            // Vẽ các đường thẳng phát hiện được
            for (long i = 0; i < lines.size(); i++) {
                Scalar4i line = lines.get(i);
                int x1 = line.get(0);
                int y1 = line.get(1);
                int x2 = line.get(2);
                int y2 = line.get(3);
                
                // Tính độ dài đường thẳng
                double length = Math.sqrt(Math.pow(x2 - x1, 2) + Math.pow(y2 - y1, 2));
                
                // Chỉ vẽ đường thẳng có độ dài >= 100px
                if (length >= 300) {
                    // Vẽ đường thẳng màu đỏ
                    opencv_imgproc.line(lineDebugMat, 
                        new Point(x1, y1),
                        new Point(x2, y2),
                        new Scalar(0, 0, 255, 0), // BGR color
                        2, // thickness
                        opencv_imgproc.LINE_AA,
                        0);
                }
            }

            // Lưu ảnh debug với các đường thẳng
            String lineDebugFileName = imagePath + "_lines_debug.png";
            opencv_imgcodecs.imwrite(Paths.get(uploadDir, lineDebugFileName).toString(), lineDebugMat);

            // Tìm contour
            MatVector contours = new MatVector();
            Mat hierarchy = new Mat();
            opencv_imgproc.findContours(edges.clone(), contours, hierarchy, 
                opencv_imgproc.RETR_EXTERNAL, opencv_imgproc.CHAIN_APPROX_SIMPLE);

            // Tạo ảnh debug cho contours
            Mat contourDebugMat = src.clone();
            
            // Tìm contour có diện tích lớn nhất
            double maxArea = 0;
            int maxAreaIdx = -1;
            for (int i = 0; i < contours.size(); i++) {
                Mat contour = contours.get(i);
                double area = opencv_imgproc.contourArea(contour);
                if (area > maxArea) {
                    maxArea = area;
                    maxAreaIdx = i;
                }
            }

            // Vẽ vùng bao của contour lớn nhất màu đỏ
            if (maxAreaIdx >= 0) {
                Mat largestContour = contours.get(maxAreaIdx);
                Mat hull = new Mat();
                opencv_imgproc.convexHull(largestContour, hull);
                
                MatVector hullContours = new MatVector(1);
                hullContours.put(0, hull);
                
                opencv_imgproc.drawContours(
                    contourDebugMat,
                    hullContours,
                    0,
                    new Scalar(0, 0, 255, 0) // BGR color (Red)
                );
            }

            // Lưu ảnh debug với contour lớn nhất
            String contourDebugFileName = imagePath + "_largest_contour_debug.png";
            opencv_imgcodecs.imwrite(Paths.get(uploadDir, contourDebugFileName).toString(), contourDebugMat);

            // Tiếp tục xử lý ảnh cho OCR như cũ (dùng ảnh gốc)
            BufferedImage originalImage = ImageIO.read(imageFile);
            if (originalImage == null) {
                throw new BaseException("Failed to read image file: ", imagePath, HttpStatus.BAD_REQUEST);
            }

            BufferedImage grayImage = new BufferedImage(originalImage.getWidth(), originalImage.getHeight(), BufferedImage.TYPE_BYTE_GRAY);
            grayImage.getGraphics().drawImage(originalImage, 0, 0, null);

            RescaleOp rescaleOp = new RescaleOp(1.5f, 0, null);
            BufferedImage contrastImage = rescaleOp.filter(grayImage, null);

            float[] sharpenKernel = {
                0.f, -1.f, 0.f,
                -1.f, 5.f, -1.f,
                0.f, -1.f, 0.f
            };
            ConvolveOp sharpenOp = new ConvolveOp(new Kernel(3, 3, sharpenKernel));
            BufferedImage sharpImage = sharpenOp.filter(contrastImage, null);

            String result = tesseract.doOCR(sharpImage);
            result = result.trim().replaceAll("\\s+", " ");

            return result;

        } catch (Exception e) {
            log.error("Error performing OCR on image: " + imagePath, e);
            throw new BaseException("Failed to perform OCR: ", e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }
}
