package com.naas.admin_service.features.auth.service.impl;


import com.naas.admin_service.core.utils.SecurityUtils;
import com.naas.admin_service.features.auth.service.BranchService;
import com.naas.admin_service.features.users.dto.ctgcfgresourcemapping.ListResourceMappingDto;
import com.naas.admin_service.features.users.repository.CtgCfgResourceMappingRepository;
import com.naas.admin_service.features.users.service.UserService;

import lombok.RequiredArgsConstructor;
import org.keycloak.admin.client.Keycloak;
import org.keycloak.representations.idm.UserRepresentation;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class BranchServiceImpl implements BranchService {

    private final CtgCfgResourceMappingRepository ctgCfgResourceMappingRepository;

    private final UserService userService;

    private final Keycloak keycloak;

    @Value("${security.keycloak.realm}")
    private String realm;


    @Override
    public List<ListResourceMappingDto> getListByUsername() {
        String userId = getUserIdByUsername(SecurityUtils.getCurrentUsername());
        return ctgCfgResourceMappingRepository.findAllListResourceMappingDto(userId);
    }

    @Override
    public void updateBranchCode(String username, String branchCode) {
        userService.updateBranchCode(username , branchCode);
    }


    private String getUserIdByUsername(String username) {
        List<UserRepresentation> users = keycloak.realm(realm).users().search(username);
        if (users.isEmpty()) {
            return null;
        }
        return users.get(0).getId();
    }
}
