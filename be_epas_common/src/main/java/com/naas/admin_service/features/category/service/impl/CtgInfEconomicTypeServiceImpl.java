package com.naas.admin_service.features.category.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.naas.admin_service.core.contants.CommonErrorCode;
import com.naas.admin_service.core.contants.EntityStatus;
import com.naas.admin_service.features.category.dto.CtgInfEconomicTypeDto;
import com.naas.admin_service.features.category.dto.CtgInfEconomicTypeResponse;
import com.naas.admin_service.features.category.mapper.CtgInfEconomicTypeMapper;
import com.naas.admin_service.features.category.model.CtgInfEconomicType;
import com.naas.admin_service.features.category.repository.CtgInfEconomicTypeRepository;
import com.naas.admin_service.features.category.service.CtgInfEconomicTypeService;
import com.naas.admin_service.core.excel.service.ExcelService;
import com.ngvgroup.bpm.core.common.exception.BusinessException;
import com.ngvgroup.bpm.core.common.exception.ErrorCode;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class CtgInfEconomicTypeServiceImpl implements CtgInfEconomicTypeService {

    private final CtgInfEconomicTypeRepository ctgInfEconomicTypeRepository;
    private final ExcelService excelService;
    private final CtgInfEconomicTypeMapper ctgInfEconomicTypeMapper;

    @Override
    public CtgInfEconomicTypeDto findOne(Long id) {
        CtgInfEconomicType entity = ctgInfEconomicTypeRepository.findById(id)
                .orElseThrow(() -> new BusinessException(ErrorCode.NOT_FOUND, " loại hình kinh tế với id: " + id));
        return ctgInfEconomicTypeMapper.toDto(entity);
    }

    @Override
    @Transactional
    public void create(CtgInfEconomicTypeDto dto) {

        Optional<CtgInfEconomicType> ctgInfEconomicType = ctgInfEconomicTypeRepository.findByEconomicTypeCode(dto.getEconomicTypeCode());
        if (ctgInfEconomicType.isPresent()) {
            throw new BusinessException(CommonErrorCode.ECONOMIC_TYPE_ALREADY_EXISTS);
        }
        ctgInfEconomicTypeRepository.save(new CtgInfEconomicType(
                dto.getIsActive(),
                dto.getOrgCode(),
                dto.getEconomicTypeCode(),
                dto.getEconomicTypeName(),
                dto.getDescription()));
    }

    @Override
    @Transactional
    public void update(Long id, CtgInfEconomicTypeDto dto) {
        Optional<CtgInfEconomicType> ctgInfEconomicType = ctgInfEconomicTypeRepository.findByEconomicTypeCode(dto.getEconomicTypeCode());
        if (ctgInfEconomicType.isEmpty()) {
            throw new BusinessException(ErrorCode.NOT_FOUND);
        }

        ctgInfEconomicTypeRepository.updateEconomicType(
                dto.getIsActive(),
                dto.getOrgCode(),
                dto.getEconomicTypeCode(),
                dto.getEconomicTypeName(),
                dto.getDescription()
        );

    }

    @Override
    @Transactional
    public void delete(Long id) {
        CtgInfEconomicType entity = ctgInfEconomicTypeRepository.findById(id)
                .orElseThrow(() -> new BusinessException(ErrorCode.NOT_FOUND, " loại hình kinh tế với id: " + id));
        entity.setIsDelete(EntityStatus.IsDelete.DELETED.getValue());
        ctgInfEconomicTypeRepository.delete(entity);
    }

    @Override
    public Page<CtgInfEconomicTypeResponse> searchAll(String filter, Pageable pageable, boolean isExport) {
        Page<CtgInfEconomicTypeResponse> res = ctgInfEconomicTypeRepository.searchCtgInfEconomicType(filter, pageable);
        res.forEach(item -> {
            item.setStatus(EntityStatus.IsActive.ACTIVE.getValue() == item.getIsActive() ? "Hiệu lực" : "Hết hiệu lực");
            if (isExport) {
                item.setIsActive(null);
            }
        });
        return res;
    }

    @Override
    public ResponseEntity<ByteArrayResource> exportToExcel(String filter, List<String> labels) {
        String fileName = "ListCtgCfgEconomicType.xlsx";
        List<CtgInfEconomicTypeResponse> responses = ctgInfEconomicTypeRepository.exportToExcel(filter);

        responses.forEach(res -> {
            if (EntityStatus.IsActive.ACTIVE.getValue() == res.getIsActive()) {
                res.setStatus("Hiệu lực");
            } else {
                res.setStatus("Hết hiệu lực");
            }
            res.setIsActive(null);
            res.setOrgCode(null);
        });
        return excelService.exportToExcel(responses, labels, CtgInfEconomicTypeResponse.class, fileName);
    }

    @Override
    public boolean checkExist(String code) {
        List<CtgInfEconomicType> entity = ctgInfEconomicTypeRepository.findCtgInfEconomicTypesByEconomicTypeCodeAndIsDelete(code, EntityStatus.IsDelete.NOT_DELETED.getValue());
        return !entity.isEmpty();
    }

    @Override
    public CtgInfEconomicType getEconomicByTypeCode(String typeCode) {
        Optional<CtgInfEconomicType> ctgInfEconomicType = ctgInfEconomicTypeRepository.findByEconomicTypeCode(typeCode);
        if (ctgInfEconomicType.isEmpty()) {
            throw new BusinessException(ErrorCode.NOT_FOUND);
        }
        return ctgInfEconomicType.get();
    }

    @Override
    public List<CtgInfEconomicTypeResponse> getAll() {
        return ctgInfEconomicTypeRepository.getAllData();
    }


}
