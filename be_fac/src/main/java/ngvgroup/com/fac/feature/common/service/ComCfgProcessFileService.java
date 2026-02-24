package ngvgroup.com.fac.feature.common.service;
import ngvgroup.com.fac.feature.common.dto.ComCfgProcessFileDto;

import java.util.List;

public interface ComCfgProcessFileService {
    List<ComCfgProcessFileDto> getByProcessTypeCode(String processTypeCode);
}
