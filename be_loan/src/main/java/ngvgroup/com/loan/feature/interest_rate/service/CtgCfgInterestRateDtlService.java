package ngvgroup.com.loan.feature.interest_rate.service;

import ngvgroup.com.loan.feature.interest_rate.dto.CtgCfgInterestRateDtlDetailDto;
import ngvgroup.com.loan.feature.interest_rate.dto.CtgCfgInterestRateDtlDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface CtgCfgInterestRateDtlService {

    void createInterestRateDtl(List<CtgCfgInterestRateDtlDto> interestRateDtlDtos);

    void updateInterestRateDtl(List<CtgCfgInterestRateDtlDto> interestRateDtlDto);

    void deleteInterestRateDtl(String interestRateDtlId);

    Page<CtgCfgInterestRateDtlDetailDto> getDetailInterestRateDtls(String interestCode,
                                                                   String keyword,
                                                                   Pageable pageable);
    void deleteById(Long id);

    List<CtgCfgInterestRateDtlDetailDto> getAll(String interestCode,String keyword);
}
