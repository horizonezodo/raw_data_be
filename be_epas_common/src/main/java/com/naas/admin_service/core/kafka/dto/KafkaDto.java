package com.naas.admin_service.core.kafka.dto;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import lombok.Data;

import java.util.List;

@Data
public class KafkaDto {
    String templateCode;
    String[] toEmail;
    String[] ccEmail;
    List<PairKVDto> data;
    String businessKey;

    @Override
    public String toString() {
        ObjectMapper om = new ObjectMapper();
        try {
            return om.writeValueAsString(this);
        } catch (JsonProcessingException e) {
            return "ERROR GENERATING JSON OBJECT";
        }
    }
}
