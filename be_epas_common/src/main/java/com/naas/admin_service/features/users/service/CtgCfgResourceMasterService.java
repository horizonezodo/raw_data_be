package com.naas.admin_service.features.users.service;

import com.naas.admin_service.features.users.dto.ctgcfgresourcemapping.ListResourceMappingDto;
import com.naas.admin_service.features.users.dto.ctgcfgresourcemaster.CtgCfgResourceMasterDto;
import com.naas.admin_service.features.users.dto.ctgcfgresourcemaster.CtgCfgResourceMasterDtoV2;
import com.naas.admin_service.features.users.dto.ctgcfgresourcemaster.ExportExcelReq;
import com.naas.admin_service.features.users.dto.ctgcfgresourcemaster.SearchDTO;
import com.naas.admin_service.features.users.dto.ctgcfgresourcemaster.SearchResourceRequest;

import org.springframework.core.io.ByteArrayResource;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;

import java.util.List;

public interface CtgCfgResourceMasterService {
    void createCfgResourceMaster(CtgCfgResourceMasterDto ctgCfgResourceMasterDto);

    void updateCfgResourceMaster(Long id, CtgCfgResourceMasterDto ctgCfgResourceMasterDto);

    CtgCfgResourceMasterDto getCfgResourceMasterById(Long id);

    void deleteCfgResourceMasterById(Long id);

    Page<CtgCfgResourceMasterDto> searchCfgResourceMaster(SearchDTO req);

    Page<CtgCfgResourceMasterDtoV2> getAllComCfgResourceMasterActive(SearchResourceRequest searchDTO);

    List<CtgCfgResourceMasterDtoV2> getAllResource(Long id, SearchResourceRequest searchDTO, String userId);

    List<CtgCfgResourceMasterDtoV2> getAllGroupResource(Long id, SearchResourceRequest searchDTO, String groupId);

    ResponseEntity<ByteArrayResource> exportExcel(ExportExcelReq request);

    List<ListResourceMappingDto> getListBranch(String userId);
}
