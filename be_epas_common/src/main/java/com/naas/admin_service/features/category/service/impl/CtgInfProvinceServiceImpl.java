package com.naas.admin_service.features.category.service.impl;

import com.naas.admin_service.core.contants.CommonErrorCode;
import com.naas.admin_service.features.category.dto.CtgInfProvinceDto;
import com.naas.admin_service.features.category.model.CtgInfProvince;
import com.naas.admin_service.features.category.repository.CtgInfProvinceRepository;
import com.naas.admin_service.features.category.service.CtgInfProvinceService;
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
public class CtgInfProvinceServiceImpl implements CtgInfProvinceService {

    private final CtgInfProvinceRepository ctgInfProvinceRepository;

    private final ExcelService excelService;

    @Override
    public Page<CtgInfProvinceDto> getProvinces(Pageable pageable) {
        return ctgInfProvinceRepository.getProvinces(pageable);
    }

    @Override
    public Page<CtgInfProvinceDto> findProvinces(String keyword,Pageable pageable){
        return ctgInfProvinceRepository.findProvinces(keyword,pageable);
    }

    @Override
    public ResponseEntity<ByteArrayResource> exportToExcel(String keyword,String fileName,List<String> label){
        List<CtgInfProvinceDto> ctgInfProvinceDtos=ctgInfProvinceRepository.exportToExcel(keyword);
        return excelService.exportToExcel(ctgInfProvinceDtos,label,CtgInfProvinceDto.class,fileName);
    }

    @Override
    public void createProvince(CtgInfProvinceDto ctgInfProvinceDto) {

        Optional<CtgInfProvince> ctgInfProvince=ctgInfProvinceRepository.findByProvinceCode(ctgInfProvinceDto.getProvinceCode());
        if (ctgInfProvince.isPresent()) {
            throw new BusinessException(CommonErrorCode.PROVINCE_ALREADY_EXISTS);
        }
        ctgInfProvinceRepository.save(new CtgInfProvince(
                ctgInfProvinceDto.getProvinceCode(),
                ctgInfProvinceDto.getProvinceName(),
                ctgInfProvinceDto.getTaxCode(),
                ctgInfProvinceDto.getDescription()
        ));

    }

    @Override
    public void updateProvince(CtgInfProvinceDto ctgInfProvinceDto) {
        Optional<CtgInfProvince> ctgInfProvince=ctgInfProvinceRepository.findByProvinceCode(ctgInfProvinceDto.getProvinceCode());
        if (ctgInfProvince.isEmpty()) {
            throw new BusinessException(ErrorCode.NOT_FOUND);
        }
        ctgInfProvinceRepository.updateProvince(ctgInfProvinceDto.getProvinceCode(),
                ctgInfProvinceDto.getProvinceName(),
                ctgInfProvinceDto.getTaxCode(),
                ctgInfProvinceDto.getDescription());
    }

    @Transactional
    @Override
    public void deleteProvince(String provinceCode) {

        Optional<CtgInfProvince> ctgInfProvince=ctgInfProvinceRepository.findByProvinceCode(provinceCode);
        if (ctgInfProvince.isEmpty()) {
            throw new BusinessException(ErrorCode.NOT_FOUND);
        }

        ctgInfProvinceRepository.deleteCtgInfProvinceByProvinceCode(provinceCode);
    }

    @Override
    public CtgInfProvinceDto getDetailProvince(String provinceCode){
        return ctgInfProvinceRepository.getDetailProvince(provinceCode);
    }

    @Override
    public List<CtgInfProvinceDto> getAll(){
        return ctgInfProvinceRepository.getAll();
    }

    @Override
    public boolean checkExist(String code){
        Optional<CtgInfProvince> ctgInfProvince=ctgInfProvinceRepository.findByProvinceCode(code);
        return ctgInfProvince.isPresent();
    }
}
