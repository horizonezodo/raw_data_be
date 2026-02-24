package com.naas.admin_service.features.auth.controller;

import com.naas.admin_service.features.auth.dto.CaptchaResponseDto;
import com.naas.admin_service.features.auth.service.CaptchaService;
import com.ngvgroup.bpm.core.common.dto.ResponseData;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;

@RestController
@RequestMapping("/captcha")
@RequiredArgsConstructor
public class CaptchaController {
    private final CaptchaService captchaService;

    @Operation(summary = "Lấy ảnh captcha", description = "API trả về ảnh captcha base64 và token stateless")
    @ApiResponse(responseCode = "200", description = "Thành công, trả về ảnh captcha và token")
    @ApiResponse(responseCode = "500", description = "Lỗi khi tạo ảnh captcha")
    @GetMapping(value = "/image")
    public ResponseEntity<CaptchaResponseDto> getCaptchaImage() throws IOException {
        return ResponseEntity.ok(captchaService.generateCaptcha());
    }

    @Operation(summary = "Verify captcha", description = "API kiểm tra mã captcha người dùng nhập vào (stateless)")
    @ApiResponse(responseCode = "200", description = "Thành công, trả về true nếu hợp lệ, false nếu không hợp lệ")
    @GetMapping("/verify")
    public ResponseEntity<ResponseData<Boolean>> verifyCaptcha(@RequestParam String code, @RequestParam String token) {
        boolean valid = captchaService.verifyCaptcha(code, token);
        return ResponseData.okEntity(valid);
    }
}
