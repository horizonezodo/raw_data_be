package com.naas.admin_service.core.utils;

import com.naas.admin_service.core.contants.CommonErrorCode;
import com.ngvgroup.bpm.core.common.exception.BusinessException;
import jakarta.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.crypto.Cipher;
import javax.crypto.spec.GCMParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.security.SecureRandom;
import java.util.Base64;

@Component
@Slf4j
public class AesEncryptionUtil {
    private static final String AES = "AES";
    private static final String AES_MODE = "AES/GCM/NoPadding";
    private static final int IV_SIZE = 12;
    private static final int TAG_LENGTH_BIT = 128;

    @Value("${aes.key}")
    private String aesKey;

    private SecretKeySpec secretKeySpec;

    @PostConstruct
    public void init() {
        byte[] keyBytes = aesKey.getBytes();
        secretKeySpec = new SecretKeySpec(keyBytes, AES);
    }

    // Mã hóa plaintext, trả về Base64 chứa IV + ciphertext

    public String encrypt(String plaintext) {
        try {
            byte[] iv = new byte[IV_SIZE];
            SecureRandom random = new SecureRandom();
            random.nextBytes(iv);

            Cipher cipher = Cipher.getInstance(AES_MODE);
            GCMParameterSpec spec = new GCMParameterSpec(TAG_LENGTH_BIT, iv);
            cipher.init(Cipher.ENCRYPT_MODE, secretKeySpec, spec);

            byte[] encrypted = cipher.doFinal(plaintext.getBytes(StandardCharsets.UTF_8));

            // Kết hợp IV và ciphertext
            byte[] encryptedIvAndText = new byte[IV_SIZE + encrypted.length];
            System.arraycopy(iv, 0, encryptedIvAndText, 0, IV_SIZE);
            System.arraycopy(encrypted, 0, encryptedIvAndText, IV_SIZE, encrypted.length);
            return Base64.getEncoder().encodeToString(encryptedIvAndText);

        } catch (Exception e) {
            log.error("Some error during encrypt password: {}",  e.getMessage());
            throw new BusinessException(CommonErrorCode.INTERNAL_SERVER_ERROR);
        }
    }

    // Giải mã Base64 chứa IV + ciphertext, trả về plaintext
    public String decrypt(String encryptedIvTextBase64) {
        try {
            byte[] encryptedIvTextBytes = Base64.getDecoder().decode(encryptedIvTextBase64);

            byte[] iv = new byte[IV_SIZE];
            System.arraycopy(encryptedIvTextBytes, 0, iv, 0, IV_SIZE);

            int encryptedSize = encryptedIvTextBytes.length - IV_SIZE;
            byte[] encryptedBytes = new byte[encryptedSize];
            System.arraycopy(encryptedIvTextBytes, IV_SIZE, encryptedBytes, 0, encryptedSize);

            Cipher cipher = Cipher.getInstance(AES_MODE);
            GCMParameterSpec spec = new GCMParameterSpec(TAG_LENGTH_BIT, iv);
            cipher.init(Cipher.DECRYPT_MODE, secretKeySpec, spec);

            byte[] decrypted = cipher.doFinal(encryptedBytes);
            return new String(decrypted, StandardCharsets.UTF_8);
        } catch (Exception e) {
            log.error("Some error during decrypt password: {}",  e.getMessage());
            throw new BusinessException(CommonErrorCode.INTERNAL_SERVER_ERROR);
        }

    }
}
