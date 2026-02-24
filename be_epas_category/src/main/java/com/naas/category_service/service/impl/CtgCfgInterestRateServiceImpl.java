package com.naas.category_service.service.impl;

import com.naas.category_service.dto.CtgCfgInterestRate.CtgCfgInterestRateDto;
import com.naas.category_service.exception.CategoryErrorCode;
import com.naas.category_service.model.CtgCfgInterestRate;
import com.naas.category_service.model.CtgCfgScoringIndcGroup;
import com.naas.category_service.repository.CtgCfgInterestRateRepository;
import com.naas.category_service.service.CtgCfgInterestRateService;
import com.ngvgroup.bpm.core.exception.BusinessException;
import com.ngvgroup.bpm.core.exception.ErrorCode;
import lombok.AllArgsConstructor;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
@AllArgsConstructor
public class CtgCfgInterestRateServiceImpl implements CtgCfgInterestRateService {
    private final CtgCfgInterestRateRepository ctgCfgInterestRateRepository;
    private final ExcelService excelService;

    @Override
    public Page<CtgCfgInterestRateDto> searchAll(String keyword,String orgCode,List<String> moduleCodes, Pageable pageable){
        return ctgCfgInterestRateRepository.searchAll(keyword,orgCode,moduleCodes, pageable);
    }

    @Override
    public ResponseEntity<ByteArrayResource> exportToExcel(String keyword, String fileName, List<String> labels){
        List<CtgCfgInterestRateDto> ctgCfgInterestRateDtos=ctgCfgInterestRateRepository.exportToExcel(keyword);
        ctgCfgInterestRateDtos.forEach(ctgCfgInterestRateDto->{
            if (ctgCfgInterestRateDto.getIsActive()){
                ctgCfgInterestRateDto.setIsActiveFormat("Hiệu lực");
                ctgCfgInterestRateDto.setIsActive(null);
            }
            else {
                ctgCfgInterestRateDto.setIsActiveFormat("Hết hiệu lực");
                ctgCfgInterestRateDto.setIsActive(null);
            }
        });
        return excelService.exportToExcel(ctgCfgInterestRateDtos,labels,CtgCfgInterestRateDto.class,fileName);
    }


    @Override
    public void createInterestRate(CtgCfgInterestRateDto ctgCfgInterestRateDto){

        Optional<CtgCfgInterestRate> interestRate=ctgCfgInterestRateRepository.findCtgCfgInterestRateByInterestCode(ctgCfgInterestRateDto.getInterestCode());
        if(interestRate.isPresent()){
            throw new BusinessException(CategoryErrorCode.INTEREST_RATE_ALREADY_EXISTS,ctgCfgInterestRateDto.getInterestCode());

        }

        CtgCfgInterestRate ctgCfgInterestRate=new CtgCfgInterestRate(
                ctgCfgInterestRateDto.getInterestCode(),
                ctgCfgInterestRateDto.getInterestName(),
                ctgCfgInterestRateDto.getOrgCode(),
                ctgCfgInterestRateDto.getModuleCode(),
                ctgCfgInterestRateDto.getInterestType(),
                ctgCfgInterestRateDto.getIsActive(),
                ctgCfgInterestRateDto.getCurrencyCode(),
                ctgCfgInterestRateDto.getIsByBalance()
        );

        ctgCfgInterestRateRepository.save(ctgCfgInterestRate);
    }


    @Transactional
    @Override
    public void updateInterestRate(CtgCfgInterestRateDto ctgCfgInterestRateDto){
        Optional<CtgCfgInterestRate> interestRate=ctgCfgInterestRateRepository.findCtgCfgInterestRateByInterestCode(ctgCfgInterestRateDto.getInterestCode());
        if(!interestRate.isPresent()){
            throw new BusinessException(ErrorCode.NOT_FOUND,ctgCfgInterestRateDto.getInterestCode());

        }
        interestRate.get().setInterestName(ctgCfgInterestRateDto.getInterestName());
        interestRate.get().setOrgCode(ctgCfgInterestRateDto.getOrgCode());
        interestRate.get().setModuleCode(ctgCfgInterestRateDto.getModuleCode());
        interestRate.get().setIsActive(ctgCfgInterestRateDto.getIsActive());
        interestRate.get().setCurrencyCode(ctgCfgInterestRateDto.getCurrencyCode());
        interestRate.get().setIsByBalance(ctgCfgInterestRateDto.getIsByBalance());
        interestRate.get().setInterestType(ctgCfgInterestRateDto.getInterestType());
        interestRate.get().setModifiedDate(Timestamp.valueOf(LocalDateTime.now()));
        ctgCfgInterestRateRepository.save(interestRate.get());
    }


    @Override
    public void deleteInterestRate(String interestCode){
        Optional<CtgCfgInterestRate> interestRate=ctgCfgInterestRateRepository.findCtgCfgInterestRateByInterestCode(interestCode);
        if(!interestRate.isPresent()){
            throw new BusinessException(ErrorCode.NOT_FOUND,interestCode);
        }
        ctgCfgInterestRateRepository.delete(interestRate.get());
    }

    @Override
    public CtgCfgInterestRateDto getDetailInterestRate(String interestCode){
        return ctgCfgInterestRateRepository.getDetail(interestCode);
    }

    @Override
    public  boolean checkExist(String code){
        Optional<CtgCfgInterestRate> position = ctgCfgInterestRateRepository.findCtgCfgInterestRateByInterestCode(code);
        if(position.isPresent()) return true;
        return false;
    }
}
