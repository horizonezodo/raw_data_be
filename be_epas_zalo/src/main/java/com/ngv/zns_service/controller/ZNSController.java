package com.ngv.zns_service.controller;

import com.fasterxml.jackson.databind.node.ObjectNode;
import com.ngv.zns_service.exception.ValidationException;
import com.ngv.zns_service.model.ResponseData;
import com.ngv.zns_service.dto.response.imageStorage.ImageStorageDtoConvertJson;
import com.ngv.zns_service.service.ImageStorageService;
import com.ngv.zns_service.service.ZNSService;
import com.ngv.zns_service.util.http.ResponseUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.Map;
import java.util.Objects;

@RequiredArgsConstructor
@RestController
public class ZNSController {
    
    private final ZNSService znsService;
    private final ImageStorageService imageStorageService;
    
    @PostMapping("/template/create")
    public ResponseEntity<ResponseData<Object>> createTemplate(@RequestHeader String oaId, @RequestBody Map<String, Object> payload) {
        ObjectNode res = znsService.sendCreateTemplate(oaId, payload);
        return ResponseUtils.success(res.get("error").asInt(), res.get("message").asText(), res.get("data"));
    }
    
    @PostMapping("/template/edit")
    public ResponseEntity<ResponseData<Object>> editTemplate(@RequestHeader String oaId, @RequestBody Map<String, Object> payload) {
        ObjectNode res = znsService.sendEditTemplate(oaId, payload);
        return ResponseUtils.success(res.get("error").asInt(), res.get("message").asText(), res.get("data"));
    }
    
    @PostMapping("/upload/image")
    public ResponseEntity<ResponseData<Object>> uploadImage(@RequestHeader String oaId, @RequestParam("image") MultipartFile file) throws IOException {
        File tempFile = File.createTempFile("upload-", file.getOriginalFilename());
        file.transferTo(tempFile);
        
        ObjectNode res = znsService.sendUploadImage(oaId, tempFile.getAbsolutePath());
        
        tempFile.delete();
        
        return ResponseUtils.success(res.get("error").asInt(), res.get("message").asText(), res.get("data"));
    }
    
    @PostMapping("/message/template")
    public ResponseEntity<ResponseData<Object>> sendMessageTemplate(@RequestHeader String oaId, @RequestBody Map<String, Object> payload) throws ValidationException {
        ObjectNode res = znsService.sendTemplateMessage(oaId, payload);
        return ResponseUtils.success(res.get("error").asInt(), res.get("message").asText(), res.get("data"));
    }
    
    @GetMapping("/message/status")
    public ResponseEntity<ResponseData<Object>> getMessageStatus(@RequestHeader String oaId, @RequestParam String messageId) {
        ObjectNode res = znsService.sendGetZNSStatus(oaId, messageId);
        return ResponseUtils.success(res.get("error").asInt(), res.get("message").asText(), res.get("data"));
    }
    
    @GetMapping("/template/all")
    public ResponseEntity<ResponseData<Object>> getAllTemplate(@RequestHeader String oaId,
                                                               @RequestParam Integer offset,
                                                               @RequestParam Integer limit,
                                                               @RequestParam(required = false) Integer status) {
        ObjectNode res = znsService.sendGetTemplateList(oaId, offset, limit, Objects.isNull(status)? 0: status);
        return ResponseUtils.success(res.get("error").asInt(), res.get("message").asText(), res.get("data"));
    }
    
    @GetMapping("/template/info")
    public ResponseEntity<ResponseData<Object>> getTemplateInfo(@RequestHeader String oaId, @RequestParam String templateId) {
        ObjectNode res = znsService.sendGetTemplateInfo(oaId, templateId);
        return ResponseUtils.success(res.get("error").asInt(), res.get("message").asText(), res.get("data"));
    }
    
    @GetMapping("/template/rating")
    public ResponseEntity<ResponseData<Object>> getTemplateRating(@RequestHeader String oaId,
                                                                  @RequestParam String templateId,
                                                                  @RequestParam Long fromTime,
                                                                  @RequestParam Long toTime,
                                                                  @RequestParam Integer offset,
                                                                  @RequestParam Integer limit) {
        System.out.println(System.currentTimeMillis());
        ObjectNode res = znsService.sendGetRating(oaId, templateId, fromTime, toTime, offset, limit);
        return ResponseUtils.success(res.get("error").asInt(), "Success", res.get("data"));
    }

    @GetMapping("/template/detail")
    public ResponseEntity<?> detail(@RequestParam String id) {
        ImageStorageDtoConvertJson dto = imageStorageService.getDetail(id);
        return new ResponseEntity<>(dto, HttpStatus.OK);
    }

}
