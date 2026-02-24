package com.ngv.zns_service.service;

import com.fasterxml.jackson.databind.node.ObjectNode;
import com.ngv.zns_service.model.entity.ZssApp;
import com.ngv.zns_service.model.entity.ZssTKhoanZoa;

public interface ZNSAuthService {
    String requestLinkAuthorize(ObjectNode payload);
    ZssTKhoanZoa requestAccessToken(String oaId, String authorizationCode, String state);
    ZssTKhoanZoa refreshAccessToken(String oaId, String refreshToken, ZssApp zssApp);

}
