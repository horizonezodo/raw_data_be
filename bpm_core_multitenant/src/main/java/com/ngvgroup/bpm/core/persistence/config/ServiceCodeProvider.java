package com.ngvgroup.bpm.core.persistence.config;

public record ServiceCodeProvider(String serviceCode) {

    public ServiceCodeProvider(String serviceCode) {
        this.serviceCode = (serviceCode == null || serviceCode.isBlank())
                ? "unknown-service"
                : serviceCode.trim();
    }
}
