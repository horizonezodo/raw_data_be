package com.ngv.zns_service.dto.response;

import lombok.Data;

@Data
public class SelectBoxDto {
    private String value;
    private String label;

    public SelectBoxDto() {
    }

    public SelectBoxDto(String value, String label) {
        this.value = value;
        this.label = label;
    }
} 