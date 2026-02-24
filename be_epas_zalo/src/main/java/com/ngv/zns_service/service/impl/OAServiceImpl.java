package com.ngv.zns_service.service.impl;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.ngv.zns_service.service.OAService;
import com.ngv.zns_service.util.json.JacksonUtil;
import com.ngv.zns_service.util.log.DebugLogger;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.*;

@RequiredArgsConstructor
@Service
public class OAServiceImpl implements OAService {
    
    @Value("${zns.oa_uri}")
    private String znsUri;
    
    private final WebClient webClient;
    
    @Override
    public ObjectNode sendMessage(String accessToken, Map<String, Object> recipient, Map<String, Object> message) {
        DebugLogger.info("Send to '{}' text message: '{}'", recipient.get("user_id"), message);
        return invokeSendMessage(accessToken, recipient, message);
    }
    
    @Override
    public ObjectNode sendGetUserList(String accessToken, JsonNode params) {
        DebugLogger.info("Send get list users");
        String res = invokeGetUserList(accessToken, params);
        return JacksonUtil.toJsonObject(res);
    }
    
    @Override
    public ObjectNode sendGetUserDetail(String accessToken, JsonNode params) {
        DebugLogger.info("Send get user detail: '{}'", params.get("user_id"));
        String res = invokeGetUserDetail(accessToken, params);
        return JacksonUtil.toJsonObject(res);
    }
    
    @Override
    public ObjectNode sendUploadImage(String accessToken, MultipartFile file) {
        DebugLogger.info("Send upload image");
        String res = invokeUploadImage(accessToken, file);
        return JacksonUtil.toJsonObject(res);
    }

    @Override
    public ObjectNode getOaInfo(String accessToken) {
        DebugLogger.info("Send get oa");
        String res = invokeGetOA(accessToken);
        return JacksonUtil.toJsonObject(res);
    }


    private String invokePostApi(String accessToken, String apiUrl, Map<String, Object> body) {
        return webClient.post()
            .uri(apiUrl)
            .header("Content-Type", "application/json")
            .header("access_token", accessToken)
            .bodyValue(body)
            .retrieve()
            .bodyToMono(String.class)
            .block();
    }
    
    //Gửi tin tư vấn
    private ObjectNode invokeSendMessage(String accessToken, Map<String, Object> recipient, Map<String, Object> message) {
        String apiUrl = znsUri + "/v3.0/oa/message/cs";
        
        Map<String, Object> body = new HashMap<>();
        
        body.put("recipient", recipient);
        body.put("message", message);
        String res = invokePostApi(accessToken, apiUrl, body);
        return JacksonUtil.toJsonObject(res);
    }
    
    
    //Truy xuất danh sách người dùng
    private String invokeGetUserList(String accessToken, JsonNode params) {
        String apiUrl = znsUri + "/v3.0/oa/user/getlist";
        
        String dataParam = JacksonUtil.toJsonString(params);
        
        return webClient.get()
            .uri(uriBuilder -> uriBuilder
                .path(apiUrl)
                .queryParam("data", dataParam)
                .build())
            .header("access_token", accessToken)
            .retrieve()
            .bodyToMono(String.class)
            .block();
    }
    
    //Truy xuất chi tiết người dùng
    private String invokeGetUserDetail(String accessToken, JsonNode params) {
        String apiUrl = znsUri + "/v3.0/oa/user/detail";
        
        // Chuyển Map thành chuỗi JSON
        String dataParam = JacksonUtil.toJsonString(params);
        
        // Gửi yêu cầu GET
        return webClient.get()
            .uri(uriBuilder -> uriBuilder
                .path(apiUrl)
                .queryParam("data", dataParam)
                .build())
            .header("access_token", accessToken)
            .retrieve()
            .bodyToMono(String.class)
            .block();
        
    }
    
    //Upload hình ảnh
    private String invokeUploadImage(String accessToken, MultipartFile file) {
        String apiUrl = znsUri + "/v2.0/oa/upload/image";
        
        return webClient.post()
            .uri(apiUrl)
            .header("access_token", accessToken)
            .contentType(MediaType.MULTIPART_FORM_DATA)
            .body(BodyInserters.fromMultipartData("file", file.getResource()))
            .retrieve()
            .bodyToMono(String.class)
            .block();
    }

    //Truy xuất thông tin OA
    private String invokeGetOA(String accessToken) {
        String apiUrl = znsUri + "/v2.0/oa/getoa";

        // Gửi yêu cầu GET
        return webClient.get()
                .uri(apiUrl)
                .header("access_token", accessToken)
                .retrieve()
                .bodyToMono(String.class)
                .block();

    }

    ////////////////////////////////////////////////////////////////////////////////////////////
    
    //Upload file
    private String uploadFile(String accessToken, MultipartFile file) {
        String apiUrl = znsUri + "/v2.0/oa/upload/file";
        
        return webClient.post()
            .uri(apiUrl)
            .header("access_token", accessToken)
            .contentType(MediaType.MULTIPART_FORM_DATA)
            .body(BodyInserters.fromMultipartData("file", file.getResource()))
            .retrieve()
            .bodyToMono(String.class)
            .block();
    }
    
    //Gửi tin tư vấn đính kèm ảnh
    private String invokeSendMessageWithAttachment(String accessToken, String userId, String messageText,
                                                   String imageUrl) {
        String apiUrl = znsUri + "/v3.0/oa/message/cs";
        
        Map<String, Object> body = new HashMap<>();
        Map<String, String> recipient = new HashMap<>();
        Map<String, Object> message = new HashMap<>();
        Map<String, Object> attachment = new HashMap<>();
        Map<String, Object> payload = new HashMap<>();
        Map<String, Object> element = new HashMap<>();
        
        // Set recipient and message content
        recipient.put("user_id", userId);
        message.put("text", messageText);
        
        // Set attachment with media type and URL
        element.put("media_type", "image");
        element.put("url", imageUrl);
        payload.put("template_type", "media");
        payload.put("elements", Arrays.asList(element));
        attachment.put("type", "template");
        attachment.put("payload", payload);
        
        // Add attachment to message
        message.put("attachment", attachment);
        
        // Add recipient and message to body
        body.put("recipient", recipient);
        body.put("message", message);
        
        return invokePostApi(accessToken, apiUrl, body);
    }
    
    //Gửi tin tư vấn đính kèm ảnh + button
    private String invokeSendMessageWithAttachment(String accessToken, String userId, String messageText,
                                                   String imageUrl, List<Map<String, Object>> buttons) {
        String apiUrl = znsUri + "/v3.0/oa/message/cs";
        
        Map<String, Object> body = new HashMap<>();
        Map<String, String> recipient = new HashMap<>();
        Map<String, Object> message = new HashMap<>();
        Map<String, Object> attachment = new HashMap<>();
        Map<String, Object> payload = new HashMap<>();
        Map<String, Object> element = new HashMap<>();
        
        // Set recipient and message content
        recipient.put("user_id", userId);
        message.put("text", messageText);
        
        // Set attachment with media type and URL
        element.put("media_type", "image");
        element.put("url", imageUrl);
        payload.put("template_type", "media");
        payload.put("elements", Arrays.asList(element));
        attachment.put("type", "template");
        
        //Set button to payload
        payload.put("buttons", buttons);
        
        // Set attachment with payload
        attachment.put("payload", payload);
        
        
        // Add attachment to message
        message.put("attachment", attachment);
        
        
        // Add recipient and message to body
        body.put("recipient", recipient);
        body.put("message", message);
        
        return invokePostApi(accessToken, apiUrl, body);
    }
    
    
    //Gửi tin tư vấn đính kèm file
    private String invokeSendMessageWithFileAttachment(String accessToken, String userId, String token) {
        String apiUrl = znsUri + "/v3.0/oa/message/cs";
        
        Map<String, Object> body = new HashMap<>();
        Map<String, String> recipient = new HashMap<>();
        Map<String, Object> message = new HashMap<>();
        Map<String, Object> attachment = new HashMap<>();
        Map<String, String> payload = new HashMap<>();
        
        // Set recipient
        recipient.put("user_id", userId);
        
        // Set attachment with file type and token
        payload.put("token", token);
        attachment.put("type", "file");
        attachment.put("payload", payload);
        
        // Add attachment to message
        message.put("attachment", attachment);
        
        // Add recipient and message to body
        body.put("recipient", recipient);
        body.put("message", message);
        
        return invokePostApi(accessToken, apiUrl, body);
    }
    
    //Gửi tin tư vấn yêu cầu thông tin người dùng
    private String invokeSendRequestUserInfoMessage(String accessToken, String userId, String title, String subtitle, String imageUrl) {
        String apiUrl = znsUri + "/v3.0/oa/message/cs";
        
        // Tạo body request
        Map<String, Object> body = new HashMap<>();
        Map<String, String> recipient = new HashMap<>();
        Map<String, Object> message = new HashMap<>();
        Map<String, Object> attachment = new HashMap<>();
        Map<String, Object> payload = new HashMap<>();
        List<Map<String, String>> elements = new ArrayList<>();
        
        recipient.put("user_id", userId);
        
        Map<String, String> element = new HashMap<>();
        element.put("title", title);
        element.put("subtitle", subtitle);
        element.put("image_url", imageUrl);
        elements.add(element);
        
        payload.put("template_type", "request_user_info");
        payload.put("elements", elements);
        
        attachment.put("type", "template");
        attachment.put("payload", payload);
        
        message.put("attachment", attachment);
        body.put("recipient", recipient);
        body.put("message", message);
        
        // Gửi request
        return invokePostApi(accessToken, apiUrl, body);
    }
    
    //Gửi tin tư vấn trích dẫn
    private String invokeSendMessageWithQuote(String accessToken, String userId, String messageText, String quoteMessageId) {
        String apiUrl = znsUri + "/v3.0/oa/message/cs";
        
        Map<String, Object> body = new HashMap<>();
        Map<String, String> recipient = new HashMap<>();
        Map<String, Object> message = new HashMap<>();
        
        // Set recipient and message content
        recipient.put("user_id", userId);
        message.put("text", messageText);
        message.put("quote_message_id", quoteMessageId);
        
        // Add recipient and message to body
        body.put("recipient", recipient);
        body.put("message", message);
        
        return invokePostApi(accessToken, apiUrl, body);
    }
    
    //Gửi tư vấn kèm sticker
    private String invokeSendMessageWithStickerAttachment(String accessToken, String userId, String attachmentId) {
        String apiUrl = znsUri + "/v3.0/oa/message/cs";
        
        Map<String, Object> body = new HashMap<>();
        Map<String, String> recipient = new HashMap<>();
        Map<String, Object> message = new HashMap<>();
        Map<String, Object> attachment = new HashMap<>();
        Map<String, Object> payload = new HashMap<>();
        Map<String, Object> element = new HashMap<>();
        
        // Set recipient
        recipient.put("user_id", userId);
        
        // Set attachment with sticker type and attachment ID
        element.put("media_type", "sticker");
        element.put("attachment_id", attachmentId);
        payload.put("template_type", "media");
        payload.put("elements", Arrays.asList(element));
        attachment.put("type", "template");
        attachment.put("payload", payload);
        
        // Add attachment to message
        message.put("attachment", attachment);
        
        // Add recipient and message to body
        body.put("recipient", recipient);
        body.put("message", message);
        
        return invokePostApi(accessToken, apiUrl, body);
    }
    
    //Gửi tin giao dịch
    private String invokeSendTransactionMessage(String accessToken, String userId, String attachmentId) {
        String apiUrl = znsUri + "/v3.0/oa/message/transaction";
        
        Map<String, Object> body = new HashMap<>();
        Map<String, String> recipient = new HashMap<>();
        Map<String, Object> message = new HashMap<>();
        Map<String, Object> attachment = new HashMap<>();
        Map<String, Object> payload = new HashMap<>();
        List<Map<String, Object>> elements = new ArrayList<>();
        List<Map<String, Object>> buttons = new ArrayList<>();
        
        // Set recipient
        recipient.put("user_id", userId);
        
        // Set elements for template
        Map<String, Object> banner = new HashMap<>();
        banner.put("attachment_id", attachmentId);
        banner.put("type", "banner");
        elements.add(banner);
        
        Map<String, Object> header = new HashMap<>();
        header.put("type", "header");
        header.put("content", "Trạng thái đơn hàng");
        header.put("align", "left");
        elements.add(header);
        
        Map<String, Object> text1 = new HashMap<>();
        text1.put("type", "text");
        text1.put("align", "left");
        text1.put("content", "• Cảm ơn bạn đã mua hàng tại cửa hàng.<br>• Thông tin đơn hàng của bạn như sau:");
        elements.add(text1);
        
        Map<String, Object> table = new HashMap<>();
        table.put("type", "table");
        List<Map<String, String>> tableContent = Arrays.asList(
            Map.of("key", "Mã khách hàng", "value", "F-01332973223"),
            Map.of("key", "Trạng thái", "value", "Đang giao", "style", "yellow"),
            Map.of("key", "Giá tiền", "value", "250,000đ")
        );
        table.put("content", tableContent);
        elements.add(table);
        
        Map<String, Object> text2 = new HashMap<>();
        text2.put("type", "text");
        text2.put("align", "center");
        text2.put("content", "📱Lưu ý điện thoại. Xin cảm ơn!");
        elements.add(text2);
        
        // Set buttons
        buttons.add(createButton("Kiểm tra lộ trình - default icon", "", "oa.open.url", Map.of("url", "https://oa.zalo.me/home")));
        buttons.add(createButton("Xem lại giỏ hàng", "wZ753VDsR4xWEC89zNTsNkGZr1xsPs19vZF22VHtTbxZ8zG9g24u3FXjZrQvQNH2wMl1MhbwT5_oOvX5_szXLB8tZq--TY0Dhp61JRfsAWglCej8ltmg3xC_rqsWAdjRkctG5lXzAGVlQe9BhZ9mJcSYVIDsc7MoPMnQ", "oa.query.show", "kiểm tra giỏ hàng"));
        buttons.add(createButton("Liên hệ tổng đài", "gNf2KPUOTG-ZSqLJaPTl6QTcKqIIXtaEfNP5Kv2NRncWPbDJpC4XIxie20pTYMq5gYv60DsQRHYn9XyVcuzu4_5o21NQbZbCxd087DcJFq7bTmeUq9qwGVie2ahEpZuLg2KDJfJ0Q12c85jAczqtKcSYVGJJ1cZMYtKR", "oa.open.phone", Map.of("phone_code", "84123456789")));
        
        // Set payload
        payload.put("template_type", "transaction_order");
        payload.put("language", "VI");
        payload.put("elements", elements);
        payload.put("buttons", buttons);
        
        // Set attachment
        attachment.put("type", "template");
        attachment.put("payload", payload);
        
        // Set message with attachment
        message.put("attachment", attachment);
        
        // Add recipient and message to body
        body.put("recipient", recipient);
        body.put("message", message);
        
        return invokePostApi(accessToken, apiUrl, body);
    }
    
    private Map<String, Object> createButton(String title, String imageIcon, String type, Object payload) {
        Map<String, Object> button = new HashMap<>();
        button.put("title", title);
        button.put("image_icon", imageIcon);
        button.put("type", type);
        button.put("payload", payload);
        return button;
    }
    
    //Gửi tin Truyền thông cá nhân
    private String invokeSendPromotionMessage(String accessToken, String userId, String attachmentId) {
        String apiUrl = znsUri + "/v3.0/oa/message/promotion";
        
        Map<String, Object> body = new HashMap<>();
        Map<String, String> recipient = new HashMap<>();
        Map<String, Object> message = new HashMap<>();
        Map<String, Object> attachment = new HashMap<>();
        Map<String, Object> payload = new HashMap<>();
        List<Map<String, Object>> elements = new ArrayList<>();
        List<Map<String, Object>> buttons = new ArrayList<>();
        
        // Set recipient
        recipient.put("user_id", userId);
        
        // Set elements for promotion template
        Map<String, Object> banner = new HashMap<>();
        banner.put("attachment_id", attachmentId);
        banner.put("type", "banner");
        elements.add(banner);
        
        Map<String, Object> header = new HashMap<>();
        header.put("type", "header");
        header.put("content", "💥💥Ưu đãi thành viên Platinum💥💥");
        elements.add(header);
        
        Map<String, Object> text1 = new HashMap<>();
        text1.put("type", "text");
        text1.put("align", "left");
        text1.put("content", "Ưu đãi dành riêng cho khách hàng Nguyen Van A hạng thẻ Platinum<br>Voucher trị giá 150$");
        elements.add(text1);
        
        Map<String, Object> table = new HashMap<>();
        table.put("type", "table");
        List<Map<String, String>> tableContent = Arrays.asList(
            Map.of("key", "Voucher", "value", "VC09279222"),
            Map.of("key", "Hạn sử dụng", "value", "30/12/2023")
        );
        table.put("content", tableContent);
        elements.add(table);
        
        Map<String, Object> text2 = new HashMap<>();
        text2.put("type", "text");
        text2.put("align", "center");
        text2.put("content", "Áp dụng tất cả cửa hàng trên toàn quốc");
        elements.add(text2);
        
        // Set buttons
        buttons.add(createButton("Tham khảo chương trình", "", "oa.open.url", Map.of("url", "https://oa.zalo.me/home")));
        buttons.add(createButton("Liên hệ chăm sóc viên", "aeqg9SYn3nIUYYeWohGI1fYRF3V9f0GHceig8Ckq4WQVcpmWb-9SL8JLPt-6gX0QbTCfSuQv40UEst1imAm53CwFPsQ1jq9MsOnlQe6rIrZOYcrlWBTAKy_UQsV9vnfGozCuOvFfIbN5rcXddFKM4sSYVM0D50I9eWy3", "oa.query.hide", "#tuvan"));
        
        // Set payload
        payload.put("template_type", "promotion");
        payload.put("elements", elements);
        payload.put("buttons", buttons);
        
        // Set attachment
        attachment.put("type", "template");
        attachment.put("payload", payload);
        
        // Set message with attachment
        message.put("attachment", attachment);
        
        // Add recipient and message to body
        body.put("recipient", recipient);
        body.put("message", message);
        
        return invokePostApi(accessToken, apiUrl, body);
    }
    
    //Gửi tin Truyền thông Broadcast
    String invokeSendTargetedMessage(String accessToken, String gender, String cities, String attachmentId) {
        String apiUrl = znsUri + "/v2.0/oa/message";
        
        Map<String, Object> body = new HashMap<>();
        Map<String, Object> recipient = new HashMap<>();
        Map<String, Object> target = new HashMap<>();
        Map<String, Object> message = new HashMap<>();
        Map<String, Object> attachment = new HashMap<>();
        Map<String, Object> payload = new HashMap<>();
        List<Map<String, Object>> elements = new ArrayList<>();
        
        // Set target information
        target.put("gender", gender);
        target.put("cities", cities);
        recipient.put("target", target);
        
        // Set elements for media template
        Map<String, Object> element = new HashMap<>();
        element.put("media_type", "article");
        element.put("attachment_id", attachmentId);
        elements.add(element);
        
        // Set payload for template
        payload.put("template_type", "media");
        payload.put("elements", elements);
        
        // Set attachment
        attachment.put("type", "template");
        attachment.put("payload", payload);
        
        // Set message with attachment
        message.put("attachment", attachment);
        
        // Add recipient and message to body
        body.put("recipient", recipient);
        body.put("message", message);
        
        return invokePostApi(accessToken, apiUrl, body);
    }
}
