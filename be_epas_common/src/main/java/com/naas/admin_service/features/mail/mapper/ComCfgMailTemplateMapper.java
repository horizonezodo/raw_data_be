package com.naas.admin_service.features.mail.mapper;

import com.naas.admin_service.features.mail.dto.comcfgmailtemplate.ComCfgMailTemplateRequest;
import com.naas.admin_service.features.mail.dto.comcfgmailtemplate.ComCfgMailTemplateResponse;
import com.naas.admin_service.features.mail.model.ComCfgMailTemplate;

import org.mapstruct.*;

@Mapper(componentModel = "spring")
public interface ComCfgMailTemplateMapper {
    ComCfgMailTemplate toEntity(ComCfgMailTemplateRequest comCfgMailTemplateRequest);
    ComCfgMailTemplateRequest toDto(ComCfgMailTemplate comCfgMailTemplate);
    ComCfgMailTemplateResponse toResponse(ComCfgMailTemplate comCfgMailTemplate);

    void updateMailTemplateFromDto(ComCfgMailTemplateRequest dto, @MappingTarget ComCfgMailTemplate entity);
}
