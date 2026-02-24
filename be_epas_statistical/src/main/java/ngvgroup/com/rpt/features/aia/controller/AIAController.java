package ngvgroup.com.rpt.features.aia.controller;

import com.ngvgroup.bpm.core.common.dto.ResponseData;
import com.ngvgroup.bpm.core.logging.activity.annotation.LogActivity;
import ngvgroup.com.rpt.features.aia.dto.ChatMessageRequest;
import ngvgroup.com.rpt.features.aia.dto.ChatMessageResponse;
import ngvgroup.com.rpt.features.aia.dto.ConversationsResponse;
import ngvgroup.com.rpt.features.aia.dto.ConversationDetailResponse;
import ngvgroup.com.rpt.features.aia.service.AIAService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/ai-tool")
@RequiredArgsConstructor
public class AIAController {

    private final AIAService aiaService;

    @LogActivity(function = "Gửi tin nhắn chat AI")
    @PostMapping("/chat/message")
    public ResponseEntity<ResponseData<ChatMessageResponse>> sendChatMessage(
            @RequestBody ChatMessageRequest request) {
        ChatMessageResponse res = aiaService.sendChatMessage(request);
        return ResponseData.okEntity(res);
    }

    @LogActivity(function = "Lấy danh sách hội thoại")
    @GetMapping("/conversations")
    public ResponseEntity<ResponseData<ConversationsResponse>> getConversations(
            @RequestParam String userId) {
        ConversationsResponse res = aiaService.getConversations(userId);
        return ResponseData.okEntity(res);
    }

    @LogActivity(function = "Lấy chi tiết hội thoại")
    @GetMapping("/conversations/{conversationId}")
    public ResponseEntity<ResponseData<ConversationDetailResponse>> getConversationDetail(
            @PathVariable String conversationId,
            @RequestParam String userId) {
        ConversationDetailResponse res = aiaService.getConversationDetail(conversationId, userId);
        return ResponseData.okEntity(res);
    }
}