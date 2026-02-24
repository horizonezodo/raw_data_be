package com.naas.admin_service.features.permission.service.impl;

import com.naas.admin_service.core.contants.CommonErrorCode;
import com.naas.admin_service.features.permission.dto.ComCfgPermissionDto;
import com.naas.admin_service.features.permission.model.ComCfgPermission;
import com.naas.admin_service.features.permission.model.ComCfgPermissionMap;
import com.naas.admin_service.features.permission.repository.ComCfgPermissionMapRepository;
import com.naas.admin_service.features.permission.repository.ComCfgPermissionRepository;
import com.naas.admin_service.features.permission.service.PermissionService;
import com.ngvgroup.bpm.core.common.exception.BusinessException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class PermissionServiceImpl implements PermissionService {

    private final ComCfgPermissionRepository permissionRepository;
    private final ComCfgPermissionMapRepository permissionMapRepository;

    @Override
    public void createPermission(ComCfgPermissionDto permissionDto) {
        permissionRepository.findByCode(permissionDto.getCode()).ifPresent(p -> {
            throw new BusinessException(CommonErrorCode.EXISTS, permissionDto.getCode());
        });
        ComCfgPermission permission = new ComCfgPermission();
        permission.setCode(permissionDto.getCode());
        permission.setName(permissionDto.getName());
        permissionRepository.save(permission);
    }

    @Override
    public List<ComCfgPermissionDto> getAllPermissions() {
        return permissionRepository.findAll().stream().map(this::toDto).toList();
    }

    @Override
    public ComCfgPermissionDto getPermissionByCode(String code) {
        return permissionRepository.findByCode(code).map(this::toDto)
                .orElseThrow(() -> new BusinessException(CommonErrorCode.NOT_FOUND, code));
    }

    @Override
    public void updatePermission(String code, ComCfgPermissionDto permissionDto) {
        ComCfgPermission permission = permissionRepository.findByCode(code)
                .orElseThrow(() -> new BusinessException(CommonErrorCode.NOT_FOUND, code));
        permission.setName(permissionDto.getName());
        permissionRepository.save(permission);
    }

    @Override
    public void deletePermission(String code) {
        ComCfgPermission permission = permissionRepository.findByCode(code)
                .orElseThrow(() -> new BusinessException(CommonErrorCode.NOT_FOUND, code));
        permissionRepository.delete(permission);
    }

    @Override
    public void assignRolesToUser(String userId, List<String> roleNames) {
        List<ComCfgPermissionMap> mappings = roleNames.stream()
                .map(roleName -> ComCfgPermissionMap.builder()
                        .userId(userId)
                        .permissionCode(roleName)
                        .build())
                .toList();
        permissionMapRepository.saveAll(mappings);
    }

    @Override
    public void unAssignRolesFromUser(String userId, List<String> roleNames) {
        List<ComCfgPermissionMap> mappings = permissionMapRepository.findByUserIdAndPermissionCodeIn(userId, roleNames);
        permissionMapRepository.deleteAll(mappings);
    }

    @Override
    public void addRolesToGroup(String groupName, List<String> roleNames) {
        List<ComCfgPermissionMap> mappings = roleNames.stream()
                .map(roleName -> ComCfgPermissionMap.builder()
                        .groupName(groupName)
                        .permissionCode(roleName)
                        .build())
                .toList();
        permissionMapRepository.saveAll(mappings);
    }

    @Override
    public void removeRolesFromGroup(String groupName, List<String> roleNames) {
        List<ComCfgPermissionMap> mappings = permissionMapRepository.findByGroupNameAndPermissionCodeIn(groupName, roleNames);
        permissionMapRepository.deleteAll(mappings);
    }

    private ComCfgPermissionDto toDto(ComCfgPermission permission) {
        ComCfgPermissionDto dto = new ComCfgPermissionDto();
        dto.setCode(permission.getCode());
        dto.setName(permission.getName());
        return dto;
    }
}
