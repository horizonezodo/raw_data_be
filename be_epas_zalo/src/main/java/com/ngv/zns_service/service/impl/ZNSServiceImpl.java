package com.ngv.zns_service.service.impl;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ObjectNode;

import com.ngv.zns_service.constant.ErrorMessages;
import com.ngv.zns_service.exception.ValidationException;
import com.ngv.zns_service.model.entity.ZSSNDung;
import com.ngv.zns_service.repository.ZSSMauZNZRepository;
import com.ngv.zns_service.repository.ZSSNDungRepository;
import com.ngv.zns_service.repository.ZssTKhoanZoaRepository;
import com.ngv.zns_service.service.ZNSService;
import com.ngv.zns_service.service.ZssTKhoanZoaService;

import com.ngv.zns_service.util.date.DateTimeConverter;
import com.ngv.zns_service.util.json.JacksonUtil;
import com.ngvgroup.bpm.core.persistence.service.storedprocedure.BaseStoredProcedureService;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.FileSystemResource;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.annotation.Isolation;


import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;


@RequiredArgsConstructor
@Service
public class ZNSServiceImpl extends BaseStoredProcedureService implements ZNSService {

    private final WebClient webClient;
    
    private final DateTimeConverter dateTimeConverter;
    private final ZssTKhoanZoaService znsAuthService;
    private final ZSSNDungRepository zssnDungRepository;
    private final ZSSMauZNZRepository zssMauZNZRepository;
    private final ZssTKhoanZoaRepository zssTKhoanZoaRepository;
    @Value("${zns.zns_uri}")
    private String znsUri;

    @Override
    public ObjectNode sendCreateTemplate(String oaId, Map<String, Object> payload) {
        String res = invokeCreateZnsTemplate(znsAuthService.getTokenInfo(oaId).getAccessToken(), payload);
        return JacksonUtil.toJsonObject(res);
    }

    @Override
    public ObjectNode sendEditTemplate(String oaId, Map<String, Object> payload) {
        String res = invokeEditZnsTemplate(znsAuthService.getTokenInfo(oaId).getAccessToken(), payload);
        return JacksonUtil.toJsonObject(res);
    }

    @Override
    public ObjectNode sendUploadImage(String oaId, String filePath) {
        String res = invokeUploadImage(znsAuthService.getTokenInfo(oaId).getAccessToken(), filePath);
        return JacksonUtil.toJsonObject(res);
    }

    @Override
    @Transactional(isolation = Isolation.SERIALIZABLE)
    public ObjectNode sendTemplateMessage(String oaId, Map<String, Object> payload) throws ValidationException {
        String res = invokeSendTemplateMessage(znsAuthService.getTokenInfo(oaId).getAccessToken(), payload);
        ObjectNode response = JacksonUtil.toJsonObject(res);
        String username = getCurrentUserName();
        String dateTimeNow = dateTimeConverter.dateTimeNow();

        String templateId = payload.get("template_id") != null ? payload.get("template_id").toString() : null;
        ZSSNDung entity = new ZSSNDung();
        entity.setMaNdung(generateMaNdung());

        zssMauZNZRepository.findMaMauByMaMauDtacAndZoaId(templateId, oaId)
                .ifPresent(entity::setMaMau);

        String maDvi = zssTKhoanZoaRepository.findMaDviByZoaId(oaId);
        if (maDvi == null) {
            throw new ValidationException("MaDvi không tìm thấy cho ZOA ID: " + oaId, ErrorMessages.NOT_FOUND);
        }
        entity.setMaDvi(maDvi);

        entity.setZoaId(oaId);
        entity.setMaMauDtac(templateId);
        entity.setTgianGui(dateTimeNow);
        entity.setNdung(payload.get("ndung") != null ? payload.get("ndung").toString() : null);
        entity.setMaDvu(payload.get("ma_dvu") != null ? payload.get("ma_dvu").toString() : null);
        entity.setTaiKhoanZalo(payload.get("phone") != null ? payload.get("phone").toString() : null);
        entity.setTthaiNvu(payload.get("tthai_nvu") != null ? payload.get("tthai_nvu").toString() : null);
        entity.setTgianTao(payload.get("tgian_tao") != null ? payload.get("tgian_tao").toString() : null);
        entity.setMaKhhang(payload.get("ma_khhang") != null ? payload.get("ma_khhang").toString() : null);
        entity.setTenKhhang(payload.get("ten_khhang") != null ? payload.get("ten_khhang").toString() : null);
        entity.setJsonTso(payload.get("template_data") != null ? payload.get("template_data").toString() : null);
        entity.setTgianGuiDukien(payload.get("tgian_gui_dukien") != null ? payload.get("tgian_gui_dukien").toString() : null);

        entity.setNguoiNhap(username);
        entity.setNguoiSua(username);
        entity.setNgayNhap(dateTimeNow);
        entity.setNgaySua(dateTimeNow);

        if (response.has("error") && response.get("error").asInt() == 0) {
            JsonNode dataNode = response.get("data");
            if (dataNode != null && dataNode.isObject()) {
                entity.setIdMessage(dataNode.get("msg_id") != null ? dataNode.get("msg_id").asText() : null);
                entity.setTthaiGuizns("20");
            }
        } else {
            entity.setTthaiGuizns("21");
        }

        zssnDungRepository.save(entity);

        return response;
    }

    public String generateMaNdung() {
        String datePrefix = new SimpleDateFormat("yyMMdd").format(new Date());

        int count = zssnDungRepository.countByMaNdungStartingWith(datePrefix);

        int nextNumber = count + 1;

        String suffix = String.format("%06d", nextNumber);

        return datePrefix + suffix;
    }

    @Override
    public ObjectNode sendGetZNSStatus(String oaId, String messageId) {
        String res = invokeGetMessageStatus(znsAuthService.getTokenInfo(oaId).getAccessToken(), messageId);
        return JacksonUtil.toJsonObject(res);
    }

    @Override
    public ObjectNode sendGetTemplateList(String oaId, int offset, int limit, int status) {
        String res = invokeGetTemplateList(znsAuthService.getTokenInfo(oaId).getAccessToken(), offset, limit, status);
        return JacksonUtil.toJsonObject(res);
    }

    @Override
    public ObjectNode sendGetTemplateInfo(String oaId, String templateId) {
        String res = invokeGetTemplateInfo(znsAuthService.getTokenInfo(oaId).getAccessToken(), templateId);
        return JacksonUtil.toJsonObject(res);
    }

    @Override
    public ObjectNode sendGetRating(String oaId, String templateId, Long fromTime, Long toTime, int offset, int limit) {
        String res = invokeGetRatings(znsAuthService.getTokenInfo(oaId).getAccessToken(), templateId, fromTime, toTime, offset, limit);
        return JacksonUtil.toJsonObject(res);
    }

    //Create template
    private String invokeCreateZnsTemplate(String accessToken, Map<String, Object> payload) {
        String apiUrl = znsUri + "/template/create";

        return webClient.post()
                .uri(apiUrl)
                .header("Content-Type", "application/json")
                .header("access_token", accessToken)
                .bodyValue(payload)
                .retrieve()
                .bodyToMono(String.class)
                .block();
    }

    //Edit template
    private String invokeEditZnsTemplate(String accessToken, Map<String, Object> payload) {
        String apiUrl = znsUri + "/template/edit";

        return webClient.post()
                .uri(apiUrl)
                .header("Content-Type", "application/json")
                .header("access_token", accessToken)
                .bodyValue(payload)
                .retrieve()
                .bodyToMono(String.class)
                .block();
    }

    //Upload image
    private String invokeUploadImage(String accessToken, String filePath) {
        String apiUrl = znsUri + "/upload/image";

        FileSystemResource file = new FileSystemResource(filePath);

        MultiValueMap<String, Object> formData = new LinkedMultiValueMap<>();
        formData.add("file", file);

        // Gửi yêu cầu POST với multipart/form-data
        return webClient.post()
                .uri(apiUrl)
                .header("access_token", accessToken)
                .contentType(MediaType.MULTIPART_FORM_DATA)
                .bodyValue(formData)
                .retrieve()
                .bodyToMono(String.class) // Nhận kết quả trả về dưới dạng chuỗi
                .block(); // Chờ kết quả đồng bộ
    }


    //Gửi ZNS
    private String invokeSendTemplateMessage(String accessToken, Map<String, Object> payload) {
        String apiUrl = znsUri + "/message/template";

        return webClient.post()
                .uri(apiUrl)
                .header("Content-Type", "application/json")
                .header("access_token", accessToken)
                .bodyValue(payload)
                .retrieve()
                .bodyToMono(String.class)
                .block();
    }

    //Lấy thông tin trạng thái ZNS
    public String invokeGetMessageStatus(String accessToken, String messageId) {
        String apiUrl = znsUri + "/message/status?message_id=" + messageId;
        return webClient.get()
                .uri(apiUrl)
                .header("Content-Type", "application/json")
                .header("access_token", accessToken)
                .retrieve()
                .bodyToMono(String.class)
                .block();
    }

    // Lấy template list ZNS
    public String invokeGetTemplateList(String accessToken, int offset, int limit, int status) {
        String apiUrl = znsUri + "/template/all?offset=" + offset + "&limit=" + limit;
        if (status == 0) {
            return webClient.get()
                    .uri(apiUrl)
                    .header("Content-Type", "application/json")
                    .header("access_token", accessToken)
                    .retrieve()
                    .bodyToMono(String.class)
                    .block();
        } else {
            apiUrl = apiUrl + "&status=" + status;
            return webClient.get()
                    .uri(apiUrl)
                    .header("Content-Type", "application/json")
                    .header("access_token", accessToken)
                    .retrieve()
                    .bodyToMono(String.class)
                    .block();
        }
    }

    // Lấy thông tin chi tiết template ZNS
    public String invokeGetTemplateInfo(String accessToken, String templateId) {
        String apiUrl = znsUri + "/template/info/v2?template_id=" + templateId;
        return webClient.get()
                .uri(apiUrl)
                .header("Content-Type", "application/json")
                .header("access_token", accessToken)
                .retrieve()
                .bodyToMono(String.class)
                .block();
    }

    //Lấy thông tin đánh giá của khách hàng
    public String invokeGetRatings(String accessToken, String templateId, long fromTime, long toTime, int offset, int limit) {
        String apiUrl = znsUri + "/rating/get?template_id=" + templateId + "&from_time=" + fromTime + "&to_time=" + toTime + "&offset=" + offset + "&limit=" + limit;
        return webClient.get()
                .uri(apiUrl)
                .header("Content-Type", "application/json")
                .header("access_token", accessToken)
                .retrieve()
                .bodyToMono(String.class)
                .block();
    }

    // Lấy thông tin loại nội dung ZNS được phép gửi
    @Override
    public ObjectNode getZnsContentValid(String oaId) {
        String res = invokeZnsContentValid(znsAuthService.getTokenInfo(oaId).getAccessToken());
        return JacksonUtil.toJsonObject(res);
    }

    private String invokeZnsContentValid(String accessToken) {
        String apiUrl = znsUri + "/message/template-tag";

        return webClient.get()
                .uri(apiUrl)
                .header("Content-Type", "application/json")
                .header("access_token", accessToken)
                .retrieve()
                .bodyToMono(String.class)
                .block();
    }

    // Lấy thông tin quota ZNS
    @Override
    public ObjectNode getInformQuotaZns(String oaId) {
        String res = invokeGetInformQuotaZns(znsAuthService.getTokenInfo(oaId).getAccessToken());
        return JacksonUtil.toJsonObject(res);
    }

    private String invokeGetInformQuotaZns(String accessToken) {
        String apiUrl = znsUri + "/message/quota";

        return webClient.get()
                .uri(apiUrl)
                .header("Content-Type", "application/json")
                .header("access_token", accessToken)
                .retrieve()
                .bodyToMono(String.class)
                .block();
    }
}
