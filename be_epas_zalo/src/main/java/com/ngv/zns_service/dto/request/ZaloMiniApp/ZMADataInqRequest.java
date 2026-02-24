package com.ngv.zns_service.dto.request.ZaloMiniApp;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ZMADataInqRequest {
    private String zaloId;
    private String phoneNumber;
    private String requestType;
    private String userToken;
    private String phoneToken;
    private String parameters;

    // Getter và Setter
    public String getZaloId() {
        return zaloId;
    }
    public void setZaloId(String zaloId) {
        this.zaloId = zaloId;
    }

public String getUserToken() {
        return userToken;
    }
    public void setUserToken(String userToken) {
        this.userToken = userToken;
    }

    public String getPhoneToken() {
        return phoneToken;
    }
    public void setPhoneToken(String phoneToken) {
        this.phoneToken = phoneToken;
    }
 
    public String getPhoneNumber() {
        return phoneNumber;
    }
    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getRequestType() {
        return requestType;
    }
    public void setRequestType(String requestType) {
        this.requestType = requestType;
    }

    public String getParameters() {
        return parameters;
    }
    public void setParameters(String parameters) {
        this.parameters = parameters;
    }
}
