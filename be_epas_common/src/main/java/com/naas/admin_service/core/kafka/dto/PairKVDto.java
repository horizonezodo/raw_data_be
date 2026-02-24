package com.naas.admin_service.core.kafka.dto;

import lombok.Data;

@Data
public class PairKVDto {
    String key;
    String value;

    public PairKVDto() {
    }

    public PairKVDto(String key, String value) {
        this.key = key;
        this.value = value;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }
}
