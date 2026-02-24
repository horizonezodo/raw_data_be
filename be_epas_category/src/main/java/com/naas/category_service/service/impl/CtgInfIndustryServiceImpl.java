package com.naas.category_service.service.impl;

import com.naas.category_service.constant.EntityStatus;
import com.naas.category_service.dto.CtgInfIndustry.CtgInfIndustryDto;
import com.naas.category_service.dto.CtgInfIndustry.CtgInfIndustryResponse;
import com.naas.category_service.exception.CategoryErrorCode;
import com.naas.category_service.mapper.CtgInfIndustryMapper;
import com.naas.category_service.model.CtgInfIndustry;
import com.naas.category_service.repository.CtgInfIndustryRepository;
import com.naas.category_service.service.CtgInfIndustryService;
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
//        boolean isExist = checkInfIndustryCodeExist(dto.getIndustryCode());
//        if(isExist) {
//            throw new BusinessException(ErrorCode.CONFLICT, dto.getIndustryCode());
//        }
//        CtgInfIndustry entity = ctgInfIndustryMapper.toEntity(dto);
//        entity.setRecordStatus(EntityStatus.RecordStatus.APPROVAL.getValue());
//        entity.setApprovedBy(entity.getCurrentUsername());
//        entity.setApprovedDate(entity.getTimestampNow());
//        ctgInfIndustryRepository.save(entity);

        Optional<CtgInfIndustry> ctgInfIndustry=ctgInfIndustryRepository.findByIndustryCode(dto.getIndustryCode());
        if(ctgInfIndustry.isPresent()){
            throw new BusinessException(CategoryErrorCode.INDUSTRY_ALREADY_EXISTS);
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
//        CtgInfIndustry entity = ctgInfIndustryRepository.findById(id)
//                .orElseThrow(() -> new BusinessException(ErrorCode.NOT_FOUND, " ngành kinh tế với id: " + id));
//        ctgInfIndustryMapper.updateCtgInfIndustryFromDto(dto, entity);
//        ctgInfIndustryRepository.save(entity);
        Optional<CtgInfIndustry> ctgInfIndustry=ctgInfIndustryRepository.findByIndustryCode(dto.getIndustryCode());
        if(!ctgInfIndustry.isPresent()){
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
        responses.forEach((res)->{
            if(res.getIsActive()){
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
        if (!entity.isEmpty()) {
            return true;
        }
        return false;
    }

}
