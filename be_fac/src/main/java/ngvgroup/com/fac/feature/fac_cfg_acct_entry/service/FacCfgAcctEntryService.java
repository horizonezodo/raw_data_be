package ngvgroup.com.fac.feature.fac_cfg_acct_entry.service;

import com.ngvgroup.bpm.core.persistence.service.BaseService;
import ngvgroup.com.fac.feature.common.dto.CtgCfgCommonDTO;
import ngvgroup.com.fac.feature.fac_cfg_acct_entry.dto.request.FacCfgAcctEntryDTO;
import ngvgroup.com.fac.feature.fac_cfg_acct_entry.dto.response.FacCfgAcctEntryResDTO;
import ngvgroup.com.fac.feature.fac_cfg_acct_entry.dto.search.AcctEntrySearchDetail;
import ngvgroup.com.fac.feature.fac_cfg_acct_entry.model.FacCfgAcctEntry;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface FacCfgAcctEntryService extends BaseService<FacCfgAcctEntry, FacCfgAcctEntryDTO> {
    Page<FacCfgAcctEntryResDTO> getListDetail(AcctEntrySearchDetail searchDetail, Pageable pageable);
    List<CtgCfgCommonDTO> getListEntryTypeCode(String moduleCode, String commonTypeCode);
}
