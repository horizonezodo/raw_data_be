package com.naas.admin_service.features.auth.service.impl;

import com.naas.admin_service.features.auth.dto.CaptchaResponseDto;
import com.naas.admin_service.features.auth.service.CaptchaService;
import com.naas.admin_service.features.setting.dto.ComCfgSettingReqDto;
import com.naas.admin_service.features.setting.model.ComCfgSetting;
import com.naas.admin_service.features.setting.repository.ComCfgSettingRepository;
import com.naas.admin_service.features.setting.service.ComCfgSettingService;
import com.naas.admin_service.core.contants.SettingCode;
import com.naas.admin_service.core.utils.AesEncryptionUtil;
import com.naas.admin_service.core.utils.CaptchaImageUtil;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.security.SecureRandom;
import java.util.Base64;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class CaptchaServiceImpl implements CaptchaService {
    private int lengthDeclare;
    private String charString;
    private String caseSensitiveSetting;
    private final ComCfgSettingService comCfgSettingService;
    private final ComCfgSettingRepository comCfgSettingRepository;
    private final AesEncryptionUtil aesEncryptionUtil;

    private void loadCaptchaConfig() {
        List<ComCfgSettingReqDto> settings = comCfgSettingService.getCaptchaSettings();

        // Giá trị mặc định
        int defaultLength = SettingCode.CaptchaDefault.LENGTH.intValue();
        String defaultDataType = SettingCode.CaptchaDefault.DATA_TYPE;
        String defaultCaseSensitive = SettingCode.CaptchaDefault.CASE_SENSITIVE;

        int length = defaultLength;
        String dataType = defaultDataType;
        String caseSensitive = defaultCaseSensitive;

        for (ComCfgSettingReqDto setting : settings) {
            String code = setting.getSettingCode();
            String value = setting.getSettingValue();

            if (SettingCode.CAPTCHA.LENGTH.equals(code)) {
                try {
                    length = Integer.parseInt(value);
                } catch (NumberFormatException ignored) {
                    // giữ nguyên default
                }
            } else if (SettingCode.CAPTCHA.DATA_TYPE.equals(code)) {
                dataType = value;
            } else if (SettingCode.CAPTCHA.CASE_SENSITIVE.equals(code)) {
                caseSensitive = value;
            }
        }

        // Định nghĩa các bộ ký tự
        final String NUMBERS = "0123456789";
        final String UPPERCASE = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
        final String LOWERCASE = "abcdefghijklmnopqrstuvwxyz";

        StringBuilder charSet = new StringBuilder();

        boolean useLetters = SettingCode.CaptchaComparison.ALPHA.equalsIgnoreCase(dataType)
                || SettingCode.CaptchaComparison.MIX.equalsIgnoreCase(dataType);
        boolean useNumbers = SettingCode.CaptchaComparison.MIX.equalsIgnoreCase(dataType)
                || SettingCode.CaptchaComparison.NUMBERIC.equalsIgnoreCase(dataType);

        if (useNumbers) {
            charSet.append(NUMBERS);
        }

        if (useLetters) {
            switch (caseSensitive.toLowerCase()) {
                case "uppercase" -> charSet.append(UPPERCASE);
                case "lowercase" -> charSet.append(LOWERCASE);
                default -> charSet.append(UPPERCASE).append(LOWERCASE);
            }
        }

        this.lengthDeclare = length;
        this.caseSensitiveSetting = caseSensitive;
        this.charString = charSet.toString();
    }

    @Override
    public CaptchaResponseDto generateCaptcha() throws IOException {
        String capText = generateCaptchaText();
        BufferedImage bi = generateCaptchaImage(capText);

        // Encode ảnh sang base64
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        ImageIO.write(bi, "png", baos);
        String base64Image = Base64.getEncoder().encodeToString(baos.toByteArray());

        // Sinh token chứa mã captcha và thời gian hết hạn (3 phút)
        long expire = System.currentTimeMillis() + 3 * 60 * 1000;
        String raw = capText + "|" + expire;
        String token = aesEncryptionUtil.encrypt(raw);

        return new CaptchaResponseDto(base64Image, token);
    }

    @Override
    public String generateCaptchaText() {
        loadCaptchaConfig(); // luôn load lại config mới nhất
        SecureRandom random = new SecureRandom();
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < lengthDeclare; i++) {
            sb.append(charString.charAt(random.nextInt(charString.length())));
        }
        return sb.toString();
    }

    @Override
    public BufferedImage generateCaptchaImage(String text) {
        loadCaptchaConfig(); // luôn load lại config mới nhất
        int width = 160;
        int height = 40;
        return CaptchaImageUtil.createCaptchaImage(text, width, height);
    }

    @Override
    public boolean verifyCaptcha(String code, String token) {
        try {
            String decrypted = aesEncryptionUtil.decrypt(token);
            String[] parts = decrypted.split("\\|");
            if (parts.length != 2)
                return false;
            String captcha = parts[0];
            long expire = Long.parseLong(parts[1]);
            if (System.currentTimeMillis() > expire)
                return false;

            if (SettingCode.CaptchaComparison.UPPERCASE.equalsIgnoreCase(caseSensitiveSetting)) {
                return captcha.equalsIgnoreCase(code);
            } else if (SettingCode.CaptchaComparison.LOWERCASE.equalsIgnoreCase(caseSensitiveSetting)) {
                return captcha.equalsIgnoreCase(code);
            } else if (SettingCode.CaptchaComparison.MIX.equalsIgnoreCase(caseSensitiveSetting)) {
                return captcha.equals(code);
            } else {
                return captcha.equalsIgnoreCase(code);
            }
        } catch (Exception e) {
            return false;
        }
    }

    @Override
    public boolean isCaptchaEnabled() {
        Optional<ComCfgSetting> isApplySetting = comCfgSettingRepository
                .findBySettingCode(SettingCode.CAPTCHA.IS_APPLY);

        return isApplySetting
                .map(setting -> Boolean.parseBoolean(setting.getSettingValue()))
                .orElse(false);
    }
}
