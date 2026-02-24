package ngvgroup.com.loan.feature.collateral_type.service.impl;

import com.ngvgroup.bpm.core.common.excel.ExportExcel;
import com.ngvgroup.bpm.core.common.exception.BusinessException;
import com.ngvgroup.bpm.core.common.exception.ErrorCode;
import lombok.RequiredArgsConstructor;
import ngvgroup.com.loan.core.constant.EntityStatus;
import ngvgroup.com.loan.core.constant.LoanErrorCode;
import ngvgroup.com.loan.core.constant.LoanVariableConstants;
import ngvgroup.com.loan.feature.collateral_type.dto.CtgCfgCollateralTypeDto;
import ngvgroup.com.loan.feature.collateral_type.dto.CtgCfgCollateralTypeResponse;
import ngvgroup.com.loan.feature.collateral_type.mapper.CtgCfgCollateralTypeMapper;
import ngvgroup.com.loan.feature.collateral_type.model.CtgCfgCollateralType;
import ngvgroup.com.loan.feature.collateral_type.repository.CtgCfgCollateralTypeRepository;
import ngvgroup.com.loan.feature.collateral_type.service.CtgCfgCollateralTypeService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.List;
import java.util.Locale;

@Service
@RequiredArgsConstructor
public class CtgCfgCollateralTypeServiceImpl implements CtgCfgCollateralTypeService {
    private final CtgCfgCollateralTypeRepository ctgCfgCollateralTypeRepository;
    private final CtgCfgCollateralTypeMapper ctgCfgCollateralTypeMapper;
    private final ExportExcel exportExcel;


    @Override
    public CtgCfgCollateralTypeDto findOne(Long id) {
        CtgCfgCollateralType entity =  ctgCfgCollateralTypeRepository.findById(id)
                .orElseThrow(() -> new BusinessException(ErrorCode.NOT_FOUND, LoanVariableConstants.MSG_ERR + id));
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
        ctgCfgCollateralTypeRepository.save(entity);
    }

    @Override
    @Transactional
    public void update(Long id, CtgCfgCollateralTypeDto ctgCfgCollateralTypeDto) {
        CtgCfgCollateralType entity = ctgCfgCollateralTypeRepository.findById(id)
                .orElseThrow(() -> new BusinessException(ErrorCode.NOT_FOUND, LoanVariableConstants.MSG_ERR + id));
        ctgCfgCollateralTypeMapper.updateCtgCfgCollateralTypeFromDto(ctgCfgCollateralTypeDto, entity);
        ctgCfgCollateralTypeRepository.save(entity);
    }

    @Override
    @Transactional
    public void delete(Long id) {
        CtgCfgCollateralType entity = ctgCfgCollateralTypeRepository.findById(id)
                .orElseThrow(() -> new BusinessException(ErrorCode.NOT_FOUND, LoanVariableConstants.MSG_ERR + id));
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
            item.setStatus(Boolean.TRUE.equals(item.getIsActive()) ? "Hiệu lực" : "Hết hiệu lực");

            if (isExport) {
                item.setIsActive(null);
            }


            checkCtgCfgCollateralType(formatter, item);
        });

        return res;
    }

    private void checkCtgCfgCollateralType(DecimalFormat formatter, CtgCfgCollateralTypeResponse item) {
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
    }


    @Override
    public ResponseEntity<byte[]> exportToExcel(String filter, List<String> labels) {
        try {
            String fileName = URLEncoder.encode("ListCtgCfgCollateralType.xlsx", StandardCharsets.UTF_8);
            List<CtgCfgCollateralTypeResponse> res = ctgCfgCollateralTypeRepository.exportToExcel(filter);

            DecimalFormatSymbols symbols = new DecimalFormatSymbols(new Locale("vi", "VN"));
            symbols.setGroupingSeparator('.');
            symbols.setDecimalSeparator(',');
            DecimalFormat formatter = new DecimalFormat("#,##0.##", symbols);

            res.forEach(item -> {
                item.setStatus(Boolean.TRUE.equals(item.getIsActive()) ? "Hiệu lực" : "Hết hiệu lực");


                checkCtgCfgCollateralType(formatter, item);

                item.setDeductionRatio(null);
                item.setGuaranteeRatio(null);
                item.setRiskCoefficient(null);
                item.setIsActive(null);
            });

            return exportExcel.exportExcel(res, fileName);
        }
        catch (Exception ex) {
            throw new BusinessException(LoanErrorCode.WRITE_EXCEL_ERROR, ex);
        }
    }

    @Override
    public boolean checkExist(String code) {
        List<CtgCfgCollateralType> entity = ctgCfgCollateralTypeRepository
                .findCtgCfgCollateralTypeByCollateralTypeCodeAndIsDelete(code, EntityStatus.IsDelete.NOT_DELETED.getValue());
        return !entity.isEmpty();
    }
}