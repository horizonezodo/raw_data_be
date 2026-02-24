package ngvgroup.com.hrm.feature.cfg_org_unit.service.impl;

import com.ngvgroup.bpm.core.common.exception.BusinessException;
import com.ngvgroup.bpm.core.persistence.service.impl.BaseServiceImpl;
import ngvgroup.com.hrm.core.constant.HrmErrorCode;
import ngvgroup.com.hrm.feature.cfg_org_unit.dto.HrmCfgOrgUnitDTO;
import ngvgroup.com.hrm.feature.cfg_org_unit.dto.HrmCfgOrgUnitOptionDto;
import ngvgroup.com.hrm.feature.cfg_org_unit.dto.HrmCfgOrgUnitSearch;
import ngvgroup.com.hrm.feature.cfg_org_unit.mapper.HrmCfgOrgUnitMapper;
import ngvgroup.com.hrm.feature.cfg_org_unit.model.HrmCfgOrgUnit;
import ngvgroup.com.hrm.feature.cfg_org_unit.repository.HrmCfgOrgUnitRepository;
import ngvgroup.com.hrm.feature.cfg_org_unit.service.HrmCfgOrgUnitService;
import ngvgroup.com.hrm.feature.common.service.CtgComCommonService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

import static ngvgroup.com.hrm.core.constant.CommonTypeCode.ORG_LEVEL_CODE;
import static ngvgroup.com.hrm.core.constant.CommonTypeCode.UNIT_TYPE_CODE;

@Service
public class HrmCfgOrgUnitImpl extends BaseServiceImpl<HrmCfgOrgUnit, HrmCfgOrgUnitDTO>
        implements HrmCfgOrgUnitService {
    private final HrmCfgOrgUnitRepository cfgOrgUnitRepository;
    private final CtgComCommonService ctgComCommonService;

    protected HrmCfgOrgUnitImpl(HrmCfgOrgUnitMapper orgUnitMapper, HrmCfgOrgUnitRepository cfgOrgUnitRepository, CtgComCommonService ctgComCommonService1) {
        super(cfgOrgUnitRepository, orgUnitMapper);
        this.cfgOrgUnitRepository = cfgOrgUnitRepository;
        this.ctgComCommonService = ctgComCommonService1;
    }

    @Override
    protected void validateBeforeCreate(HrmCfgOrgUnitDTO dto) {
        String unitCode = dto.getOrgUnitCode();
        this.cfgOrgUnitRepository.findByOrgUnitCode(unitCode)
                .ifPresent(existing -> {
                    throw new BusinessException(HrmErrorCode.DUPLICATE_UNIT_CODE, unitCode);
                });
    }

    @Override
    protected void validateBeforeUpdate(HrmCfgOrgUnitDTO dto, HrmCfgOrgUnit entity) {
        if (dto.getOrgUnitCode().equals(entity.getOrgUnitCode())) {
            return;
        }
        validateBeforeCreate(dto);
    }

    @Override
    public List<HrmCfgOrgUnitSearch> findAllUnit() {
        Map<String, String> commonDtos = ctgComCommonService.findByCommonTypeCodes(List.of(ORG_LEVEL_CODE, UNIT_TYPE_CODE));

        List<HrmCfgOrgUnitSearch> res =  cfgOrgUnitRepository.findAllByOrderByModifiedDateDesc();

        res.forEach(item -> {
                    String unitTypeCode = item.getUnitTypeCode();
                    String orgLevelCode = item.getOrgLevelCode();

                    if (commonDtos.containsKey(unitTypeCode)) {
                        item.setUnitTypeCode(commonDtos.get(unitTypeCode));
                    }

                    if (commonDtos.containsKey(orgLevelCode)) {
                        item.setOrgLevelCode(commonDtos.get(orgLevelCode));
                    }
                });

        return res;
    }

    @Override
    public List<HrmCfgOrgUnitOptionDto> listOptions() {
        return cfgOrgUnitRepository.findAllByIsDelete(0).stream()
                .map(item -> new HrmCfgOrgUnitOptionDto(
                        item.getOrgCode(),
                        item.getOrgUnitCode(),
                        item.getOrgUnitName()
                ))
                .toList();
    }
}
