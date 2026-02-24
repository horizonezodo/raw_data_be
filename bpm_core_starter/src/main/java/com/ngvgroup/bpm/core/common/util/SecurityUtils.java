package com.ngvgroup.bpm.core.common.util;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.Base64;

/**
 * Tiện ích bảo mật
 */
public class SecurityUtils {

    private static final String AES_ALGORITHM = "AES";
    private static final String SHA_256_ALGORITHM = "SHA-256";
    private static final String MD5_ALGORITHM = "MD5";
    private static final int AES_KEY_SIZE = 256;

    private SecurityUtils() {
        throw new IllegalStateException("Utility class");
    }

    /**
     * Tạo khóa AES
     */
    public static String generateAesKey() throws Exception {
        KeyGenerator keyGen = KeyGenerator.getInstance(AES_ALGORITHM);
        keyGen.init(AES_KEY_SIZE);
        SecretKey secretKey = keyGen.generateKey();
        return Base64.getEncoder().encodeToString(secretKey.getEncoded());
    }

    /**
     * Mã hóa AES
     */
    public static String encryptAes(String data, String key) throws Exception {
        if (data == null || key == null) {
            return null;
        }
        SecretKey secretKey = new SecretKeySpec(Base64.getDecoder().decode(key), AES_ALGORITHM);
        Cipher cipher = Cipher.getInstance(AES_ALGORITHM);
        cipher.init(Cipher.ENCRYPT_MODE, secretKey);
        byte[] encryptedBytes = cipher.doFinal(data.getBytes());
        return Base64.getEncoder().encodeToString(encryptedBytes);
    }

    /**
     * Giải mã AES
     */
    public static String decryptAes(String encryptedData, String key) throws Exception {
        if (encryptedData == null || key == null) {
            return null;
        }
        SecretKey secretKey = new SecretKeySpec(Base64.getDecoder().decode(key), AES_ALGORITHM);
        Cipher cipher = Cipher.getInstance(AES_ALGORITHM);
        cipher.init(Cipher.DECRYPT_MODE, secretKey);
        byte[] decryptedBytes = cipher.doFinal(Base64.getDecoder().decode(encryptedData));
        return new String(decryptedBytes);
    }

    /**
     * Mã hóa SHA-256
     */
    public static String encryptSha256(String data) throws NoSuchAlgorithmException {
        if (data == null) {
            return null;
        }
        MessageDigest digest = MessageDigest.getInstance(SHA_256_ALGORITHM);
        byte[] hash = digest.digest(data.getBytes(StandardCharsets.UTF_8));
        return Base64.getEncoder().encodeToString(hash);
    }

    /**
     * Mã hóa MD5
     */
    public static String encryptMd5(String data) throws NoSuchAlgorithmException {
        if (data == null) {
            return null;
        }
        MessageDigest digest = MessageDigest.getInstance(MD5_ALGORITHM);
        byte[] hash = digest.digest(data.getBytes(StandardCharsets.UTF_8));
        return Base64.getEncoder().encodeToString(hash);
    }

    /**
     * Tạo salt ngẫu nhiên
     */
    public static String generateSalt() {
        SecureRandom random = new SecureRandom();
        byte[] salt = new byte[16];
        random.nextBytes(salt);
        return Base64.getEncoder().encodeToString(salt);
    }

    /**
     * Mã hóa mật khẩu với salt
     */
    public static String hashPassword(String password, String salt) throws NoSuchAlgorithmException {
        if (password == null || salt == null) {
            return null;
        }
        String saltedPassword = password + salt;
        return encryptSha256(saltedPassword);
    }

    /**
     * Kiểm tra mật khẩu
     */
    public static boolean verifyPassword(String password, String salt, String hashedPassword) throws NoSuchAlgorithmException {
        if (password == null || salt == null || hashedPassword == null) {
            return false;
        }
        String newHashedPassword = hashPassword(password, salt);
        return newHashedPassword.equals(hashedPassword);
    }

    /**
     * Tạo token ngẫu nhiên
     */
    public static String generateToken() {
        SecureRandom random = new SecureRandom();
        byte[] tokenBytes = new byte[32];
        random.nextBytes(tokenBytes);
        return Base64.getUrlEncoder().withoutPadding().encodeToString(tokenBytes);
    }

    /**
     * Tạo mật khẩu ngẫu nhiên
     */
    public static String generatePassword(int length) {
        if (length < 8) {
            length = 8;
        }
        String chars = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789!@#$%^&*()_+";
        SecureRandom random = new SecureRandom();
        StringBuilder password = new StringBuilder();
        for (int i = 0; i < length; i++) {
            int index = random.nextInt(chars.length());
            password.append(chars.charAt(index));
        }
        return password.toString();
    }

    /**
     * Kiểm tra độ mạnh của mật khẩu
     */
    public static boolean isStrongPassword(String password) {
        if (password == null || password.length() < 8) {
            return false;
        }
        boolean hasUpperCase = false;
        boolean hasLowerCase = false;
        boolean hasDigit = false;
        boolean hasSpecialChar = false;
        for (char c : password.toCharArray()) {
            if (Character.isUpperCase(c)) {
                hasUpperCase = true;
            } else if (Character.isLowerCase(c)) {
                hasLowerCase = true;
            } else if (Character.isDigit(c)) {
                hasDigit = true;
            } else {
                hasSpecialChar = true;
            }
        }
        return hasUpperCase && hasLowerCase && hasDigit && hasSpecialChar;
    }

    /**
     * Mã hóa Base64
     */
    public static String encodeBase64(String data) {
        if (data == null) {
            return null;
        }
        return Base64.getEncoder().encodeToString(data.getBytes(StandardCharsets.UTF_8));
    }

    /**
     * Giải mã Base64
     */
    public static String decodeBase64(String data) {
        if (data == null) {
            return null;
        }
        return new String(Base64.getDecoder().decode(data), StandardCharsets.UTF_8);
    }

    /**
     * Mã hóa URL-safe Base64
     */
    public static String encodeUrlSafeBase64(String data) {
        if (data == null) {
            return null;
        }
        return Base64.getUrlEncoder().withoutPadding().encodeToString(data.getBytes(StandardCharsets.UTF_8));
    }

    /**
     * Giải mã URL-safe Base64
     */
    public static String decodeUrlSafeBase64(String data) {
        if (data == null) {
            return null;
        }
        return new String(Base64.getUrlDecoder().decode(data), StandardCharsets.UTF_8);
    }
} 