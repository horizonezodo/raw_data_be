package ngvgroup.com.bpm.features.common.service;

import ngvgroup.com.bpm.features.common.dto.CtgComCommonDTO;

import java.util.List;

public interface CtgComCommonService {
    List<CtgComCommonDTO> findAllByCommonTypeCode(String commonTypeCode);
}
