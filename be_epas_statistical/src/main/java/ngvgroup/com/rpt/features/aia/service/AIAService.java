package ngvgroup.com.rpt.features.aia.service;

import ngvgroup.com.rpt.features.aia.dto.ChatMessageRequest;
import ngvgroup.com.rpt.features.aia.dto.ChatMessageResponse;
import ngvgroup.com.rpt.features.aia.dto.ConversationsResponse;
import ngvgroup.com.rpt.features.aia.dto.ConversationDetailResponse;

public interface AIAService {
    /**
     * Send chat message to AIA
     * @param partnerCode Mã đối tác
     * @param request Chat message request
     * @return
     */
    ChatMessageResponse sendChatMessage(ChatMessageRequest request);
    
    /**
     * Get conversations list for user
     * @param partnerCode Mã đối tác
     * @param userId User ID
     * @return
     */
    ConversationsResponse getConversations(String userId);
    
    /**
     * Get conversation detail by conversation ID
     * @param conversationId Conversation ID
     * @param userId User ID
     * @return
     */
    ConversationDetailResponse getConversationDetail(String conversationId, String userId);
}