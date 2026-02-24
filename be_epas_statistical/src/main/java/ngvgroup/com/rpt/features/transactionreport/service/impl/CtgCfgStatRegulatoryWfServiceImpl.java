package ngvgroup.com.rpt.features.transactionreport.service.impl;

import com.ngvgroup.bpm.core.common.exception.BusinessException;
import com.ngvgroup.bpm.core.common.exception.ErrorCode;
import com.ngvgroup.bpm.core.persistence.service.impl.BaseServiceImpl;
import ngvgroup.com.rpt.core.constant.VariableConstants;
import ngvgroup.com.rpt.features.ctgcfgstattemplate.dto.ctgcfgstatregulatorytype.CtgCfgStatRegulatoryTypeDTO;
import ngvgroup.com.rpt.features.transactionreport.mapper.CtgCfgStatRegulatoryWfMapper;
import ngvgroup.com.rpt.features.transactionreport.model.CtgCfgStatRegulatoryWf;
import ngvgroup.com.rpt.features.transactionreport.repository.CtgCfgStatRegulatoryWfRepository;
import ngvgroup.com.rpt.features.transactionreport.service.CtgCfgStatRegulatoryWfService;
import org.springframework.stereotype.Service;

@Service
public class CtgCfgStatRegulatoryWfServiceImpl extends BaseServiceImpl<CtgCfgStatRegulatoryWf,CtgCfgStatRegulatoryTypeDTO> implements CtgCfgStatRegulatoryWfService {
    private final CtgCfgStatRegulatoryWfRepository repo;

    @Override
    protected void beforeSaveCreate(CtgCfgStatRegulatoryWf entity, CtgCfgStatRegulatoryTypeDTO dto) {
        entity.setOrgCode(VariableConstants.ORG);
        entity.setRecordStatus(VariableConstants.DD);
    }

    protected CtgCfgStatRegulatoryWfServiceImpl(
            CtgCfgStatRegulatoryWfRepository repository,
            CtgCfgStatRegulatoryWfMapper mapper) {
        super(repository, mapper);
        this.repo = repository;
    }

    @Override
    public Long findIdByRegulatoryCode(String code) {
        CtgCfgStatRegulatoryWf wf = this.repo.findByRegulatoryTypeCode(code);
        if(wf == null){
            throw new BusinessException(ErrorCode.NOT_FOUND,code);
        }
        return wf.getId();
    }
}
