package com.ngv.zns_service.service.impl;


import com.ngv.zns_service.constant.ZaloErrorCode;
import com.ngv.zns_service.dto.request.ZaloMiniApp.AIADataInqRequest;
import com.ngv.zns_service.dto.request.ZaloMiniApp.ZMADataInqRequest;
import com.ngv.zns_service.dto.response.ZaloMiniApp.AIADataInqResponse;
import com.ngv.zns_service.dto.response.ZaloMiniApp.ZMADataInqResponse;
import com.ngvgroup.bpm.core.common.exception.BusinessException;
import com.ngv.zns_service.intergation.EfundGatewayProvider;
import com.ngv.zns_service.intergation.OptionConst;
import com.ngv.zns_service.intergation.OptionManager;
import com.ngv.zns_service.service.EfundService;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;

@Log4j2
@Service
@RequiredArgsConstructor
public class EfundServiceImpl implements EfundService {

    private final EfundGatewayProvider efundGatewayProvider;
    private final OptionManager optionManager;

    @Override
    public String callEfundApi(String partnerCode) {
        String baseUrl = optionManager.getSettingValueForApplicationAsync(partnerCode, OptionConst.EFUND_GATEWAY_BASE_URL);
        String url = baseUrl + "/MessageSyncZNS";
        String data = efundGatewayProvider.getAsync(url, partnerCode)
                .thenApply(response -> {
                    log.info(response);
                    return response;
                })
                .exceptionally(ex -> {
                    throw new BusinessException(ZaloErrorCode.ERROR_INTERGRATED);
                })
                .join()
                .bodyToMono(String.class).block();
        return data;
    }

    @Override
    public ZMADataInqResponse EFfundZMALogin(String partnerCode, ZMADataInqRequest zmaDataInq) {
        String url = optionManager.getSettingValueForApplicationAsync(partnerCode, OptionConst.EFUND_GATEWAY_ZMA_LOGIN);
        try {
            ZMADataInqResponse response = efundGatewayProvider.postAsyncAuthen(url, partnerCode, zmaDataInq)
                .thenApply(res -> {
                    return res;
                })
                .exceptionally(ex -> {
                    throw new BusinessException(ZaloErrorCode.ERROR_INTERGRATED);
                })
                .join()
                .bodyToMono(ZMADataInqResponse.class).block();
            return response;
        } catch (Exception ex) {
            throw new BusinessException(ZaloErrorCode.ERROR_INTERGRATED);
        }
    }

    @Override
    public ZMADataInqResponse EFfundZMAPublicInfo(String partnerCode, ZMADataInqRequest zmaDataInq) {
        String url = optionManager.getSettingValueForApplicationAsync(partnerCode, OptionConst.EFUND_GATEWAY_ZMA_PUBLIC_INFO);
        try {
            ZMADataInqResponse response = efundGatewayProvider.postAsyncAuthen(url, partnerCode, zmaDataInq)
                .thenApply(res -> {
                    return res;
                })
                .exceptionally(ex -> {
                    throw new BusinessException(ZaloErrorCode.ERROR_INTERGRATED);
                })
                .join()
                .bodyToMono(ZMADataInqResponse.class).block();
            return response;
        } catch (Exception ex) {
            throw new BusinessException(ZaloErrorCode.ERROR_INTERGRATED);
        }
    }

    @Override
    public ZMADataInqResponse EFfundZMASignup(String partnerCode, ZMADataInqRequest zmaDataInq) {
        String url = optionManager.getSettingValueForApplicationAsync(partnerCode, OptionConst.EFUND_GATEWAY_ZMA_SIGNUP);
        try {
            ZMADataInqResponse response = efundGatewayProvider.postAsyncAuthen(url, partnerCode, zmaDataInq)
                .thenApply(res -> {
                    return res;
                })
                .exceptionally(ex -> {
                    throw new BusinessException(ZaloErrorCode.ERROR_INTERGRATED);
                })
                .join()
                .bodyToMono(ZMADataInqResponse.class).block();
            return response;
        } catch (Exception ex) {
            throw new BusinessException(ZaloErrorCode.ERROR_INTERGRATED);
        }
    }

    @Override
    public ZMADataInqResponse EFfundZMADataInq(String partnerCode, ZMADataInqRequest zmaDataInq, String zmpToken) {
        String url = optionManager.getSettingValueForApplicationAsync(partnerCode, OptionConst.EFUND_GATEWAY_ZMA_DATA_INQ);
        try {
            ZMADataInqResponse response = efundGatewayProvider.postAsyncAuthen(url, partnerCode, zmaDataInq, zmpToken)
                .thenApply(res -> {
                    return res;
                })
                .exceptionally(ex -> {
                    throw new BusinessException(ZaloErrorCode.ERROR_INTERGRATED);
                })
                .join()
                .bodyToMono(ZMADataInqResponse.class).block();
            return response;
        } catch (Exception ex) {
            throw new BusinessException(ZaloErrorCode.ERROR_INTERGRATED);
        }
    }

    @Override
    public ZMADataInqResponse EFfundZMACreateTranCore(String partnerCode, ZMADataInqRequest zmaDataInq, String zmpToken) {
        String url = optionManager.getSettingValueForApplicationAsync(partnerCode, OptionConst.EFUND_GATEWAY_ZMA_CREATE_TRAN_CORE);
        try {
            ZMADataInqResponse response = efundGatewayProvider.postAsyncAuthen(url, partnerCode, zmaDataInq, zmpToken)
                .thenApply(res -> {
                    return res;
                })
                .exceptionally(ex -> {
                    throw new BusinessException(ZaloErrorCode.ERROR_INTERGRATED);
                })
                .join()
                .bodyToMono(ZMADataInqResponse.class).block();
            return response;
        } catch (Exception ex) {
            throw new BusinessException(ZaloErrorCode.ERROR_INTERGRATED);
        }
    }

    @Override
    public ZMADataInqResponse EFfundZMATransactionRegistration(String partnerCode, ZMADataInqRequest zmaDataInq,String zmpToken) {
        String url = optionManager.getSettingValueForApplicationAsync(partnerCode, OptionConst.EFUND_GATEWAY_ZMA_TRANSACTION_REGISTRATION);
        try {
            ZMADataInqResponse response = efundGatewayProvider.postAsyncAuthen(url, partnerCode, zmaDataInq, zmpToken)
                .thenApply(res -> {
                    log.info(res);
                    return res;
                })
                .exceptionally(ex -> {
                    throw new BusinessException(ZaloErrorCode.ERROR_INTERGRATED);
                })
                .join()
                .bodyToMono(ZMADataInqResponse.class).block();
            return response;
        } catch (Exception ex) {
            throw new BusinessException(ZaloErrorCode.ERROR_INTERGRATED);
        }
    }

    @Override
    public AIADataInqResponse EFfundAIADataInq(String partnerCode, AIADataInqRequest aiaDataInq, String zmpToken) {
        String url = optionManager.getSettingValueForApplicationAsync(partnerCode, OptionConst.EFUND_GATEWAY_AIA_DATA_INQ);
        try {
            AIADataInqResponse response = efundGatewayProvider.postAsyncAuthen(url, partnerCode, aiaDataInq, zmpToken)
                .thenApply(res -> {
                    log.info(res);
                    return res;
                })
                .exceptionally(ex -> {
                    throw new BusinessException(ZaloErrorCode.ERROR_INTERGRATED);
                })
                .join()
                .bodyToMono(AIADataInqResponse.class).block();
            return response;
        } catch (Exception ex) {
            throw new BusinessException(ZaloErrorCode.ERROR_INTERGRATED);
        }
    }

    @Override
    public byte[] getImageStream(String partnerCode, String imageName) {
        try {
            // Get base image URL from options
            String baseImageUrl = optionManager.getSettingValueForApplicationAsync(partnerCode, OptionConst.EFUND_GATEWAY_IMAGE_BASE_URL);
            String imageUrl = baseImageUrl + "/" + imageName;
            
            // Stream image data from external URL with authentication
            byte[] imageData = efundGatewayProvider.getAsync(imageUrl, partnerCode)
                    .thenCompose(response -> response.bodyToMono(byte[].class).toFuture())
                    .join();
            
            return imageData;
        } catch (Exception ex) {
            throw new BusinessException(ZaloErrorCode.ERROR_INTERGRATED);
        }
    }
}