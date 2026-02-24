package com.naas.category_service.service.impl;

import com.naas.category_service.constant.EntityStatus;
import com.naas.category_service.dto.CtgCfgCollateralType.CtgCfgCollateralTypeDto;
import com.naas.category_service.dto.CtgCfgCollateralType.CtgCfgCollateralTypeResponse;
import com.naas.category_service.mapper.CtgCfgCollateralTypeMapper;
import com.naas.category_service.model.CtgCfgCollateralType;
import com.naas.category_service.repository.CtgCfgCollateralTypeRepository;
import com.naas.category_service.service.CtgCfgCollateralTypeService;
import com.ngvgroup.bpm.core.exception.BusinessException;
import com.ngvgroup.bpm.core.exception.ErrorCode;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.List;
import java.util.Locale;

@Service
@RequiredArgsConstructor
public class CtgCfgCollateralTypeServiceImpl implements CtgCfgCollateralTypeService {
    private final CtgCfgCollateralTypeRepository ctgCfgCollateralTypeRepository;
    private final CtgCfgCollateralTypeMapper ctgCfgCollateralTypeMapper;
    private final ExcelService excelService;


    @Override
    public CtgCfgCollateralTypeDto findOne(Long id) {
        CtgCfgCollateralType entity =  ctgCfgCollateralTypeRepository.findById(id)
                .orElseThrow(() -> new BusinessException(ErrorCode.NOT_FOUND, " loại tài sản đảm bảo với id: " + id));
        entity.setRecordStatus(EntityStatus.RecordStatus.APPROVAL.getValue());
        return ctgCfgCollateralTypeMapper.toDto(entity);
    }

    @Override
    @Transactional
    public void create(CtgCfgCollateralTypeDto ctgCfgCollateralTypeDto) {
        boolean isExist = checkExist(ctgCfgCollateralTypeDto.getCollateralTypeCode());
        if (isExist) {
            throw new BusinessException(ErrorCode.CONFLICT, ctgCfgCollateralTypeDto.getCollateralTypeCode());
        }
        CtgCfgCollateralType entity = ctgCfgCollateralTypeMapper.toEntity(ctgCfgCollateralTypeDto);
        entity.setRecordStatus(EntityStatus.RecordStatus.APPROVAL.getValue());
        entity.setApprovedBy(entity.getCurrentUsername());
        entity.setApprovedDate(entity.getTimestampNow());
        ctgCfgCollateralTypeRepository.save(entity);
    }

    @Override
    @Transactional
    public void update(Long id, CtgCfgCollateralTypeDto ctgCfgCollateralTypeDto) {
        CtgCfgCollateralType entity = ctgCfgCollateralTypeRepository.findById(id)
                .orElseThrow(() -> new BusinessException(ErrorCode.NOT_FOUND, " loại tài sản đảm bảo với id: " + id));
        ctgCfgCollateralTypeMapper.updateCtgCfgCollateralTypeFromDto(ctgCfgCollateralTypeDto, entity);
        ctgCfgCollateralTypeRepository.save(entity);
    }

    @Override
    @Transactional
    public void delete(Long id) {
        CtgCfgCollateralType entity = ctgCfgCollateralTypeRepository.findById(id)
                .orElseThrow(() -> new BusinessException(ErrorCode.NOT_FOUND, " loại tài sản đảm bảo với id: " + id));
        entity.setIsDelete(EntityStatus.IsDelete.DELETED.getValue());
        ctgCfgCollateralTypeRepository.delete(entity);
    }


    @Override
    public Page<CtgCfgCollateralTypeResponse> searchAll(String filter, Pageable pageable, boolean isExport) {
        Page<CtgCfgCollateralTypeResponse> res = ctgCfgCollateralTypeRepository.search(filter, pageable);

        DecimalFormatSymbols symbols = new DecimalFormatSymbols(new Locale("vi", "VN"));
        symbols.setGroupingSeparator('.');
        symbols.setDecimalSeparator(',');
        DecimalFormat formatter = new DecimalFormat("#,##0.##", symbols);

        res.getContent().forEach(item -> {
            item.setStatus(EntityStatus.IsActive.ACTIVE.getValue() == item.getIsActive() ? "Hiệu lực" : "Hết hiệu lực");

            if (isExport) {
                item.setIsActive(null);
            }


            if (item.getDeductionRatio() != null) {
                item.setDeductionRatioFormat(formatter.format(item.getDeductionRatio()));
            } else {
                item.setDeductionRatioFormat(null);
            }


            if (item.getGuaranteeRatio() != null) {
                item.setGuaranteeRatioFormat(formatter.format(item.getGuaranteeRatio()));
            } else {
                item.setGuaranteeRatioFormat(null);
            }

            if (item.getRiskCoefficient() != null) {
                item.setRiskCoefficientFormat(formatter.format(item.getRiskCoefficient()));
            } else {
                item.setRiskCoefficientFormat(null);
            }
        });

        return res;
    }


    @Override
    public ResponseEntity<ByteArrayResource> exportToExcel(String filter, List<String> labels) {
        String fileName = "ListCtgCfgCollateralType.xlsx";
        List<CtgCfgCollateralTypeResponse> res = ctgCfgCollateralTypeRepository.exportToExcel(filter);

        DecimalFormatSymbols symbols = new DecimalFormatSymbols(new Locale("vi", "VN"));
        symbols.setGroupingSeparator('.');
        symbols.setDecimalSeparator(',');
        DecimalFormat formatter = new DecimalFormat("#,##0.##", symbols);

        res.forEach(item -> {
            item.setStatus(EntityStatus.IsActive.ACTIVE.getValue() == item.getIsActive() ? "Hiệu lực" : "Hết hiệu lực");


            if (item.getDeductionRatio() != null) {
                item.setDeductionRatioFormat(formatter.format(item.getDeductionRatio()));
            } else {
                item.setDeductionRatioFormat(null);
            }


            if (item.getGuaranteeRatio() != null) {
                item.setGuaranteeRatioFormat(formatter.format(item.getGuaranteeRatio()));
            } else {
                item.setGuaranteeRatioFormat(null);
            }

            if (item.getRiskCoefficient() != null) {
                item.setRiskCoefficientFormat(formatter.format(item.getRiskCoefficient()));
            } else {
                item.setRiskCoefficientFormat(null);
            }

            item.setDeductionRatio(null);
            item.setGuaranteeRatio(null);
            item.setRiskCoefficient(null);
            item.setIsActive(null);
        });


        return excelService.exportToExcel(res, labels, CtgCfgCollateralTypeResponse.class, fileName);
    }

    @Override
    public boolean checkExist(String code) {
        List<CtgCfgCollateralType> entity = ctgCfgCollateralTypeRepository.findCtgCfgCollateralTypeByCollateralTypeCodeAndIsDelete(code, EntityStatus.IsDelete.NOT_DELETED.getValue());
        if (!entity.isEmpty()) {
            return true;
        }
        return false;
    }
}
