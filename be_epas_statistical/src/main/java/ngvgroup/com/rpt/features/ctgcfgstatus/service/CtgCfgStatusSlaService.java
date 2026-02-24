package ngvgroup.com.rpt.features.ctgcfgstatus.service;

import com.ngvgroup.bpm.core.persistence.service.BaseService;
import ngvgroup.com.rpt.features.ctgcfgstatus.dto.CtgCfgStatusSlaDto;
import ngvgroup.com.rpt.features.ctgcfgstatus.model.CtgCfgStatusSla;

public interface CtgCfgStatusSlaService extends BaseService<CtgCfgStatusSla, CtgCfgStatusSlaDto> {
    Long getIdByStatusCode(String statusCode);
}
