package com.naas.admin_service.features.auth.service.impl;

import com.naas.admin_service.core.provider.IdentityStoreService;
import com.naas.admin_service.core.utils.SecurityUtils;
import com.naas.admin_service.features.auth.service.BranchService;
import com.naas.admin_service.features.users.dto.ctgcfgresourcemapping.ListResourceMappingDto;
import com.naas.admin_service.features.users.repository.CtgCfgResourceMappingRepository;
import com.naas.admin_service.features.users.service.UserService;

import lombok.RequiredArgsConstructor;
import org.keycloak.representations.idm.UserRepresentation;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class BranchServiceImpl implements BranchService {

    private final CtgCfgResourceMappingRepository ctgCfgResourceMappingRepository;

    private final UserService userService;

    private final IdentityStoreService identityStoreService;

    @Override
    public List<ListResourceMappingDto> getListByUsername() {
        String userId = getUserIdByUsername(SecurityUtils.getCurrentUsername());
        return ctgCfgResourceMappingRepository.findAllListResourceMappingDto(userId);
    }

    @Override
    public void updateBranchCode(String username, String branchCode) {
        userService.updateBranchCode(username, branchCode);
    }

    private String getUserIdByUsername(String username) {
        List<UserRepresentation> users = identityStoreService.searchUsers(username);
        if (users.isEmpty()) {
            return null;
        }
        return users.get(0).getId();
    }
}
