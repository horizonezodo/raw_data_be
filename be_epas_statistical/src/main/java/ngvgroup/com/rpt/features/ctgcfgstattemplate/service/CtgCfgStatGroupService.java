package ngvgroup.com.rpt.features.ctgcfgstattemplate.service;

import ngvgroup.com.rpt.features.common.dto.ComCfgCommonDto;
import ngvgroup.com.rpt.features.common.dto.CommonResponse;

import java.util.List;

public interface CtgCfgStatGroupService {
    List<ComCfgCommonDto> getGroupsByType(String commonTypeCode);

    List<CommonResponse> getGroupsWithData(String commonTypeCode);
}
