package ngvgroup.com.loan.feature.type_of_capital_use.service;

import ngvgroup.com.loan.feature.type_of_capital_use.dto.LnmCfgCapitalUseRateDTO;

import java.util.List;

public interface LnmCfgCapitalUseRateService {
    void saveOrUpdateAll(List<LnmCfgCapitalUseRateDTO> dtos,String type);
    void deleteAll(String capitalUseCode,String orgCode);
    List<LnmCfgCapitalUseRateDTO> getAllByCapitalUseCodeAndOrgCode(String capitalUseCode,String orgCode);

}
