package com.naas.admin_service.core.kafka;

import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

import com.naas.admin_service.core.kafka.dto.KafkaDto;
import com.naas.admin_service.core.kafka.dto.MailStatusDto;

@Component
public class MessageProducer {

    private final KafkaTemplate<String, String> kafkaTemplate;

    public MessageProducer(KafkaTemplate<String, String> kafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;
    }

    public void sendMessage(KafkaDto dto) {
        kafkaTemplate.send("MAIL-DATA", dto.toString());
    }

    public void sendMessageStatus(MailStatusDto dto) {
        kafkaTemplate.send("MAIL-STATUS", dto.toString());
    }
}
