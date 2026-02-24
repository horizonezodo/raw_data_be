package com.naas.admin_service.features.common.controller;

import com.naas.admin_service.features.common.dto.PublicImageResponse;
import com.naas.admin_service.features.common.service.PublicService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import jakarta.servlet.http.HttpServletRequest;

@RequiredArgsConstructor
@RestController
@RequestMapping("/public")
public class PublicController {
    private final PublicService publicService;

    @Operation(summary = "Lấy ảnh công khai từ MinIO", description = "API lấy ảnh từ bucket 'epas', folder 'public' trong MinIO và trả về dưới dạng binary. "
            +
            "Hỗ trợ path có folder con. " +
            "Ví dụ: /public/images/logo.png hoặc /public/images/folder1/folder2/logo.png", responses = {
                    @ApiResponse(responseCode = "200", description = "Thành công, trả về ảnh"),
                    @ApiResponse(responseCode = "404", description = "Không tìm thấy ảnh")
            })
    @GetMapping("/images/**")
    public ResponseEntity<byte[]> getImage(HttpServletRequest request) {
        // Extract path after /images/
        // requestURI format: /api/public/images/folder1/folder2/logo.png
        String requestURI = request.getRequestURI();
        int imagesIndex = requestURI.indexOf("/images/");
        String imagePath = requestURI.substring(imagesIndex + "/images/".length());

        PublicImageResponse response = publicService.getImage(imagePath);
        return ResponseEntity.ok()
                .contentType(MediaType.parseMediaType(response.getContentType()))
                .body(response.getImageData());
    }
}
