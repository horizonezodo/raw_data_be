package com.naas.admin_service.features.area_type.service.impl;

import com.naas.admin_service.core.contants.CommonErrorCode;
import com.naas.admin_service.core.contants.Constant;
import com.ngvgroup.bpm.core.common.excel.ExportExcel;
import com.ngvgroup.bpm.core.common.exception.BusinessException;
import com.ngvgroup.bpm.core.common.exception.ErrorCode;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import com.naas.admin_service.features.area_type.dto.ComInfAreaTypeDto;
import com.naas.admin_service.features.area_type.mapper.ComInfAreaTypeMapper;
import com.naas.admin_service.features.area_type.model.ComInfAreaType;
import com.naas.admin_service.features.area_type.repository.ComInfAreaTypeRepository;
import com.naas.admin_service.features.area_type.service.ComInfAreaTypeService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Optional;

@Service
@AllArgsConstructor
public class ComInfAreaTypeServiceImpl implements ComInfAreaTypeService {

    private final ComInfAreaTypeRepository comInfAreaTypeRepository;
    private final ComInfAreaTypeMapper comInfAreaTypeMapper;
    private final ExportExcel exportExcel;

    @Override
    public Page<ComInfAreaTypeDto> getAreaTypes(Pageable pageable) {

        return comInfAreaTypeRepository.getAreaTypes(pageable);
    }

    @Override
    public Page<ComInfAreaTypeDto> findAreaTypes(String keyword,Pageable pageable) {

        return comInfAreaTypeRepository.findAreaTypes(keyword, pageable);
    }

    @Override
    public ResponseEntity<byte[]> exportToExcel(String keyword, String fileName, List<String> labels) {
        try {
            List<ComInfAreaTypeDto> comInfAreaTypeDtos = comInfAreaTypeRepository.exportToExcel(keyword);
            String safeFileName = URLEncoder.encode(fileName, StandardCharsets.UTF_8);

            return exportExcel.exportExcel(comInfAreaTypeDtos, safeFileName);
        }
        catch (Exception ex) {
            throw new BusinessException(CommonErrorCode.WRITE_EXCEL_ERROR, ex);
        }
    }

    @Override
    @Transactional
    public void createCtgComAreaType(ComInfAreaTypeDto comInfAreaTypeDto) {

        Optional<ComInfAreaType> ctgComAreaType = comInfAreaTypeRepository.findByAreaTypeCode(comInfAreaTypeDto.getAreaTypeCode());
        if (ctgComAreaType.isPresent()) {
            throw new BusinessException(ErrorCode.CONFLICT);
        }

        ComInfAreaType areaType = new ComInfAreaType();
        areaType.setOrgCode(Constant.ORG);
        areaType.setAreaTypeCode(comInfAreaTypeDto.getAreaTypeCode());
        areaType.setAreaTypeName(comInfAreaTypeDto.getAreaTypeName());
        areaType.setDescription(comInfAreaTypeDto.getDescription());

        comInfAreaTypeRepository.save(areaType);
    }

    @Override
    public void updateCtgComAreaType(ComInfAreaTypeDto comInfAreaTypeDto) {
        Optional<ComInfAreaType> ctgComAreaType = comInfAreaTypeRepository.findByAreaTypeCode(comInfAreaTypeDto.getAreaTypeCode());
        if (ctgComAreaType.isEmpty()) {
            throw new BusinessException(ErrorCode.NOT_FOUND,"Mã khu vực");
        }
        comInfAreaTypeRepository.updateAreaTypeCode(
                comInfAreaTypeDto.getAreaTypeCode(),
                comInfAreaTypeDto.getAreaTypeName(),

                comInfAreaTypeDto.getDescription()
        );
    }

    @Override
    @Transactional
    public void deleteCtgComAreaType(String areaTypeCode) {
        Optional<ComInfAreaType> ctgComAreaType = comInfAreaTypeRepository.findByAreaTypeCode(areaTypeCode);
        if (ctgComAreaType.isEmpty()) {
            throw new BusinessException(ErrorCode.NOT_FOUND,"Mã khu vực");
        }
        comInfAreaTypeRepository.deleteCtgComAreaTypeByAreaTypeCode(areaTypeCode);
    }

    @Override
    public ComInfAreaTypeDto getDetail(String areaTypeCode) {
        return comInfAreaTypeRepository.getDetailAreaType(areaTypeCode);
    }

    @Override
    public List<ComInfAreaTypeDto> getAll() {
        return comInfAreaTypeRepository.findAll().stream()
                .map(comInfAreaTypeMapper::toDto)
                .toList();
    }

    @Override
    public List<ComInfAreaTypeDto> getDistinctAreaTypes(){
        return comInfAreaTypeRepository.getDistinctAreaTypes();
    }

}
