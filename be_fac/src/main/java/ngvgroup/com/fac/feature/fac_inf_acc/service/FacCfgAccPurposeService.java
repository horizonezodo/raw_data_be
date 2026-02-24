package ngvgroup.com.fac.feature.fac_inf_acc.service;

import com.ngvgroup.bpm.core.persistence.service.BaseService;
import ngvgroup.com.fac.feature.fac_inf_acc.dto.FacCfgAccPurposeDto;
import ngvgroup.com.fac.feature.fac_inf_acc.model.FacCfgAccPurpose;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface FacCfgAccPurposeService extends BaseService<FacCfgAccPurpose, FacCfgAccPurposeDto> {
    Page<FacCfgAccPurposeDto> getPurposes(Pageable pageable);
}
