package com.naas.admin_service.features.mail.service;

import com.naas.admin_service.core.kafka.dto.MailProcessRequestDto;
import com.naas.admin_service.features.mail.dto.comlogmail.ComLogMailDto;
import com.naas.admin_service.features.mail.dto.comlogmail.ComLogMailResponse;
import com.naas.admin_service.features.mail.dto.comlogmail.ComLogMailSearchRequest;
import com.naas.admin_service.features.mail.model.ComCfgMailTemplate;

import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;

public interface MailService {
    void sendMail(MailProcessRequestDto dto);

    void saveLogMail(ComCfgMailTemplate comCfgMailTemplate, boolean isSuccess,
                     MailProcessRequestDto dto, String mailBody, String[] sendTo, String error);

    ComLogMailDto getLogMailDetail(Long id);

    Page<ComLogMailResponse> searchLogMail(ComLogMailSearchRequest request);

    ResponseEntity<byte[]> downloadLogMail(String fileName, ComLogMailSearchRequest request);
}
