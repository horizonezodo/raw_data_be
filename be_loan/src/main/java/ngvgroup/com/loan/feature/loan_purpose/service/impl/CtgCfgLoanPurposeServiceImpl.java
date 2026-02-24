package ngvgroup.com.loan.feature.loan_purpose.service.impl;

import com.ngvgroup.bpm.core.common.excel.ExportExcel;
import com.ngvgroup.bpm.core.common.exception.BusinessException;
import com.ngvgroup.bpm.core.common.exception.ErrorCode;
import lombok.RequiredArgsConstructor;
import ngvgroup.com.loan.core.constant.EntityStatus;
import ngvgroup.com.loan.core.constant.LoanErrorCode;
import ngvgroup.com.loan.core.constant.LoanVariableConstants;
import ngvgroup.com.loan.feature.loan_purpose.dto.CtgCfgLoanPurposeDto;
import ngvgroup.com.loan.feature.loan_purpose.dto.CtgCfgLoanPurposeResponse;
import ngvgroup.com.loan.feature.loan_purpose.mapper.CtgCfgLoanPurposeMapper;
import ngvgroup.com.loan.feature.loan_purpose.model.CtgCfgLoanPurpose;
import ngvgroup.com.loan.feature.loan_purpose.repository.CtgCfgLoanPurposeRepository;
import ngvgroup.com.loan.feature.loan_purpose.service.CtgCfgLoanPurposeService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.sql.Timestamp;
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
    private final ExportExcel exportExcel;

    @Override
    public CtgCfgLoanPurposeDto findOne(Long id) {
        CtgCfgLoanPurpose entity=  ctgCfgLoanPurposeRepository.findById(id)
                .orElseThrow(() -> new BusinessException(ErrorCode.NOT_FOUND, " mục đích vay vốn với id: " + id));
        return ctgCfgLoanPurposeMapper.toDto(entity);
    }

    @Override
    @Transactional
    public void create(CtgCfgLoanPurposeDto ctgCfgLoanPurposeDto) {
        Optional<CtgCfgLoanPurpose> ctgCfgLoanPurpose = ctgCfgLoanPurposeRepository
                .findCtgCfgLoanPurposeByPurposeCode(ctgCfgLoanPurposeDto.getPurposeCode());
        if(ctgCfgLoanPurpose.isPresent()){
            throw new BusinessException(LoanErrorCode.LOAN_PURPOSE_ALREADY_EXISTS);
        }

        ctgCfgLoanPurposeRepository.save(new CtgCfgLoanPurpose(
                ctgCfgLoanPurposeDto.getOrgCode(),
                ctgCfgLoanPurposeDto.getPurposeCode(),
                ctgCfgLoanPurposeDto.getPurposeName(),
                ctgCfgLoanPurposeDto.getRiskLevel(),
                ctgCfgLoanPurposeDto.getLoanLimitMin(),
                ctgCfgLoanPurposeDto.getLoanLimitMax(),
                LoanVariableConstants.APPROVAL,
                ctgCfgLoanPurposeDto.getIsActive()
        ));

    }

    @Override
    @Transactional
    public void update(Long id, CtgCfgLoanPurposeDto dto) {
        CtgCfgLoanPurpose entity = ctgCfgLoanPurposeRepository
                .findCtgCfgLoanPurposeByPurposeCode(dto.getPurposeCode())
                .orElseThrow(() -> new BusinessException(ErrorCode.NOT_FOUND));

        entity.setPurposeName(dto.getPurposeName());
        entity.setOrgCode(dto.getOrgCode());
        entity.setLoanLimitMin(dto.getLoanLimitMin());
        entity.setLoanLimitMax(dto.getLoanLimitMax());
        entity.setRiskLevel(dto.getRiskLevel());
        entity.setIsActive(dto.getIsActive());
        entity.setDescription(dto.getDescription());
        entity.setModifiedDate(new Timestamp(System.currentTimeMillis()));
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
            item.setStatus(Boolean.TRUE.equals(item.getIsActive()) ? "Hiệu lực" : "Hết hiệu lực");

            if (isExport) {
                item.setIsActive(null);
            }

            checkCtgCfgLoanPurpose(formatter, item);
        });

        return res;
    }

    private void checkCtgCfgLoanPurpose(DecimalFormat formatter, CtgCfgLoanPurposeResponse item) {

        Number loanLimitMin = item.getLoanLimitMin();
        Number loanLimitMax = item.getLoanLimitMax();

        item.setLoanLimitMinFormat(
                loanLimitMin != null ? formatter.format(loanLimitMin) : null
        );

        item.setLoanLimitMaxFormat(
                loanLimitMax != null ? formatter.format(loanLimitMax) : null
        );
    }

    @Override
    public ResponseEntity<byte[]> exportToExcel(String filter, List<String> labels) {
        try {
            String fileName = "ListCtgCfgLoanPurpose.xlsx";
            List<CtgCfgLoanPurposeResponse> responses = ctgCfgLoanPurposeRepository.exportToExcel(filter);
            DecimalFormatSymbols symbols = new DecimalFormatSymbols(new Locale("vi", "VN"));
            symbols.setGroupingSeparator('.');
            symbols.setDecimalSeparator(',');
            DecimalFormat formatter = new DecimalFormat("#,##0.##", symbols);

            responses.forEach(item -> {
                item.setStatus(Boolean.TRUE.equals(item.getIsActive()) ? "Hiệu lực" : "Hết hiệu lực");


                checkCtgCfgLoanPurpose(formatter, item);
                item.setLoanLimitMin(null);
                item.setLoanLimitMax(null);
                item.setIsActive(null);
            });

            return exportExcel.exportExcel(responses, fileName);
        }
        catch (Exception e) {
            throw new BusinessException(LoanErrorCode.WRITE_EXCEL_ERROR, e);
        }
    }

    @Override
    public boolean checkExist(String code) {
        List<CtgCfgLoanPurpose> entity = ctgCfgLoanPurposeRepository.findCtgCfgLoanPurposesByPurposeCodeAndIsDelete(code, EntityStatus.IsDelete.NOT_DELETED.getValue());
        return !entity.isEmpty();
    }
}

