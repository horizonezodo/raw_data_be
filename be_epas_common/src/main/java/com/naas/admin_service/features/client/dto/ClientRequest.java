package com.naas.admin_service.features.client.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class ClientRequest {
    @NotBlank(message = "clientId không được để trống")
    private String clientId;
    private String name;
    private String description;
    private Boolean publicClient;
    private Boolean authorizationServicesEnabled;
    private Boolean standardFlowEnabled;
    private Boolean directAccessGrantsEnabled;
    private Boolean implicitFlowEnabled;
    private Boolean serviceAccountsEnabled;



}
