package ngvgroup.com.fac.feature.common.service;

import ngvgroup.com.fac.feature.common.dto.CtgCfgCommonDTO;

import java.util.List;

public interface CtgCfgCommonService {
    List<CtgCfgCommonDTO> getByTypeCodeAndParentCode(String commonTypeCode, String parentCode);

    CtgCfgCommonDTO getCommonNameBuCommonValue(String commonValue);

    List<CtgCfgCommonDTO> getList(List<String> commonCode);
}
