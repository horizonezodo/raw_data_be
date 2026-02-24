package ngvgroup.com.loan.feature.interest_rate.service.impl;

import com.ngvgroup.bpm.core.common.exception.BusinessException;
import com.ngvgroup.bpm.core.common.exception.ErrorCode;
import lombok.AllArgsConstructor;
import ngvgroup.com.loan.core.constant.LoanVariableConstants;
import ngvgroup.com.loan.feature.interest_rate.dto.CtgCfgInterestRateDtlDetailDto;
import ngvgroup.com.loan.feature.interest_rate.dto.CtgCfgInterestRateDtlDto;
import ngvgroup.com.loan.feature.interest_rate.model.CtgCfgInterestRateDtl;
import ngvgroup.com.loan.feature.interest_rate.repository.CtgCfgInterestRateDtlRepository;
import ngvgroup.com.loan.feature.interest_rate.service.CtgCfgInterestRateDtlService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@AllArgsConstructor
public class CtgCfgInterestRateDtlServiceImpl implements CtgCfgInterestRateDtlService {

    private final CtgCfgInterestRateDtlRepository ctgCfgInterestRateDtlRepository;

    @Override
    public void createInterestRateDtl(List<CtgCfgInterestRateDtlDto> interestRateDtlDtos){

        List<CtgCfgInterestRateDtl> ctgCfgInterestRateDtls = interestRateDtlDtos.stream()
                .map(dto -> new CtgCfgInterestRateDtl(
                        dto.getOrgCode(),
                        dto.getInterestCode(),
                        dto.getEffectiveDate(),
                        dto.getNegotiatedRate(),
                        dto.getPreviousRate(),
                        dto.getInterestRate(),
                        dto.getAmountFrom(),
                        dto.getAmountTo(),
                        dto.getDocNo(),
                        dto.getDocEffectiveDate(),
                        LoanVariableConstants.APPROVAL,
                        dto.getIsActive()
                ))
                .toList();

        ctgCfgInterestRateDtlRepository.saveAll(ctgCfgInterestRateDtls);
    }

    @Transactional
    @Override
    public void updateInterestRateDtl(List<CtgCfgInterestRateDtlDto> dtos) {

        for (CtgCfgInterestRateDtlDto dto : dtos) {

            CtgCfgInterestRateDtl entity;

            if (dto.getId() != null) {
                entity = ctgCfgInterestRateDtlRepository.findById(dto.getId())
                        .orElseThrow(() -> new IllegalArgumentException(
                                "Interest Rate Detail ID not found: " + dto.getId()
                        ));
            }
            else {
                entity = new CtgCfgInterestRateDtl();
                entity.setInterestCode(dto.getInterestCode());
            }

            entity.setOrgCode(dto.getOrgCode());
            entity.setInterestRate(dto.getInterestRate());
            entity.setNegotiatedRate(dto.getNegotiatedRate());
            entity.setPreviousRate(dto.getPreviousRate());
            entity.setEffectiveDate(dto.getEffectiveDate());
            entity.setAmountFrom(dto.getAmountFrom());
            entity.setAmountTo(dto.getAmountTo());
            entity.setDocEffectiveDate(dto.getDocEffectiveDate());
            entity.setDocNo(dto.getDocNo());

            ctgCfgInterestRateDtlRepository.save(entity);
        }
    }

    @Override
    public void deleteInterestRateDtl(String interestRateCode){
        List<CtgCfgInterestRateDtl> interestRateDtls = ctgCfgInterestRateDtlRepository.findCtgCfgInterestRateDtlsByInterestCode(interestRateCode);
        if(interestRateDtls.isEmpty()){
            throw new BusinessException(ErrorCode.NOT_FOUND,interestRateCode);
        }

        ctgCfgInterestRateDtlRepository.deleteAll(interestRateDtls);

    }

    @Override
    public Page<CtgCfgInterestRateDtlDetailDto> getDetailInterestRateDtls(String interestCode, String keyword, Pageable pageable){
        return ctgCfgInterestRateDtlRepository.getDetailInterestRateDtls(interestCode,keyword,pageable);
    }


    @Override
    public void deleteById(Long id){
        CtgCfgInterestRateDtl ctgCfgInterestRateDtl = ctgCfgInterestRateDtlRepository.findById(id).orElse(null);
        if(ctgCfgInterestRateDtl == null){
            throw new BusinessException(ErrorCode.NOT_FOUND);
        }
        ctgCfgInterestRateDtlRepository.deleteById(id);
    }


    @Override
    public  List<CtgCfgInterestRateDtlDetailDto> getAll(String interestCode,String keyword){
        return ctgCfgInterestRateDtlRepository.getAll(interestCode,keyword);
    }


}
