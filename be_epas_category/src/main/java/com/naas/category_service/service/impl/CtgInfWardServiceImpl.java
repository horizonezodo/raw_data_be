package com.naas.category_service.service.impl;

import com.naas.category_service.dto.CtgInfWard.CtgInfWardDto;
import com.naas.category_service.exception.CategoryErrorCode;
import com.naas.category_service.model.CtgInfProvince;
import com.naas.category_service.model.CtgInfWard;
import com.naas.category_service.repository.CtgInfWardRepository;
import com.naas.category_service.service.CtgInfWardService;
import com.ngvgroup.bpm.core.exception.BusinessException;
import com.ngvgroup.bpm.core.exception.ErrorCode;
import lombok.AllArgsConstructor;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.security.PublicKey;
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
            throw new BusinessException(CategoryErrorCode.WARD_ALREADY_EXISTS);
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
        if (!ctgInfWard.isPresent()) {
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
        if (!ctgInfWard.isPresent()) {
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
        if (ctgInfWard.isPresent()) {
            return true;
        }
        return false;
    }

}
