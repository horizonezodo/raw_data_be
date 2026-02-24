package com.naas.admin_service.features.category.service.impl;

import com.naas.admin_service.core.contants.CommonErrorCode;
import com.naas.admin_service.features.category.dto.CtgInfWardDto;
import com.naas.admin_service.features.category.model.CtgInfWard;
import com.naas.admin_service.features.category.repository.CtgInfWardRepository;
import com.naas.admin_service.features.category.service.CtgInfWardService;
import com.naas.admin_service.core.excel.service.ExcelService;
import com.ngvgroup.bpm.core.common.exception.BusinessException;
import com.ngvgroup.bpm.core.common.exception.ErrorCode;
import lombok.AllArgsConstructor;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@AllArgsConstructor
public class CtgInfWardServiceImpl implements CtgInfWardService {

    private final CtgInfWardRepository ctgInfWardRepository;

    private final ExcelService excelService;

    @Override
    public Page<CtgInfWardDto> getWards(Pageable pageable) {
        return ctgInfWardRepository.getWards(pageable);
    }

    @Override
    public Page<CtgInfWardDto> searchWards(String filter, Pageable pageable) {
        return ctgInfWardRepository.searchWards(filter, pageable);
    }

    @Override
    public ResponseEntity<ByteArrayResource> exportToExcel(String filter, String fileName, List<String> labels){

        List<CtgInfWardDto> ctgInfWardDtos = ctgInfWardRepository.exportToExcel(filter);
        return excelService.exportToExcel(ctgInfWardDtos,labels,CtgInfWardDto.class,fileName);

    }

    @Override
    public void create(CtgInfWardDto ctgInfWardDto) {

        Optional<CtgInfWard> ctgInfWard=ctgInfWardRepository.findByWardCode(ctgInfWardDto.getWardCode());
        if (ctgInfWard.isPresent()) {
            throw new BusinessException(CommonErrorCode.WARD_ALREADY_EXISTS);
        }

        ctgInfWardRepository.save(new CtgInfWard(
                ctgInfWardDto.getWardCode(),
                ctgInfWardDto.getWardName(),
                ctgInfWardDto.getProvinceCode(),
                ctgInfWardDto.getDescription()
        ));
    }

    @Override
    public void update(CtgInfWardDto ctgInfWardDto) {
        Optional<CtgInfWard> ctgInfWard=ctgInfWardRepository.findByWardCode(ctgInfWardDto.getWardCode());
        if (ctgInfWard.isEmpty()) {
            throw new BusinessException(ErrorCode.NOT_FOUND);
        }
        ctgInfWardRepository.updateWard(
                ctgInfWardDto.getWardCode(),
                ctgInfWardDto.getWardName(),
                ctgInfWardDto.getProvinceCode(),
                ctgInfWardDto.getDescription()
        );
    }

    @Transactional
    @Override
    public void delete(String warCode){
        Optional<CtgInfWard> ctgInfWard=ctgInfWardRepository.findByWardCode(warCode);
        if (ctgInfWard.isEmpty()) {
            throw new BusinessException(ErrorCode.NOT_FOUND);
        }

        ctgInfWardRepository.deleteCtgInfWardByWardCode(warCode);
    }

    @Override
    public CtgInfWardDto getDetail(String warCode){
        return ctgInfWardRepository.getDetail(warCode);
    }

    @Override
    public boolean checkExist(String code){
        Optional<CtgInfWard> ctgInfWard=ctgInfWardRepository.findByWardCode(code);
        return ctgInfWard.isPresent();
    }

    @Override
    public List<CtgInfWardDto> getAll() {
        return ctgInfWardRepository.getAll();
    }

}
