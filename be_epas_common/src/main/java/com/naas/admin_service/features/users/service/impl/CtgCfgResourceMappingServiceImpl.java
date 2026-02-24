package com.naas.admin_service.features.users.service.impl;

import com.naas.admin_service.core.contants.Constant;
import com.naas.admin_service.features.users.dto.ctgcfgresourcemapping.CtgCfgResourceMappingDto1;
import com.naas.admin_service.features.users.dto.ctgcfgresourcemapping.ResourceMappingDto;
import com.naas.admin_service.features.users.mapper.ComCfgResourceMappingMapper;
import com.naas.admin_service.features.users.model.CtgCfgResourceMapping;
import com.naas.admin_service.features.users.repository.CtgCfgResourceMappingRepository;
import com.naas.admin_service.features.users.service.CtgCfgResourceMappingService;
import com.naas.admin_service.features.users.dto.ctgcfgresourcemaster.CtgCfgResourceMappingRequest;
import com.naas.admin_service.features.users.dto.ctgcfgresourcemaster.CtgCfgResourceMasterDtoV2;
import com.ngvgroup.bpm.core.persistence.service.storedprocedure.BaseStoredProcedureService;


import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Slf4j
@Service
public class CtgCfgResourceMappingServiceImpl extends BaseStoredProcedureService implements CtgCfgResourceMappingService {
    private final CtgCfgResourceMappingRepository repository;
    private final ComCfgResourceMappingMapper mapper;

    protected CtgCfgResourceMappingServiceImpl(CtgCfgResourceMappingRepository repository, ComCfgResourceMappingMapper mapper) {
        super();
        this.repository = repository;
        this.mapper = mapper;
    }

    @Override
    public CtgCfgResourceMapping save(CtgCfgResourceMapping resourceMapping) {
        return repository.save(resourceMapping);
    }

    @Override
    public List<CtgCfgResourceMapping> findAll() {
        return repository.findAll();
    }

    @Override
    public CtgCfgResourceMapping findById(Long id) {
        return repository.findById(id).orElse(null);
    }

    @Override
    public void deleteById(Long id) {
        repository.deleteById(id);
    }

    @Override
    @Transactional
    public void updateCfgResourceMapping(List<CtgCfgResourceMappingRequest> request, String userId) {
        Map<String, List<CtgCfgResourceMasterDtoV2>> updateResourceCodes = buildUpdateResourceCodesMap(request);
        Map<String, List<CtgCfgResourceMapping>> oldMappingsByType = loadOldUserMappingsByResourceType(
                userId, updateResourceCodes.keySet().stream().toList());

        updateResourceCodes.forEach((rsTypeCode, lst) ->
                processUserResourceType(userId, rsTypeCode, lst, oldMappingsByType));
    }

    private Map<String, List<CtgCfgResourceMasterDtoV2>> buildUpdateResourceCodesMap(
            List<CtgCfgResourceMappingRequest> request) {
        Map<String, List<CtgCfgResourceMasterDtoV2>> map = new HashMap<>();
        request.forEach(item -> map.put(item.getResourceTypeCode(), item.getLstResourceMapping()));
        return map;
    }

    private Map<String, List<CtgCfgResourceMapping>> loadOldUserMappingsByResourceType(
            String userId, List<String> resourceTypeCodes) {
        return repository
                .findByUserIdAndResourceTypeCodeAndIsActive(userId, resourceTypeCodes, null)
                .stream()
                .collect(Collectors.groupingBy(CtgCfgResourceMapping::getResourceTypeCode));
    }

    private void processUserResourceType(String userId, String rsTypeCode, List<CtgCfgResourceMasterDtoV2> lst,
                                         Map<String, List<CtgCfgResourceMapping>> oldMappingsByType) {
        if (oldMappingsByType.containsKey(rsTypeCode)) {
            processExistingUserResourceType(userId, rsTypeCode, lst, oldMappingsByType.get(rsTypeCode));
        } else {
            saveNewUserMappings(userId, rsTypeCode, lst);
        }
    }

    private void processExistingUserResourceType(String userId, String rsTypeCode,
                                                  List<CtgCfgResourceMasterDtoV2> lst,
                                                  List<CtgCfgResourceMapping> oldMappingsList) {
        Map<String, CtgCfgResourceMasterDtoV2> lstUpdate = lst.stream()
                .collect(Collectors.toMap(CtgCfgResourceMasterDtoV2::getResourceCode, rs -> rs));
        Map<String, CtgCfgResourceMapping> lstOld = oldMappingsList.stream()
                .collect(Collectors.toMap(CtgCfgResourceMapping::getResourceCode, rs -> rs));

        applyRequestedUserMappings(userId, rsTypeCode, lstUpdate, lstOld);
        deactivateRemovedUserMappings(lstOld);
    }

    private void applyRequestedUserMappings(String userId, String rsTypeCode,
                                            Map<String, CtgCfgResourceMasterDtoV2> lstUpdate,
                                            Map<String, CtgCfgResourceMapping> lstOld) {
        lstUpdate.forEach((rsCode, v) -> {
            if (!lstOld.containsKey(rsCode)) {
                saveNewUserMapping(userId, rsTypeCode, v);
            } else {
                updateExistingUserMapping(lstOld.get(rsCode), v);
                lstOld.remove(rsCode);
            }
        });
    }

    private void saveNewUserMapping(String userId, String rsTypeCode, CtgCfgResourceMasterDtoV2 v) {
        CtgCfgResourceMapping entity = mapper.toEntity(v);
        entity.setResourceTypeCode(rsTypeCode);
        entity.setIsActive(1);
        entity.setRecordStatus(Constant.APPROVAL);
        entity.setUserId(userId);
        if (entity.getEffectiveDate() == null) {
            entity.setEffectiveDate(new Date());
        }
        repository.save(entity);
    }

    private void updateExistingUserMapping(CtgCfgResourceMapping entity, CtgCfgResourceMasterDtoV2 v) {
        entity.setEffectiveDate(v.getEffectiveDate());
        entity.setExpiryDate(v.getExpiryDate());
        if (Integer.valueOf(0).equals(entity.getIsActive())) {
            entity.setIsActive(1);
            entity.setIsDelete(0);
        }
        repository.save(entity);
    }

    private void deactivateRemovedUserMappings(Map<String, CtgCfgResourceMapping> lstOld) {
        lstOld.values().forEach(mapping -> {
            mapping.setIsActive(0);
            mapping.setIsDelete(1);
            repository.save(mapping);
        });
    }

    private void saveNewUserMappings(String userId, String rsTypeCode, List<CtgCfgResourceMasterDtoV2> lst) {
        lst.forEach(item -> saveNewUserMapping(userId, rsTypeCode, item));
    }

    @Override
    public void updateGroupCfgResourceMapping(List<CtgCfgResourceMappingRequest> request, String groupId) {
        Map<String, List<CtgCfgResourceMasterDtoV2>> updateResourceCodes = buildUpdateResourceCodesMap(request);
        Map<String, List<CtgCfgResourceMapping>> oldMappingsByType = loadOldGroupMappingsByResourceType(
                groupId, updateResourceCodes.keySet().stream().toList());

        updateResourceCodes.forEach((rsTypeCode, lst) ->
                processGroupResourceType(groupId, rsTypeCode, lst, oldMappingsByType));
    }

    private Map<String, List<CtgCfgResourceMapping>> loadOldGroupMappingsByResourceType(
            String groupId, List<String> resourceTypeCodes) {
        Map<String, List<CtgCfgResourceMapping>> result = new HashMap<>();
        resourceTypeCodes.forEach(code -> {
            List<CtgCfgResourceMapping> list = repository.findByGroupIdAndResourceTypeCodeAndIsActive(groupId, code, null);
            result.put(code, list);
        });
        return result;
    }

    private void processGroupResourceType(String groupId, String rsTypeCode, List<CtgCfgResourceMasterDtoV2> lst,
                                          Map<String, List<CtgCfgResourceMapping>> oldMappingsByType) {
        if (oldMappingsByType.containsKey(rsTypeCode)) {
            processExistingGroupResourceType(groupId, rsTypeCode, lst, oldMappingsByType.get(rsTypeCode));
        } else {
            saveNewGroupMappings(groupId, rsTypeCode, lst);
        }
    }

    private void processExistingGroupResourceType(String groupId, String rsTypeCode,
                                                   List<CtgCfgResourceMasterDtoV2> lst,
                                                   List<CtgCfgResourceMapping> oldMappingsList) {
        Map<String, CtgCfgResourceMasterDtoV2> lstUpdate = lst.stream()
                .collect(Collectors.toMap(CtgCfgResourceMasterDtoV2::getResourceCode, rs -> rs));
        Map<String, CtgCfgResourceMapping> lstOld = oldMappingsList.stream()
                .collect(Collectors.toMap(CtgCfgResourceMapping::getResourceCode, rs -> rs));

        applyRequestedGroupMappings(groupId, rsTypeCode, lstUpdate, lstOld);
        deactivateRemovedUserMappings(lstOld);
    }

    private void applyRequestedGroupMappings(String groupId, String rsTypeCode,
                                             Map<String, CtgCfgResourceMasterDtoV2> lstUpdate,
                                             Map<String, CtgCfgResourceMapping> lstOld) {
        lstUpdate.forEach((rsCode, v) -> {
            if (!lstOld.containsKey(rsCode)) {
                saveNewGroupMapping(groupId, rsTypeCode, v);
            } else {
                updateExistingUserMapping(lstOld.get(rsCode), v);
                lstOld.remove(rsCode);
            }
        });
    }

    private void saveNewGroupMapping(String groupId, String rsTypeCode, CtgCfgResourceMasterDtoV2 v) {
        CtgCfgResourceMapping entity = mapper.toEntity(v);
        entity.setResourceTypeCode(rsTypeCode);
        entity.setIsActive(1);
        entity.setRecordStatus(Constant.APPROVAL);
        entity.setGroupId(groupId);
        if (entity.getEffectiveDate() == null) {
            entity.setEffectiveDate(new Date());
        }
        repository.save(entity);
    }

    private void saveNewGroupMappings(String groupId, String rsTypeCode, List<CtgCfgResourceMasterDtoV2> lst) {
        lst.forEach(item -> saveNewGroupMapping(groupId, rsTypeCode, item));
    }

    @Override
    public List<CtgCfgResourceMapping> findByResourceTypeCode(String resourceTypeCode) {
        String userId = getCurrentUserId();
        return repository.findByUserIdAndResourceTypeCodeAndIsActiveEffective(userId, resourceTypeCode, 1);
    }

    @Override
    public List<ResourceMappingDto> getListOfResourceMappingDto(String resourceTypeCode) {
        return repository.getListOfResourceMappingDto(resourceTypeCode, getCurrentUserId());
    }

    @Override
    public List<CtgCfgResourceMappingDto1> listOrg() {
        return repository.listOrg(getCurrentUserId());

    }

    @Override
    public List<CtgCfgResourceMappingDto1> listArea(String resourceCode) {

        return repository.listArea(getCurrentUserId(), resourceCode);
    }

    @Override
    public List<CtgCfgResourceMappingDto1> findAreaByResourceTypeName(String resourceTypeCode, String orgCode) {
        return repository.findAreaByResourceCode(resourceTypeCode, List.of(orgCode.split(",")), getCurrentUserId());

    }
}
