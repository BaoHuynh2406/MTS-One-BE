package com.mts.mtsone.modules.util.OCR.controller;

import com.mts.mtsone.common.response.ApiResponse;
import com.mts.mtsone.modules.util.OCR.service.OcrService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/ocr")
@RequiredArgsConstructor
@Tag(name = "OCR Management", description = "API xử lý OCR")
public class OcrController {

    private final OcrService ocrService;

    @GetMapping("/read")
    @Operation(summary = "Đọc text từ ảnh")
    public ResponseEntity<ApiResponse<String>> readTextFromImage(@RequestParam("imagePath") String imagePath) {
        try {
            String result = ocrService.performOcr(imagePath);
            return ResponseEntity.ok(ApiResponse.success("OCR thành công", result));
        } catch (Exception e) {
            return ResponseEntity.badRequest()
                .body(ApiResponse.error(e.getMessage()));
        }
    }
}
