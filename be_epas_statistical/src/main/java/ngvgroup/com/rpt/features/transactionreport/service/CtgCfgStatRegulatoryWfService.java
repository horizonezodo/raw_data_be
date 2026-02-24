package ngvgroup.com.rpt.features.transactionreport.service;

import com.ngvgroup.bpm.core.persistence.service.BaseService;

import ngvgroup.com.rpt.features.ctgcfgstattemplate.dto.ctgcfgstatregulatorytype.CtgCfgStatRegulatoryTypeDTO;
import ngvgroup.com.rpt.features.transactionreport.model.CtgCfgStatRegulatoryWf;

public interface CtgCfgStatRegulatoryWfService extends BaseService<CtgCfgStatRegulatoryWf, CtgCfgStatRegulatoryTypeDTO> {
    Long findIdByRegulatoryCode(String code);
}
