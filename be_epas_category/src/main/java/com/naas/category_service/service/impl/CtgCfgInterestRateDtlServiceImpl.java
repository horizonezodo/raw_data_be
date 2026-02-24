package com.naas.category_service.service.impl;

import com.naas.category_service.dto.CtgCfgInterestRateDtl.CtgCfgInterestRateDtlDto;
import com.naas.category_service.exception.CategoryErrorCode;
import com.naas.category_service.model.CtgCfgInterestRateDtl;
import com.naas.category_service.repository.CtgCfgInterestRateDtlRepository;
import com.naas.category_service.service.CtgCfgInterestRateDtlService;
import com.ngvgroup.bpm.core.exception.BusinessException;
import com.ngvgroup.bpm.core.exception.ErrorCode;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class CtgCfgInterestRateDtlServiceImpl implements CtgCfgInterestRateDtlService {

    private CtgCfgInterestRateDtlRepository ctgCfgInterestRateDtlRepository;

    @Override
    public void createInterestRateDtl(List<CtgCfgInterestRateDtlDto> interestRateDtlDtos){


//        List<CtgCfgInterestRateDtl> interestRateDtls = ctgCfgInterestRateDtlRepository.findCtgCfgInterestRateDtlsByInterestCode(interestRateDtlDtos.get(0).getInterestCode());
//        if(!interestRateDtls.isEmpty()){
//             throw new BusinessException(CategoryErrorCode.INTEREST_RATE_DTL_ALREADY_EXISTS);
//        }
        List<CtgCfgInterestRateDtl> ctgCfgInterestRateDtls = interestRateDtlDtos.stream()
                .map(dto -> new CtgCfgInterestRateDtl(
                        dto.getOrgCode(),
                        dto.getInterestCode(),
                        dto.getIsActive(),
                        dto.getInterestRate(),
                        dto.getNegotiatedRate(),
                        dto.getPreviousRate(),
                        dto.getEffectiveDate(),
                        dto.getAmountFrom(),
                        dto.getAmountTo(),
                        dto.getDocNo(),
                        dto.getDocEffectiveDate()
                ))
                .collect(Collectors.toList());

        ctgCfgInterestRateDtlRepository.saveAll(ctgCfgInterestRateDtls);
    }

    @Transactional
    @Override
    public void updateInterestRateDtl(List<CtgCfgInterestRateDtlDto> interestRateDtlDtos){


        for(CtgCfgInterestRateDtlDto ctgCfgInterestRateDtlDto:interestRateDtlDtos){
            if (ctgCfgInterestRateDtlDto.getId()!=null) {
                CtgCfgInterestRateDtl ctgCfgInterestRateDtl = ctgCfgInterestRateDtlRepository.findById(ctgCfgInterestRateDtlDto.getId()).orElse(null);
                ctgCfgInterestRateDtl.setIsActive(ctgCfgInterestRateDtlDto.getIsActive());
                ctgCfgInterestRateDtl.setOrgCode(ctgCfgInterestRateDtlDto.getOrgCode());
                ctgCfgInterestRateDtl.setInterestRate(ctgCfgInterestRateDtlDto.getInterestRate());
                ctgCfgInterestRateDtl.setNegotiatedRate(ctgCfgInterestRateDtlDto.getNegotiatedRate());
                ctgCfgInterestRateDtl.setPreviousRate(ctgCfgInterestRateDtlDto.getPreviousRate());
                ctgCfgInterestRateDtl.setEffectiveDate(ctgCfgInterestRateDtlDto.getEffectiveDate());
                ctgCfgInterestRateDtl.setAmountFrom(ctgCfgInterestRateDtlDto.getAmountFrom());
                ctgCfgInterestRateDtl.setAmountTo(ctgCfgInterestRateDtlDto.getAmountTo());
                ctgCfgInterestRateDtl.setDocEffectiveDate(ctgCfgInterestRateDtlDto.getDocEffectiveDate());
                ctgCfgInterestRateDtl.setDocNo(ctgCfgInterestRateDtlDto.getDocNo());
                ctgCfgInterestRateDtlRepository.save(ctgCfgInterestRateDtl);
            }
            else {
                CtgCfgInterestRateDtl ctgCfgInterestRateDtlNew = new CtgCfgInterestRateDtl(
                        ctgCfgInterestRateDtlDto.getOrgCode(),
                        ctgCfgInterestRateDtlDto.getInterestCode(),
                        ctgCfgInterestRateDtlDto.getIsActive(),
                        ctgCfgInterestRateDtlDto.getInterestRate(),
                        ctgCfgInterestRateDtlDto.getNegotiatedRate(),
                        ctgCfgInterestRateDtlDto.getPreviousRate(),
                        ctgCfgInterestRateDtlDto.getEffectiveDate(),
                        ctgCfgInterestRateDtlDto.getAmountFrom(),
                        ctgCfgInterestRateDtlDto.getAmountTo(),
                        ctgCfgInterestRateDtlDto.getDocNo(),
                        ctgCfgInterestRateDtlDto.getDocEffectiveDate()
                );
                ctgCfgInterestRateDtlRepository.save(ctgCfgInterestRateDtlNew);
            }

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
    public Page<CtgCfgInterestRateDtlDto> getDetailInterestRateDtls(String interestCode,String keyword,Pageable pageable){
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
    public  List<CtgCfgInterestRateDtlDto> getAll(String interestCode,String keyword){
        return ctgCfgInterestRateDtlRepository.getAll(interestCode,keyword);
    }


}
