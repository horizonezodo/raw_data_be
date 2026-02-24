package com.ngv.zns_service.controller;

import com.fasterxml.jackson.databind.node.ObjectNode;
import com.ngv.zns_service.model.ResponseData;
import com.ngv.zns_service.service.OAService;
import com.ngv.zns_service.util.http.ResponseUtils;
import com.ngv.zns_service.util.json.JacksonUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.Map;


@RequiredArgsConstructor
@RestController
@RequestMapping("/oa")
public class OAController {
    
    private final OAService oaService;
    
    @PostMapping("/message/cs")
    public ResponseEntity<ResponseData<Object>> editTemplate(@RequestHeader String oaId,@RequestBody Map<String, Object> payload) {
        ObjectNode res = oaService.sendMessage(oaId, (Map<String, Object>) payload.get("recipient"), (Map<String, Object>) payload.get("message"));
        return ResponseUtils.success(res.get("error").asInt(), res.get("message").asText(), res.get("data"));
    }
    
    @GetMapping("/user/getlist")
    public ResponseEntity<ResponseData<Object>> getUserList(@RequestHeader String oaId,@RequestParam String params) {
        ObjectNode res = oaService.sendGetUserList(oaId, JacksonUtil.toJsonObject(params));
        return ResponseUtils.success(res.get("error").asInt(), res.get("message").asText(), res.get("data"));
    }
    
    @GetMapping("/user/detail")
    public ResponseEntity<ResponseData<Object>> getUserDetail(@RequestHeader String oaId,@RequestParam String params) {
        ObjectNode res = oaService.sendGetUserDetail(oaId, JacksonUtil.toJsonObject(params));
        return ResponseUtils.success(res.get("error").asInt(), res.get("message").asText(), res.get("data"));
    }
    
    @PostMapping("/upload/image")
    public ResponseEntity<ResponseData<Object>> uploadImage(@RequestHeader String oaId, @RequestBody MultipartFile file) {
        ObjectNode res = oaService.sendUploadImage(oaId, file);
        return ResponseUtils.success(res.get("error").asInt(), res.get("message").asText(), res.get("data"));
    }
}
