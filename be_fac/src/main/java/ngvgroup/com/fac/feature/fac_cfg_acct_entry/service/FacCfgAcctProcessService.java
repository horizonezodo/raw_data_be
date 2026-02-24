package ngvgroup.com.fac.feature.fac_cfg_acct_entry.service;

import com.ngvgroup.bpm.core.persistence.service.BaseService;
import ngvgroup.com.fac.feature.fac_cfg_acct_entry.dto.request.FacCfgAcctProcessDTO;
import ngvgroup.com.fac.feature.fac_cfg_acct_entry.dto.response.FacCfgAcctProcessResDTO;
import ngvgroup.com.fac.feature.fac_cfg_acct_entry.dto.search.FacCfgAcctEntrySearch;
import ngvgroup.com.fac.feature.fac_cfg_acct_entry.model.FacCfgAcctProcess;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface FacCfgAcctProcessService extends BaseService<FacCfgAcctProcess, FacCfgAcctProcessDTO> {
    FacCfgAcctProcessDTO addProcess(FacCfgAcctProcessDTO processDTO);
    Page<FacCfgAcctProcessResDTO> getGeneralList(FacCfgAcctEntrySearch search, Pageable pageable);
    List<FacCfgAcctProcessResDTO> exportToExcel();
    FacCfgAcctProcessDTO updateAcctProcess(FacCfgAcctProcessDTO processDTO);
    void deleteAcctProcess(String processTypeCode, String orgCode);
    boolean checkExistProcess(String processTypeCode, String orgCode, boolean isApplyAll);
}
