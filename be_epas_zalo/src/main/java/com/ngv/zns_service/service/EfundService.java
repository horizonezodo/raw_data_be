package com.ngv.zns_service.service;


import com.ngv.zns_service.dto.request.ZaloMiniApp.AIADataInqRequest;
import com.ngv.zns_service.dto.request.ZaloMiniApp.ZMADataInqRequest;
import com.ngv.zns_service.dto.response.ZaloMiniApp.AIADataInqResponse;
import com.ngv.zns_service.dto.response.ZaloMiniApp.ZMADataInqResponse;

public interface EfundService {
    String callEfundApi(String partnerCode);
    ZMADataInqResponse EFfundZMADataInq(String partnerCode, ZMADataInqRequest zmaDataInq, String zmpToken);
    ZMADataInqResponse EFfundZMACreateTranCore(String partnerCode, ZMADataInqRequest zmaDataInq, String zmpToken);
    ZMADataInqResponse EFfundZMALogin(String partnerCode, ZMADataInqRequest zmaDataInq);
    ZMADataInqResponse EFfundZMAPublicInfo(String partnerCode, ZMADataInqRequest zmaDataInq);
    ZMADataInqResponse EFfundZMASignup(String partnerCode, ZMADataInqRequest zmaDataInq);
    ZMADataInqResponse EFfundZMATransactionRegistration(String partnerCode, ZMADataInqRequest zmaDataInq,String zmpToken);
    AIADataInqResponse EFfundAIADataInq(String partnerCode, AIADataInqRequest aiaDataInq, String zmpToken);
    
    // Image streaming proxy
    byte[] getImageStream(String partnerCode, String imageName);
}
