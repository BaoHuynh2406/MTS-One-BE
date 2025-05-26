package com.mts.mtsone.modules.util.Image.controller;

import com.mts.mtsone.modules.util.Image.Service.ImageService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/api/v1/images")
@RequiredArgsConstructor
@Tag(name = "Image Management", description = "API quản lý hình ảnh")
public class ImageController {

    private final ImageService imageService;

    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @Operation(summary = "Upload hình ảnh")
    public ResponseEntity<String> uploadImage(
            @RequestParam("file") MultipartFile file,
            @RequestParam(value = "subFolder", defaultValue = "") String subFolder
    ) {
        try {
            String imagePath = imageService.saveImage(file, subFolder);
            return ResponseEntity.ok(imagePath);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @GetMapping("/{subFolder}/{fileName}")
    @Operation(summary = "Lấy hình ảnh")
    public ResponseEntity<byte[]> getImage(
            @PathVariable String subFolder,
            @PathVariable String fileName
    ) {
        try {
            byte[] imageBytes = imageService.getImage(subFolder + "/" + fileName);
            return ResponseEntity.ok()
                    .contentType(MediaType.IMAGE_JPEG)
                    .body(imageBytes);
        } catch (Exception e) {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping
    @Operation(summary = "Lấy hình ảnh bằng đường dẫn")
    public ResponseEntity<byte[]> getImageByPath(@RequestParam("path") String imagePath) {
        try {
            byte[] imageBytes = imageService.getImage(imagePath);
            return ResponseEntity.ok()
                    .contentType(MediaType.IMAGE_JPEG)
                    .body(imageBytes);
        } catch (Exception e) {
            return ResponseEntity.notFound().build();
        }
    }
}