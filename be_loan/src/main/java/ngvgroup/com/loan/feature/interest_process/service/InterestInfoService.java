package ngvgroup.com.loan.feature.interest_process.service;

import ngvgroup.com.loan.feature.interest_process.dto.InterestDetailDTO;
import ngvgroup.com.loan.feature.interest_process.dto.CfgInterestRateDTO;
import ngvgroup.com.loan.feature.interest_process.dto.InterestProfileDTO;
import ngvgroup.com.loan.feature.interest_process.dto.LnmCfgIntRateDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;

import java.util.List;

public interface InterestInfoService {
    Page<LnmCfgIntRateDTO> search(String keyword, List<String> commonCodes, Pageable pageable);

    InterestProfileDTO getInterestDetail(String processInstanceCode);

    List<String> getAllInterestRateCode();

    InterestDetailDTO getInterestDetail(Long id);

    void deleteInterest(Long id);


    List<CfgInterestRateDTO> getListRate();

    ResponseEntity<byte[]> exportExcel();
}
