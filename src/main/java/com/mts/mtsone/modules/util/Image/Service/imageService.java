package com.mts.mtsone.modules.util.Image.Service;

import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

public interface ImageService {
    //Lưu ảnh vào thư mục
    String saveImage(MultipartFile file, String subFolder) throws IOException;
    
    //Lấy ảnh ra bằng URL
    byte[] getImage(String imagePath) throws IOException;
    
    void deleteImage(String imagePath) throws IOException;
}
