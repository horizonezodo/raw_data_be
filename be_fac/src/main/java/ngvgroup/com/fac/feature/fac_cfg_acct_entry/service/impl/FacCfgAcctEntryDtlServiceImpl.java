package ngvgroup.com.fac.feature.fac_cfg_acct_entry.service.impl;

import com.ngvgroup.bpm.core.persistence.service.impl.BaseServiceImpl;
import ngvgroup.com.fac.feature.fac_cfg_acct_entry.dto.request.FacCfgAcctEntryDtlDTO;
import ngvgroup.com.fac.feature.fac_cfg_acct_entry.dto.response.FacCfgAcctEntryDtlResDTO;
import ngvgroup.com.fac.feature.fac_cfg_acct_entry.mapper.FacCfgAcctEntryDtlMapper;
import ngvgroup.com.fac.feature.fac_cfg_acct_entry.model.FacCfgAcctEntryDtl;
import ngvgroup.com.fac.feature.fac_cfg_acct_entry.repository.FacCfgAcctEntryDtlRepository;
import ngvgroup.com.fac.feature.fac_cfg_acct_entry.service.FacCfgAcctEntryDtlService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class FacCfgAcctEntryDtlServiceImpl
        extends BaseServiceImpl<FacCfgAcctEntryDtl, FacCfgAcctEntryDtlDTO>
        implements FacCfgAcctEntryDtlService {
    private final FacCfgAcctEntryDtlRepository dtlRepository;

    protected FacCfgAcctEntryDtlServiceImpl(
            FacCfgAcctEntryDtlRepository facCfgAcctEntryDtlRepository,
            FacCfgAcctEntryDtlMapper dtlMapper) {
        super(facCfgAcctEntryDtlRepository, dtlMapper);
        this.dtlRepository = facCfgAcctEntryDtlRepository;
    }

    @Override
    public List<FacCfgAcctEntryDtlResDTO> getListEntryDetail(String entryCode) {
        return dtlRepository.getListEntryDetail(entryCode);
    }

    @Override
    public List<String> getAccClassCode(String entryTypeCode, String entryType) {
        return dtlRepository.getAccClassCode(entryTypeCode, entryType);
    }
}
