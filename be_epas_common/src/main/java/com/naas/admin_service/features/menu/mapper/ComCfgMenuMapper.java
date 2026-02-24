package com.naas.admin_service.features.menu.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

import com.naas.admin_service.features.menu.dto.com_cfg_menu.ComCfgMenuBaseReqDto;
import com.naas.admin_service.features.menu.dto.com_cfg_menu.ComCfgMenuReqDto;
import com.naas.admin_service.features.menu.dto.com_cfg_menu.ComCfgMenuResDto;
import com.naas.admin_service.features.menu.model.ComCfgMenu;


@Mapper(componentModel = "spring")
public interface ComCfgMenuMapper {
    public ComCfgMenu toEntity(ComCfgMenuReqDto comCfgMenuReqDto);

    @Mapping(target = "menuId", ignore = true)
    void updateEntity(ComCfgMenuBaseReqDto comCfgMenuBaseReqDto, @MappingTarget ComCfgMenu comCfgMenu);

    public ComCfgMenuResDto toResDto(ComCfgMenu comCfgMenu);
}
