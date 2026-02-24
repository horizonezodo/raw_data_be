package ngvgroup.com.rpt.features.ctgcfgstatruletype.service;

import com.ngvgroup.bpm.core.persistence.service.BaseService;
import ngvgroup.com.rpt.features.ctgcfgstatruletype.dto.ResponseSearchStatRuleTypeDto;
import ngvgroup.com.rpt.features.ctgcfgstatruletype.dto.StatRuleTypeDto;
import ngvgroup.com.rpt.features.ctgcfgstatruletype.model.StatRuleType;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;


public interface StatRuleTypeService extends BaseService<StatRuleType, StatRuleTypeDto> {
    Page<ResponseSearchStatRuleTypeDto> search(String search, Pageable page);
}
