package com.ngv.aia_service.util.file;

import com.fasterxml.jackson.databind.node.ObjectNode;
import com.ngv.aia_service.util.json.JacksonUtil;
import com.ngv.aia_service.util.log.DebugLogger;
import org.apache.commons.lang3.exception.ExceptionUtils;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Base64;

public class FileUtil {
    public static String getString(String filePath, boolean relative) {
        String path = "";
        if(relative){
            path = System.getProperty("user.dir");
        }
        File file = new File(path + filePath);
        StringBuffer contents = new StringBuffer();
        BufferedReader reader = null;
        try {
            Reader r = new InputStreamReader( new FileInputStream(file), StandardCharsets.UTF_8);
            reader = new BufferedReader(r);
            String text;
            boolean firstLine = true;
            while (true) {
                text = reader.readLine();
                if(text != null){
                    if(firstLine){
                        firstLine = false;
                    }else{
                        contents.append(System.getProperty("line.separator"));
                    }
                    contents.append(text);
                }else{
                    break;
                }
            }
            return contents.toString();
        } catch (Exception e) {
            String stacktrace = ExceptionUtils.getStackTrace(e);
            DebugLogger.error(stacktrace);
            return null;
        }
    }
    public static ObjectNode getJsonObject(String filePath, boolean relative) {
        String data = getString(filePath, relative);
        return JacksonUtil.toJsonObject(data);
    }
    
    public static void writeStringToFile(String fileName, String data) {
        Path path = Paths.get(fileName);
        try{
            BufferedWriter writer = Files.newBufferedWriter(path, StandardCharsets.UTF_8);
            writer.append(data);
            writer.close();
        } catch (Exception e) {
            String stacktrace = ExceptionUtils.getStackTrace(e);
            DebugLogger.error(stacktrace);
        }
    }
    
    public static void writeJsonToFile(String fileName, ObjectNode data) {
        Path path = Paths.get(fileName);
        try {
            BufferedWriter writer = Files.newBufferedWriter(path, StandardCharsets.UTF_8);
            writer.write(JacksonUtil.toJsonString(data));
            writer.close();
        } catch (Exception e) {
            String stacktrace = ExceptionUtils.getStackTrace(e);
            DebugLogger.error(stacktrace);
        }
    }



        // Convert byte[] to Base64 String
        public static String encode(byte[] data) {
            return (data != null) ? Base64.getEncoder().encodeToString(data) : null;
        }

        // Convert Base64 String to byte[]
        public static byte[] decode(String base64String) {
            return (base64String != null && !base64String.isEmpty()) ? Base64.getDecoder().decode(base64String) : null;
        }

    public static File convertBase64ToFile(String base64String, String fileName) throws IOException {
        // Giải mã Base64 về byte array
        byte[] decodedBytes = Base64.getDecoder().decode(base64String);

        // Tạo file tạm với phần mở rộng từ tên file
        String fileExtension = getFileExtension(fileName); // Lấy đuôi file từ tên
        File tempFile = File.createTempFile("upload-", fileExtension);

        // Ghi dữ liệu vào file
        try (FileOutputStream fos = new FileOutputStream(tempFile)) {
            fos.write(decodedBytes);
        }

        return tempFile;
    }

    public static String getFileExtension(String fileName) {
        int lastDotIndex = fileName.lastIndexOf('.');
        return (lastDotIndex == -1) ? ".jpg" : fileName.substring(lastDotIndex);
    }




}
