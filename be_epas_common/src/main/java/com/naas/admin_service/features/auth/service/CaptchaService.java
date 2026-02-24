package com.naas.admin_service.features.auth.service;

import java.awt.image.BufferedImage;
import java.io.IOException;

import com.naas.admin_service.features.auth.dto.CaptchaResponseDto;

public interface CaptchaService {
    CaptchaResponseDto generateCaptcha() throws IOException;

    String generateCaptchaText();

    BufferedImage generateCaptchaImage(String text);

    boolean verifyCaptcha(String code, String token);

    boolean isCaptchaEnabled();
}
