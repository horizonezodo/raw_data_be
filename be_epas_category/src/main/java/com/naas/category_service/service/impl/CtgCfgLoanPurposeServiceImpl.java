package com.naas.category_service.service.impl;

import com.naas.category_service.constant.EntityStatus;
import com.naas.category_service.dto.CtgCfgLoanPurpose.CtgCfgLoanPurposeDto;
import com.naas.category_service.dto.CtgCfgLoanPurpose.CtgCfgLoanPurposeResponse;
import com.naas.category_service.exception.CategoryErrorCode;
import com.naas.category_service.mapper.CtgCfgLoanPurposeMapper;
import com.naas.category_service.model.CtgCfgLoanPurpose;
import com.naas.category_service.repository.CtgCfgLoanPurposeRepository;
import com.naas.category_service.service.CtgCfgLoanPurposeService;
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
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class CtgCfgLoanPurposeServiceImpl implements CtgCfgLoanPurposeService {
    private final CtgCfgLoanPurposeRepository ctgCfgLoanPurposeRepository;
    private final CtgCfgLoanPurposeMapper ctgCfgLoanPurposeMapper;
    private final ExcelService excelService;


    @Override
    public CtgCfgLoanPurposeDto findOne(Long id) {
        CtgCfgLoanPurpose entity=  ctgCfgLoanPurposeRepository.findById(id)
                .orElseThrow(() -> new BusinessException(ErrorCode.NOT_FOUND, " mục đích vay vốn với id: " + id));
        return ctgCfgLoanPurposeMapper.toDto(entity);
    }

    @Override
    @Transactional
    public void create(CtgCfgLoanPurposeDto ctgCfgLoanPurposeDto) {
//        boolean isExist = checkExist(ctgCfgLoanPurposeDto.getPurposeCode());
//        if (isExist) {
//            throw new BusinessException(ErrorCode.CONFLICT, ctgCfgLoanPurposeDto.getPurposeCode());
//        }
//        CtgCfgLoanPurpose entity = ctgCfgLoanPurposeMapper.toEntity(ctgCfgLoanPurposeDto);
//        entity.setRecordStatus(EntityStatus.RecordStatus.APPROVAL.getValue());
//        entity.setApprovedBy(entity.getCurrentUsername());
//        entity.setApprovedDate(entity.getTimestampNow());
//        ctgCfgLoanPurposeRepository.save(entity);
        Optional<CtgCfgLoanPurpose> ctgCfgLoanPurpose=ctgCfgLoanPurposeRepository.findCtgCfgLoanPurposeByPurposeCode(ctgCfgLoanPurposeDto.getPurposeCode());
        if(ctgCfgLoanPurpose.isPresent()){
            throw new BusinessException(CategoryErrorCode.LOAN_PURPOSE_ALREADY_EXISTS);
        }

        ctgCfgLoanPurposeRepository.save(new CtgCfgLoanPurpose(
                ctgCfgLoanPurposeDto.getIsActive(),
                ctgCfgLoanPurposeDto.getOrgCode(),
                ctgCfgLoanPurposeDto.getPurposeCode(),
                ctgCfgLoanPurposeDto.getPurposeName(),
                ctgCfgLoanPurposeDto.getRiskLevel(),
                ctgCfgLoanPurposeDto.getLoanLimitMin(),
                ctgCfgLoanPurposeDto.getLoanLimitMax(),
                ctgCfgLoanPurposeDto.getDescription()
        ));

    }

    @Override
    @Transactional
    public void update(Long id, CtgCfgLoanPurposeDto ctgCfgLoanPurposeDto) {
//        CtgCfgLoanPurpose entity=  ctgCfgLoanPurposeRepository.findById(id)
//                .orElseThrow(() -> new BusinessException(ErrorCode.NOT_FOUND, " mục đích vay vốn với id: " + id));
//        ctgCfgLoanPurposeMapper.updateCtgCfgLoanPurposeFromDto(ctgCfgLoanPurposeDto, entity);
//        ctgCfgLoanPurposeRepository.save(entity);
        Optional<CtgCfgLoanPurpose> ctgCfgLoanPurpose=ctgCfgLoanPurposeRepository.findCtgCfgLoanPurposeByPurposeCode(ctgCfgLoanPurposeDto.getPurposeCode());
        if(!ctgCfgLoanPurpose.isPresent()){
            throw new BusinessException(ErrorCode.NOT_FOUND);
        }

        ctgCfgLoanPurposeRepository.updateCtgCfgLoanPurpose(
                ctgCfgLoanPurposeDto.getPurposeCode(),
                ctgCfgLoanPurposeDto.getPurposeName(),
                ctgCfgLoanPurposeDto.getOrgCode(),
                ctgCfgLoanPurposeDto.getLoanLimitMin(),
                ctgCfgLoanPurposeDto.getLoanLimitMax(),
                ctgCfgLoanPurposeDto.getRiskLevel(),
                ctgCfgLoanPurposeDto.getIsActive(),
                ctgCfgLoanPurposeDto.getDescription()
        );
    }

    @Override
    @Transactional
    public void delete(Long id) {
        CtgCfgLoanPurpose entity =  ctgCfgLoanPurposeRepository.findById(id)
                .orElseThrow(() -> new BusinessException(ErrorCode.NOT_FOUND, " mục đích vay vốn với id: " + id));
        entity.setIsDelete(EntityStatus.IsDelete.DELETED.getValue());
        ctgCfgLoanPurposeRepository.delete(entity);
    }


    @Override
    public Page<CtgCfgLoanPurposeResponse> searchAll(String filter, Pageable pageable, boolean isExport) {
        Page<CtgCfgLoanPurposeResponse> res = ctgCfgLoanPurposeRepository.searchCtgCfgLoanPurpose(filter, pageable);

        DecimalFormatSymbols symbols = new DecimalFormatSymbols(new Locale("vi", "VN"));
        symbols.setGroupingSeparator('.');
        symbols.setDecimalSeparator(',');
        DecimalFormat formatter = new DecimalFormat("#,##0.##", symbols);

        res.getContent().forEach(item -> {
            item.setStatus(EntityStatus.IsActive.ACTIVE.getValue() == item.getIsActive() ? "Hiệu lực" : "Hết hiệu lực");

            if (isExport) {
                item.setIsActive(null);
            }

            Object loanLimitMin = item.getLoanLimitMin();
            Object loanLimitMax = item.getLoanLimitMax();

            if (loanLimitMin instanceof Number) {
                item.setLoanLimitMinFormat(formatter.format(loanLimitMin));
            } else {
                item.setLoanLimitMinFormat(null);
            }

            if (loanLimitMax instanceof Number) {
                item.setLoanLimitMaxFormat(formatter.format(loanLimitMax));
            } else {
                item.setLoanLimitMaxFormat(null);
            }
        });

        return res;
    }



    @Override
    public ResponseEntity<ByteArrayResource> exportToExcel(String filter, List<String> labels) {
        String fileName = "ListCtgCfgLoanPurpose.xlsx";
        List<CtgCfgLoanPurposeResponse> responses = ctgCfgLoanPurposeRepository.exportToExcel(filter);
        DecimalFormatSymbols symbols = new DecimalFormatSymbols(new Locale("vi", "VN"));
        symbols.setGroupingSeparator('.');
        symbols.setDecimalSeparator(',');
        DecimalFormat formatter = new DecimalFormat("#,##0.##", symbols);

        responses.forEach(item -> {
            item.setStatus(EntityStatus.IsActive.ACTIVE.getValue() == item.getIsActive() ? "Hiệu lực" : "Hết hiệu lực");



            Object loanLimitMin = item.getLoanLimitMin();
            Object loanLimitMax = item.getLoanLimitMax();

            if (loanLimitMin instanceof Number) {
                item.setLoanLimitMinFormat(formatter.format(loanLimitMin));
            } else {
                item.setLoanLimitMinFormat(null);
            }

            if (loanLimitMax instanceof Number) {
                item.setLoanLimitMaxFormat(formatter.format(loanLimitMax));
            } else {
                item.setLoanLimitMaxFormat(null);
            }
            item.setLoanLimitMin(null);
            item.setLoanLimitMax(null);
            item.setIsActive(null);
        });

        return excelService.exportToExcel(responses, labels, CtgCfgLoanPurposeResponse.class, fileName);
    }

    @Override
    public boolean checkExist(String code) {
        List<CtgCfgLoanPurpose> entity = ctgCfgLoanPurposeRepository.findCtgCfgLoanPurposesByPurposeCodeAndIsDelete(code, EntityStatus.IsDelete.NOT_DELETED.getValue());
        if (!entity.isEmpty()) {
            return true;
        }
        return false;
    }
}
