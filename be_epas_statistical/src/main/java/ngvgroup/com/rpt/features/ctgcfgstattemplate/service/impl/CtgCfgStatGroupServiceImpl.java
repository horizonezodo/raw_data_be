package ngvgroup.com.rpt.features.ctgcfgstattemplate.service.impl;

import lombok.AllArgsConstructor;
import ngvgroup.com.rpt.features.common.dto.ComCfgCommonDto;
import ngvgroup.com.rpt.features.common.dto.CommonResponse;
import ngvgroup.com.rpt.features.common.repository.ComCfgCommonRepository;
import ngvgroup.com.rpt.features.ctgcfgstattemplate.service.CtgCfgStatGroupService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
public class CtgCfgStatGroupServiceImpl implements CtgCfgStatGroupService {
    private final ComCfgCommonRepository commonRepository;

    @Override
    public List<ComCfgCommonDto> getGroupsByType(String commonTypeCode) {
        return this.commonRepository.findByCommonTypeCodeAndIsActiveTrue(commonTypeCode);
    }

    @Override
    public List<CommonResponse> getGroupsWithData(String commonTypeCode) {
        // Get all groups that have actual data in CTG_CFG_STAT_TYPE
        return this.commonRepository.findGroupsWithData(commonTypeCode);
    }

}
