package ngvgroup.com.loan.feature.common.service;

import ngvgroup.com.loan.feature.common.dto.CommonDto;
import ngvgroup.com.loan.feature.common.dto.CtgComCommonDTO;
import ngvgroup.com.loan.feature.common.dto.TemplateResDto;

import java.util.List;

public interface CtgComCommonService {
    List<CommonDto> listCommon(String commonTypeCode);

    List<CommonDto> getAllCommon();

    List<CtgComCommonDTO> findAllByCommonTypeCode(String commonTypeCode);

    TemplateResDto getTemplateByCode(String templateCode);
}
