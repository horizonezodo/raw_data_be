package com.ngv.aia_service.util.string;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Random;
import java.util.UUID;

public class StringUtil {
    public static String sha256(String src) {
        try {
            MessageDigest md = MessageDigest.getInstance("SHA-256");
            md.update(src.getBytes());
            byte byteData[] = md.digest();
            StringBuffer sb = new StringBuffer();
            for (int i = 0; i < byteData.length; i++) {
                sb.append(Integer.toString((byteData[i] & 0xff) + 0x100, 16).substring(1));
            }
            return sb.toString();
        } catch (NoSuchAlgorithmException e) {
            return "";
        }
    }
    private static Random random = new Random();
    private static final String charset = "0123456789abcdefghijklmnopqrstuvwxyz";
    public static String generateRandomStringNumberCharacter(int length){
        char[] buf = new char[length];
        for(int i = 0; i < length; i++){
            int index = random.nextInt(charset.length());
            buf[i] = charset.charAt(index);
        }
        return new String(buf);
    }
    
    private static String randomString(char from, char to, int length){
        int range = to - from;
        char[] buf = new char[length];
        for (int i = 0; i < buf.length; i++){
            int index = random.nextInt(range);
            buf[i] = (char) (from + index);
        }
        return new String(buf);
    }
    public static String randomNumberString(int length){
        Random random = new Random();
        StringBuilder sb = new StringBuilder(length);
        for (int i = 0; i < length; i++) {
            sb.append(random.nextInt(10)); // append a random digit between 0 and 9
        }
        String randomNumberString = sb.toString();
        return randomNumberString;
    }
    
    public static String randomString(int length) {
        return randomString('a', 'z', length);
    }
    public static String randomStringUpper(int length) {
        return randomString('A', 'Z', length);
    }
    public static String generateUUID(){
        return UUID.randomUUID().toString();
    }
}
