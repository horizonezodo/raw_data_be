package com.naas.admin_service.features.mail.mapper;

import com.naas.admin_service.features.mail.dto.comcfgmailtemplate.ComCfgMailTemplateRequest;
import com.naas.admin_service.features.mail.dto.comlogmail.ComLogMailDto;
import com.naas.admin_service.features.mail.model.ComLogMail;

import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface ComLogMailMapper {
    ComLogMail toEntity(ComCfgMailTemplateRequest comCfgMailTemplateDto);
    ComLogMailDto toDto(ComLogMail comLogMail);
}
