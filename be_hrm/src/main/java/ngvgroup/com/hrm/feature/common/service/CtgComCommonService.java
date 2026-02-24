package ngvgroup.com.hrm.feature.common.service;


import ngvgroup.com.hrm.feature.common.dto.CtgComCommonDTO;

import java.util.List;
import java.util.Map;

public interface CtgComCommonService {

    List<CtgComCommonDTO> findAllByCommonTypeCode(String commonTypeCode);
    Map<String, String> findByCommonTypeCodes(List<String> commonTypeCodes);
}
