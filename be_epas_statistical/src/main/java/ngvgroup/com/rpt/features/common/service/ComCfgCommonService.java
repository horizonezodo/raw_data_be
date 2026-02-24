package ngvgroup.com.rpt.features.common.service;

import ngvgroup.com.rpt.features.common.dto.ComCfgCommonDto;
import ngvgroup.com.rpt.features.common.dto.CommonDto;
import ngvgroup.com.rpt.features.common.dto.CommonResponse;

import java.util.List;

public interface ComCfgCommonService {
    List<ComCfgCommonDto> listCommonByCommonTypeCode(String commonTypeCode);
    List<ComCfgCommonDto> getAllBy();
    List<CommonResponse> getAllCommonByCommonTypeCode(String commonTypeCode);
    List<ComCfgCommonDto> getAllCommonByParentCode(String parentCode);
    List<ComCfgCommonDto> getAllByListCommonTypeCode(List<String> commonTypeCodes);

    List<CommonDto> getAllCommon();
    List<ComCfgCommonDto> getAllByParentCodeAndTypeCode(String parentCode, String commonTypeCode);
}
