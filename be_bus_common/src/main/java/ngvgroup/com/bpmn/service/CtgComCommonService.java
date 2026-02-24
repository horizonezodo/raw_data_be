package ngvgroup.com.bpmn.service;

import ngvgroup.com.bpmn.dto.ComCommon.CommonDto;
import ngvgroup.com.bpmn.dto.CtgComCommon.CtgComCommonDTO;

import java.util.List;

public interface CtgComCommonService {
    List<CommonDto> listCommon(String commonTypeCode);

    List<CommonDto> getAllCommon();

    List<CtgComCommonDTO> findAllByCommonTypeCode(String commonTypeCode);
}
