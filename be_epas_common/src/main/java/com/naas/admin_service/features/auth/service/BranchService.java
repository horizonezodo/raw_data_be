package com.naas.admin_service.features.auth.service;

import java.util.List;

import com.naas.admin_service.features.users.dto.ctgcfgresourcemapping.ListResourceMappingDto;

public interface BranchService {
    List<ListResourceMappingDto> getListByUsername();
    void updateBranchCode(String username , String branchCode);
}
