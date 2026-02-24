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
import com.naas.admin_service.features.category.dto.CtgInfIndustryDto;
import com.naas.admin_service.features.category.dto.CtgInfIndustryResponse;
import com.naas.admin_service.features.category.mapper.CtgInfIndustryMapper;
import com.naas.admin_service.features.category.model.CtgInfIndustry;
import com.naas.admin_service.features.category.repository.CtgInfIndustryRepository;
import com.naas.admin_service.features.category.service.CtgInfIndustryService;
import com.naas.admin_service.core.excel.service.ExcelService;
import com.ngvgroup.bpm.core.common.exception.BusinessException;
import com.ngvgroup.bpm.core.common.exception.ErrorCode;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class CtgInfIndustryServiceImpl implements CtgInfIndustryService {

    private final CtgInfIndustryRepository ctgInfIndustryRepository;
    private final ExcelService excelService;
    private final CtgInfIndustryMapper ctgInfIndustryMapper;

    @Override
    public CtgInfIndustryDto findOne(Long id) {
        CtgInfIndustry entity = ctgInfIndustryRepository.findById(id)
                .orElseThrow(() -> new BusinessException(ErrorCode.NOT_FOUND, " ngành kinh tế với id: " + id));
        return ctgInfIndustryMapper.toDto(entity);
    }

    @Override
    @Transactional
    public void create(CtgInfIndustryDto dto) {

        Optional<CtgInfIndustry> ctgInfIndustry=ctgInfIndustryRepository.findByIndustryCode(dto.getIndustryCode());
        if(ctgInfIndustry.isPresent()){
            throw new BusinessException(CommonErrorCode.INDUSTRY_ALREADY_EXISTS);
        }
        ctgInfIndustryRepository.save(new CtgInfIndustry(
                dto.getOrgCode(),
                dto.getIndustryCode(),
                dto.getIndustryName(),
                dto.getIsActive(),
                dto.getDescription()
        ));
    }

    @Override
    @Transactional
    public void update(Long id, CtgInfIndustryDto dto) {
        Optional<CtgInfIndustry> ctgInfIndustry=ctgInfIndustryRepository.findByIndustryCode(dto.getIndustryCode());
        if(ctgInfIndustry.isEmpty()){
            throw new BusinessException(ErrorCode.NOT_FOUND);
        }
        ctgInfIndustryRepository.updateCtgInfIndustry(
                dto.getIsActive(),
                dto.getIndustryCode(),
                dto.getOrgCode(),
                dto.getIndustryName(),
                dto.getDescription()
        );

    }

    @Override
    @Transactional
    public void delete(Long id) {
        CtgInfIndustry entity = ctgInfIndustryRepository.findById(id)
                .orElseThrow(() -> new BusinessException(ErrorCode.NOT_FOUND, " ngành kinh tế với id: " + id));
        entity.setIsDelete(EntityStatus.IsDelete.DELETED.getValue());
        ctgInfIndustryRepository.delete(entity);
    }

    @Override
    public Page<CtgInfIndustryResponse> searchAll(String filter, Pageable pageable, boolean isExport) {
        Page<CtgInfIndustryResponse> res =  ctgInfIndustryRepository.searchCtgInfIndustry(filter,pageable);
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
        String fileName = "ListCtgInfIndustry.xlsx";
        List<CtgInfIndustryResponse> responses = ctgInfIndustryRepository.exportToExcel(filter);
        responses.forEach( res ->{
            if(EntityStatus.IsActive.ACTIVE.getValue() == res.getIsActive()){
                res.setStatus("Hiệu lực");
            }
            else {
                res.setStatus("Hết hiệu lực");
            }
            res.setIsActive(null);
        });
        return excelService.exportToExcel(responses, labels, CtgInfIndustryResponse.class, fileName);
    }

    @Override
    public boolean checkInfIndustryCodeExist(String code) {
        List<CtgInfIndustry> entity = ctgInfIndustryRepository.findCtgInfIndustrysByIndustryCodeAndIsDelete(code, EntityStatus.IsDelete.NOT_DELETED.getValue());
        return !entity.isEmpty();
    }

    @Override
    public CtgInfIndustryDto getByIndustryCode(String code) {
        Optional<CtgInfIndustry> ctgInfIndustry=ctgInfIndustryRepository.findByIndustryCode(code);
        if(ctgInfIndustry.isEmpty()){
            throw new BusinessException(ErrorCode.NOT_FOUND);
        }
        return this.ctgInfIndustryMapper.toDto(ctgInfIndustry.get());
    }

    @Override
    public List<CtgInfIndustryDto> getAll() {
        return this.ctgInfIndustryRepository.getAllData();
    }

}
