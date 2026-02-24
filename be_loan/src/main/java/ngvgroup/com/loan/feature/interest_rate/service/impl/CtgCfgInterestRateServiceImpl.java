package ngvgroup.com.loan.feature.interest_rate.service.impl;

import com.ngvgroup.bpm.core.common.excel.ExportExcel;
import ngvgroup.com.loan.core.constant.LoanErrorCode;
import ngvgroup.com.loan.feature.interest_rate.dto.CtgCfgInterestRateDto;
import ngvgroup.com.loan.feature.interest_rate.model.CtgCfgInterestRate;
import ngvgroup.com.loan.feature.interest_rate.repository.CtgCfgInterestRateRepository;
import com.ngvgroup.bpm.core.common.exception.BusinessException;
import com.ngvgroup.bpm.core.common.exception.ErrorCode;
import lombok.AllArgsConstructor;
import ngvgroup.com.loan.feature.interest_rate.service.CtgCfgInterestRateService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
@AllArgsConstructor
public class CtgCfgInterestRateServiceImpl implements CtgCfgInterestRateService {
    private final CtgCfgInterestRateRepository ctgCfgInterestRateRepository;
    private final ExportExcel exportExcel;

    @Override
    public Page<CtgCfgInterestRateDto> searchAll(String keyword,String orgCode,List<String> moduleCodes, Pageable pageable){
        return ctgCfgInterestRateRepository.searchAll(keyword,orgCode,moduleCodes, pageable);
    }

    @Override
    public ResponseEntity<byte[]> exportToExcel(String keyword, String fileName, List<String> labels) {
        try {
            List<CtgCfgInterestRateDto> ctgCfgInterestRateDtos = ctgCfgInterestRateRepository.exportToExcel(keyword);

            ctgCfgInterestRateDtos.forEach(item -> {
                boolean isActive = Boolean.TRUE.equals(item.getIsActive());
                item.setIsActiveFormat(isActive ? "Hiệu lực" : "Hết hiệu lực");
                item.setIsActive(null); // common part tách riêng
            });

            return exportExcel.exportExcel(ctgCfgInterestRateDtos, fileName);
        }
        catch (Exception ex) {
            throw new BusinessException(LoanErrorCode.WRITE_EXCEL_ERROR, ex);
        }
    }

    @Override
    public void createInterestRate(CtgCfgInterestRateDto ctgCfgInterestRateDto){

        Optional<CtgCfgInterestRate> interestRate=ctgCfgInterestRateRepository.findCtgCfgInterestRateByInterestCode(ctgCfgInterestRateDto.getInterestCode());
        if(interestRate.isPresent()){
            throw new BusinessException(LoanErrorCode.INTEREST_RATE_ALREADY_EXISTS,ctgCfgInterestRateDto.getInterestCode());

        }

        CtgCfgInterestRate ctgCfgInterestRate=new CtgCfgInterestRate(
                ctgCfgInterestRateDto.getInterestCode(),
                ctgCfgInterestRateDto.getInterestName(),
                ctgCfgInterestRateDto.getOrgCode(),
                ctgCfgInterestRateDto.getModuleCode(),
                ctgCfgInterestRateDto.getInterestType(),
                ctgCfgInterestRateDto.getCurrencyCode(),
                ctgCfgInterestRateDto.getIsByBalance()
        );

        ctgCfgInterestRateRepository.save(ctgCfgInterestRate);
    }


    @Transactional
    @Override
    public void updateInterestRate(CtgCfgInterestRateDto ctgCfgInterestRateDto){
        Optional<CtgCfgInterestRate> interestRate=ctgCfgInterestRateRepository.findCtgCfgInterestRateByInterestCode(ctgCfgInterestRateDto.getInterestCode());
        if(interestRate.isEmpty()){
            throw new BusinessException(ErrorCode.NOT_FOUND,ctgCfgInterestRateDto.getInterestCode());

        }
        interestRate.get().setInterestName(ctgCfgInterestRateDto.getInterestName());
        interestRate.get().setOrgCode(ctgCfgInterestRateDto.getOrgCode());
        interestRate.get().setModuleCode(ctgCfgInterestRateDto.getModuleCode());
        interestRate.get().setCurrencyCode(ctgCfgInterestRateDto.getCurrencyCode());
        interestRate.get().setIsByBalance(ctgCfgInterestRateDto.getIsByBalance());
        interestRate.get().setInterestType(ctgCfgInterestRateDto.getInterestType());
        interestRate.get().setModifiedDate(Timestamp.valueOf(LocalDateTime.now()));
        ctgCfgInterestRateRepository.save(interestRate.get());
    }


    @Override
    public void deleteInterestRate(String interestCode){
        Optional<CtgCfgInterestRate> interestRate=ctgCfgInterestRateRepository.findCtgCfgInterestRateByInterestCode(interestCode);
        if(interestRate.isEmpty()){
            throw new BusinessException(ErrorCode.NOT_FOUND,interestCode);
        }
        ctgCfgInterestRateRepository.delete(interestRate.get());
    }

    @Override
    public CtgCfgInterestRateDto getDetailInterestRate(String interestCode){
        return ctgCfgInterestRateRepository.getDetail(interestCode);
    }

    @Override
    public boolean checkExist(String code) {
        return ctgCfgInterestRateRepository
                .findCtgCfgInterestRateByInterestCode(code)
                .isPresent();
    }

}
