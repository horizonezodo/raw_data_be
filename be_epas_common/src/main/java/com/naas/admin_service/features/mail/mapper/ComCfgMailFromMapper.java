package com.naas.admin_service.features.mail.mapper;

import com.naas.admin_service.features.mail.dto.comcfgmailfrom.ComCfgMailFromDto;
import com.naas.admin_service.features.mail.model.ComCfgMailFrom;

import org.mapstruct.*;

@Mapper(componentModel = "spring")
public interface ComCfgMailFromMapper {
    ComCfgMailFrom toEntity(ComCfgMailFromDto comCfgMailFromDto);
    ComCfgMailFromDto toDto(ComCfgMailFrom comCfgMailFrom);

    @Mapping(target = "mailCode", ignore = true)
    @Mapping(target = "mailPassword", ignore = true)
    void updateMailTemplateFromDto(ComCfgMailFromDto dto, @MappingTarget ComCfgMailFrom entity);
}
