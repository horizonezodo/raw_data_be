package ngvgroup.com.fac.feature.fac_inf_acc.service;

import com.ngvgroup.bpm.core.persistence.service.BaseService;
import ngvgroup.com.fac.feature.fac_inf_acc.dto.AccMapValueDto;
import ngvgroup.com.fac.feature.fac_inf_acc.model.FacCfgAccMapValue;

public interface FacCfgAccMapValueService extends BaseService<FacCfgAccMapValue, AccMapValueDto> {
    AccMapValueDto getDomainConfig();
}
