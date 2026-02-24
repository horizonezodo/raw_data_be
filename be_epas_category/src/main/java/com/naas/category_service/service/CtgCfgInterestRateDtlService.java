package com.naas.category_service.service;

import com.naas.category_service.dto.CtgCfgInterestRateDtl.CtgCfgInterestRateDtlDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface CtgCfgInterestRateDtlService {

    void createInterestRateDtl(List<CtgCfgInterestRateDtlDto> interestRateDtlDtos);

    void updateInterestRateDtl(List<CtgCfgInterestRateDtlDto> interestRateDtlDto);

    void deleteInterestRateDtl(String interestRateDtlId);

    Page<CtgCfgInterestRateDtlDto> getDetailInterestRateDtls( String interestCode,
                                                              String keyword,
                                                             Pageable pageable);
    void deleteById(Long id);

    List<CtgCfgInterestRateDtlDto> getAll(String interestCode,String keyword);
}
