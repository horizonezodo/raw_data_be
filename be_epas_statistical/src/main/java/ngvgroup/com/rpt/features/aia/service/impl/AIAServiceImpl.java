package ngvgroup.com.rpt.features.aia.service.impl;

import com.ngvgroup.bpm.core.common.exception.ErrorCode;
import ngvgroup.com.rpt.features.aia.dto.ChatMessageRequest;
import ngvgroup.com.rpt.features.aia.dto.ChatMessageResponse;
import ngvgroup.com.rpt.features.aia.dto.ConversationsResponse;
import ngvgroup.com.rpt.features.aia.dto.ConversationDetailResponse;
import ngvgroup.com.rpt.features.integration.AIAGatewayProvider;
import ngvgroup.com.rpt.features.integration.OptionConst;
import ngvgroup.com.rpt.features.integration.OptionManager;
import ngvgroup.com.rpt.features.aia.service.AIAService;
import com.ngvgroup.bpm.core.common.exception.BusinessException;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;

@Log4j2
@Service
@RequiredArgsConstructor
public class AIAServiceImpl implements AIAService {

    private final AIAGatewayProvider aiaGatewayProvider;
    private final OptionManager optionManager;

    @Override
    public ChatMessageResponse sendChatMessage(ChatMessageRequest request) {
        String url = optionManager.getSettingValueForApplicationAsync(OptionConst.AIA_GATEWAY_CHAT_MESSAGE);
        try {
            return aiaGatewayProvider.postAsync(url, request)
                .thenApply(res -> res)
                .exceptionally(ex -> {
                    throw new BusinessException(ErrorCode.INTERNAL_SERVER_ERROR);
                })
                .join()
                .bodyToMono(ChatMessageResponse.class).block();
        } catch (Exception ex) {
            log.error("Error sending chat message: {}", ex.getMessage(), ex);
            throw new BusinessException(ErrorCode.INTERNAL_SERVER_ERROR);
        }
    }

    @Override
    public ConversationsResponse getConversations(String userId) {
        String baseUrl = optionManager.getSettingValueForApplicationAsync(OptionConst.AIA_GATEWAY_CONVERSATIONS);
        String url = baseUrl + "?user_id=" + userId;
        try {
            return aiaGatewayProvider.getAsync(url)
                .thenApply(res -> res)
                .exceptionally(ex -> {
                    throw new BusinessException(ErrorCode.INTERNAL_SERVER_ERROR);
                })
                .join()
                .bodyToMono(ConversationsResponse.class).block();
        } catch (Exception ex) {
            log.error("Error getting conversations: {}", ex.getMessage(), ex);
            throw new BusinessException(ErrorCode.INTERNAL_SERVER_ERROR);
        }
    }

    @Override
    public ConversationDetailResponse getConversationDetail(String conversationId, String userId) {
        String baseUrl = optionManager.getSettingValueForApplicationAsync(OptionConst.AIA_GATEWAY_CONVERSATION_DETAIL);
        String url = baseUrl + "/" + conversationId + "?user_id=" + userId;
        try {
            return aiaGatewayProvider.getAsync(url)
                .thenApply(res -> res)
                .exceptionally(ex -> {
                    throw new BusinessException(ErrorCode.INTERNAL_SERVER_ERROR);
                })
                .join()
                .bodyToMono(ConversationDetailResponse.class).block();
        } catch (Exception ex) {
            log.error("Error getting conversation detail: {}", ex.getMessage(), ex);
            throw new BusinessException(ErrorCode.INTERNAL_SERVER_ERROR);
        }
    }
}
