package ngvgroup.com.fac.feature.fac_cfg_acct_entry.service.impl;

import com.ngvgroup.bpm.core.persistence.service.impl.BaseServiceImpl;
import ngvgroup.com.fac.feature.common.dto.CtgCfgCommonDTO;
import ngvgroup.com.fac.feature.common.repository.CtgCfgCommonRepository;
import ngvgroup.com.fac.feature.fac_cfg_acct_entry.dto.request.FacCfgAcctEntryDTO;
import ngvgroup.com.fac.feature.fac_cfg_acct_entry.dto.response.FacCfgAcctEntryResDTO;
import ngvgroup.com.fac.feature.fac_cfg_acct_entry.dto.search.AcctEntrySearchDetail;
import ngvgroup.com.fac.feature.fac_cfg_acct_entry.mapper.FacCfgAcctEntryMapper;
import ngvgroup.com.fac.feature.fac_cfg_acct_entry.model.FacCfgAcctEntry;
import ngvgroup.com.fac.feature.fac_cfg_acct_entry.repository.FacCfgAcctEntryRepository;
import ngvgroup.com.fac.feature.fac_cfg_acct_entry.service.FacCfgAcctEntryService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class FacCfgAcctEntryServiceImpl
        extends BaseServiceImpl<FacCfgAcctEntry, FacCfgAcctEntryDTO>
        implements FacCfgAcctEntryService {
    private final FacCfgAcctEntryRepository entryRepo;
    private final CtgCfgCommonRepository commonRepository;

    protected FacCfgAcctEntryServiceImpl(
            FacCfgAcctEntryMapper facCfgAcctEntryMapper, FacCfgAcctEntryRepository entryRepo, CtgCfgCommonRepository commonRepository) {
        super(entryRepo, facCfgAcctEntryMapper);
        this.entryRepo = entryRepo;
        this.commonRepository = commonRepository;
    }

    @Override
    public Page<FacCfgAcctEntryResDTO> getListDetail(AcctEntrySearchDetail searchDetail, Pageable pageable) {
        String acctProcessCode = searchDetail.getAcctProcessCode();
        String orgCode = searchDetail.getOrgCode();

        return entryRepo.getListDetail(acctProcessCode, orgCode, pageable);
    }

    @Override
    public List<CtgCfgCommonDTO> getListEntryTypeCode(String moduleCode, String commonTypeCode) {
        return commonRepository.getListEntryTypeCode(moduleCode, commonTypeCode);
    }
}
