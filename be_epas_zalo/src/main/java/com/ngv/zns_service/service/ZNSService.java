package com.ngv.zns_service.service;

import com.fasterxml.jackson.databind.node.ObjectNode;
import com.ngv.zns_service.exception.ValidationException;

import java.util.Map;

public interface ZNSService {
    ObjectNode sendCreateTemplate(String oaId, Map<String, Object> payload);
    ObjectNode sendEditTemplate(String oaId, Map<String, Object> payload);
    ObjectNode sendUploadImage(String oaId, String filePath);
    ObjectNode sendTemplateMessage(String oaId, Map<String, Object> payload) throws ValidationException;
    ObjectNode sendGetZNSStatus(String oaId, String message_id);
    ObjectNode sendGetTemplateList(String oaId, int offset, int limit, int status);
    ObjectNode sendGetTemplateInfo(String oaId, String templateId);
    ObjectNode sendGetRating(String oaId, String templateId, Long fromTime, Long toTime, int offset, int limit);

    // Lấy thông tin quota ZNS
    ObjectNode getInformQuotaZns(String oaId);

    // Lấy thông tin loại nội dung ZNS được phép gửi
    ObjectNode getZnsContentValid(String oaId);

}
