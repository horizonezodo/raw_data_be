package com.naas.admin_service.core.kafka;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.naas.admin_service.core.kafka.dto.KafkaDto;
import com.naas.admin_service.core.kafka.dto.MailProcessRequestDto;
import com.naas.admin_service.core.kafka.dto.MailStatusDto;
import com.naas.admin_service.features.mail.service.MailService;

import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class MessageConsumer {

    private final MessageProducer messageProducer;
    private final MailService mailService;

    public MessageConsumer( MessageProducer messageProducer, MailService mailService) {
        this.messageProducer = messageProducer;
        this.mailService = mailService;
    }

    @KafkaListener(topics = "MAIL_PROCESS", groupId = "kafka_MAIL_PROCESS")
    public void consumer(String message) {
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            MailProcessRequestDto dto = objectMapper.readValue(message, MailProcessRequestDto.class);
            log.info("Message received: {}", dto);
            mailService.sendMail(dto);
            sendTopicStatus(Boolean.TRUE);
        } catch (Exception e) {
            log.error("errrororororor {}", e.getMessage());
            sendTopicStatus(Boolean.FALSE);
        }
    }

    private void sendTopicStatus(boolean isSuccess) {
        MailStatusDto dto;

        if (isSuccess) {
            dto = new MailStatusDto("Success", "Mail sent successfully !");
        } else {
            dto = new MailStatusDto("Failure", "Mail sent Failure !");
        }
        this.messageProducer.sendMessageStatus(dto);
    }
}
