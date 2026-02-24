package ngvgroup.com.loan.feature.loan_purpose.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import ngvgroup.com.loan.feature.loan_purpose.dto.CtgCfgLoanPurposeDto;
import ngvgroup.com.loan.feature.loan_purpose.dto.CtgCfgLoanPurposeResponse;
import org.springframework.http.ResponseEntity;

import java.util.List;

public interface CtgCfgLoanPurposeService {
    CtgCfgLoanPurposeDto findOne(Long id);
    void create(CtgCfgLoanPurposeDto ctgCfgLoanPurposeDto);
    void update(Long id, CtgCfgLoanPurposeDto dto);
    void delete(Long id);
    Page<CtgCfgLoanPurposeResponse> searchAll(String filter,Pageable pageable, boolean isExport);
    ResponseEntity<byte[]> exportToExcel(String filter, List<String> labels);
    boolean checkExist(String code);
}