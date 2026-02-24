package ngvgroup.com.hrm.feature.cfg_org_unit.service;

import com.ngvgroup.bpm.core.persistence.service.BaseService;
import ngvgroup.com.hrm.feature.cfg_org_unit.dto.HrmCfgOrgUnitDTO;
import ngvgroup.com.hrm.feature.cfg_org_unit.dto.HrmCfgOrgUnitOptionDto;
import ngvgroup.com.hrm.feature.cfg_org_unit.dto.HrmCfgOrgUnitSearch;
import ngvgroup.com.hrm.feature.cfg_org_unit.model.HrmCfgOrgUnit;

import java.util.List;

public interface HrmCfgOrgUnitService extends BaseService<HrmCfgOrgUnit, HrmCfgOrgUnitDTO> {
    List<HrmCfgOrgUnitSearch> findAllUnit();
    List<HrmCfgOrgUnitOptionDto> listOptions();

}
