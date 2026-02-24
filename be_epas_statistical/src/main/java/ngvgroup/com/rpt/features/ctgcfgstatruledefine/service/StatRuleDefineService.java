package ngvgroup.com.rpt.features.ctgcfgstatruledefine.service;

import com.ngvgroup.bpm.core.persistence.service.BaseService;
import ngvgroup.com.rpt.features.ctgcfgstatruledefine.dto.ReqSearchStatRuleDefineDto;
import ngvgroup.com.rpt.features.ctgcfgstatruledefine.dto.ResponseSearchStatRuleDefineDto;
import ngvgroup.com.rpt.features.ctgcfgstatruledefine.dto.StatRuleDefineDto;
import ngvgroup.com.rpt.features.ctgcfgstatruledefine.model.StatRuleDefine;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;


public interface StatRuleDefineService extends BaseService<StatRuleDefine, StatRuleDefineDto> {
    Page<ResponseSearchStatRuleDefineDto> search(String search, ReqSearchStatRuleDefineDto body, Pageable pageable);
}
