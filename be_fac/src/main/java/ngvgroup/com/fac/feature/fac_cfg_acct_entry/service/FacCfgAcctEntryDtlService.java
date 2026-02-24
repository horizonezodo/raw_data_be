package ngvgroup.com.fac.feature.fac_cfg_acct_entry.service;

import com.ngvgroup.bpm.core.persistence.service.BaseService;
import ngvgroup.com.fac.feature.fac_cfg_acct_entry.dto.request.FacCfgAcctEntryDtlDTO;
import ngvgroup.com.fac.feature.fac_cfg_acct_entry.dto.response.FacCfgAcctEntryDtlResDTO;
import ngvgroup.com.fac.feature.fac_cfg_acct_entry.model.FacCfgAcctEntryDtl;

import java.util.List;

public interface FacCfgAcctEntryDtlService extends BaseService<FacCfgAcctEntryDtl, FacCfgAcctEntryDtlDTO> {
    List<FacCfgAcctEntryDtlResDTO> getListEntryDetail(String entryCode);
    List<String> getAccClassCode(String entryTypeCode, String entryType);
}
