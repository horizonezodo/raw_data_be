package com.naas.category_service.service.impl;

import com.naas.category_service.constant.EntityStatus;
import com.naas.category_service.dto.CtgInfEconomicType.CtgInfEconomicTypeDto;
import com.naas.category_service.dto.CtgInfEconomicType.CtgInfEconomicTypeResponse;
import com.naas.category_service.exception.CategoryErrorCode;
import com.naas.category_service.mapper.CtgInfEconomicTypeMapper;
import com.naas.category_service.model.CtgInfEconomicType;
import com.naas.category_service.repository.CtgInfEconomicTypeRepository;
import com.naas.category_service.service.CtgInfEconomicTypeService;
import com.ngvgroup.bpm.core.exception.BusinessException;
import com.ngvgroup.bpm.core.exception.ErrorCode;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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
//        boolean isExist = checkExist(dto.getEconomicTypeCode());
//        if (isExist) {
//            throw new BusinessException(ErrorCode.CONFLICT, dto.getEconomicTypeCode());
//        }
//        CtgInfEconomicType entity = ctgInfEconomicTypeMapper.toEntity(dto);
//        entity.setRecordStatus(EntityStatus.RecordStatus.APPROVAL.getValue());
//        entity.setApprovedBy(entity.getCurrentUsername());
//        entity.setApprovedDate(entity.getTimestampNow());
//        ctgInfEconomicTypeRepository.save(entity);

        Optional<CtgInfEconomicType> ctgInfEconomicType=ctgInfEconomicTypeRepository.findByEconomicTypeCode(dto.getEconomicTypeCode());
        if(ctgInfEconomicType.isPresent()){
            throw new BusinessException(CategoryErrorCode.ECONOMIC_TYPE_ALREADY_EXISTS);
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
//        CtgInfEconomicType entity = ctgInfEconomicTypeRepository.findById(id)
//                .orElseThrow(() -> new BusinessException(ErrorCode.NOT_FOUND, " loại hình kinh tế với id: " + id));
//        ctgInfEconomicTypeMapper.updateCtgInfEconomicTypeFromDto(dto, entity);
//        ctgInfEconomicTypeRepository.save(entity);
        Optional<CtgInfEconomicType> ctgInfEconomicType=ctgInfEconomicTypeRepository.findByEconomicTypeCode(dto.getEconomicTypeCode());
        if(!ctgInfEconomicType.isPresent()){
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
        Page<CtgInfEconomicTypeResponse> res =  ctgInfEconomicTypeRepository.searchCtgInfEconomicType(filter,pageable);
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

        responses.forEach((res)->{
            if(res.getIsActive()){
                res.setStatus("Hiệu lực");
            }
            else {
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
        if (!entity.isEmpty()) {
            return true;
        }
        return false;
    }
}
