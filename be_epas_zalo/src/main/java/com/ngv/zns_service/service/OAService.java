package com.ngv.zns_service.service;


import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.springframework.web.multipart.MultipartFile;

import java.util.Map;

public interface OAService {
    ObjectNode sendMessage(String accessToken, Map<String, Object> recipient, Map<String, Object> message);
    ObjectNode sendGetUserList(String accessToken, JsonNode params);
    ObjectNode sendGetUserDetail(String accessToken, JsonNode params);
    ObjectNode sendUploadImage(String accessToken, MultipartFile file);
    ObjectNode getOaInfo(String accessToken);
}
