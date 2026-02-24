package com.naas.admin_service.features.users.service;

import com.naas.admin_service.features.users.dto.ctgcfgresourcemapping.CtgCfgResourceMappingDto1;
import com.naas.admin_service.features.users.dto.ctgcfgresourcemapping.ResourceMappingDto;
import com.naas.admin_service.features.users.model.CtgCfgResourceMapping;
import com.naas.admin_service.features.users.dto.ctgcfgresourcemaster.CtgCfgResourceMappingRequest;

import java.util.List;

public interface CtgCfgResourceMappingService {
    CtgCfgResourceMapping save(CtgCfgResourceMapping resourceMapping);
    List<CtgCfgResourceMapping> findAll();
    CtgCfgResourceMapping findById(Long id);
    void deleteById(Long id);

    void updateCfgResourceMapping(List<CtgCfgResourceMappingRequest> request, String userId);

    void updateGroupCfgResourceMapping(List<CtgCfgResourceMappingRequest> request,String groupId);

    List<CtgCfgResourceMapping> findByResourceTypeCode(String resourceTypeCode);

    List<ResourceMappingDto> getListOfResourceMappingDto(String resourceTypeCode);

    List<CtgCfgResourceMappingDto1> listOrg();

    List<CtgCfgResourceMappingDto1> listArea(String resourceCode);

    List<CtgCfgResourceMappingDto1> findAreaByResourceTypeName(String resourceTypeCode, String orgCode);
}
