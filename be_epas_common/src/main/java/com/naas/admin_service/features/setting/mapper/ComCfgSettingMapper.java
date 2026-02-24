package com.naas.admin_service.features.setting.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

import com.naas.admin_service.features.setting.dto.ComCfgSettingReqDto;
import com.naas.admin_service.features.setting.model.ComCfgSetting;

import java.util.List;

@Mapper(componentModel = "spring")
public interface ComCfgSettingMapper {

    ComCfgSetting toEntity(ComCfgSettingReqDto dto);

    ComCfgSettingReqDto toDto(ComCfgSetting entity);

    List<ComCfgSettingReqDto> toDtoList(List<ComCfgSetting> entities);

    @Mapping(target = "settingCode", ignore = true)
    void updateEntity(ComCfgSettingReqDto dto, @MappingTarget ComCfgSetting entity);
}
