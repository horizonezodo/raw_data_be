package ngvgroup.com.fac.feature.regulated_account.service.impl;

import com.ngvgroup.bpm.core.common.excel.ExportExcel;
import com.ngvgroup.bpm.core.common.exception.BusinessException;
import com.ngvgroup.bpm.core.persistence.service.impl.BaseServiceImpl;

import ngvgroup.com.fac.core.constant.FacErrorCode;
import ngvgroup.com.fac.feature.regulated_account.dto.CoaAccExportProjection;
import ngvgroup.com.fac.feature.regulated_account.dto.CoaAccFilter;
import ngvgroup.com.fac.feature.regulated_account.dto.CtgCfgCoaAccDTO;
import ngvgroup.com.fac.feature.regulated_account.dto.CtgCfgCoaAccResDTO;
import ngvgroup.com.fac.feature.regulated_account.mapper.CtgCfgCoaAccMapper;
import ngvgroup.com.fac.feature.regulated_account.model.CtgCfgCoaAcc;
import ngvgroup.com.fac.feature.regulated_account.repository.CtgCfgCoaAccRepository;
import ngvgroup.com.fac.feature.regulated_account.service.CtgCfgCoaAccService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import java.util.Collections;
import java.util.List;

@Service
public class CtgCfgCoaAccServiceImpl extends BaseServiceImpl<CtgCfgCoaAcc, CtgCfgCoaAccDTO> implements CtgCfgCoaAccService {
    private final CtgCfgCoaAccRepository repo;
    private final ExportExcel exportExcel;

    protected CtgCfgCoaAccServiceImpl(
            CtgCfgCoaAccRepository repository,
            CtgCfgCoaAccMapper mapper,
            ExportExcel exportExcel
            ) {
        super(repository, mapper);
        this.repo = repository;
        this.exportExcel = exportExcel;
    }

    @Override
    protected void validateBeforeCreate(CtgCfgCoaAccDTO dto) {
        this.repo.findByAccCoaCodeAndOrgCodeAndIsInternal(dto.getAccCoaCode(), dto.getOrgCode(),dto.getIsInternal())
                .ifPresent(existing -> {
                    throw new BusinessException(FacErrorCode.DUPLICATE_COA_ACC_CODE, dto.getAccCoaCode());
                });
    }

    @Override
    protected void validateBeforeUpdate(CtgCfgCoaAccDTO dto, CtgCfgCoaAcc entity) {
        this.repo.findByAccCoaCodeAndOrgCodeAndIsInternal(dto.getAccCoaCode(),dto.getOrgCode(),entity.getIsInternal())
                .ifPresent(existing -> {
                    if (!existing.getId().equals(entity.getId())) {
                        throw new BusinessException(FacErrorCode.NOT_FOUND_COA_ACC_CODE, dto.getAccCoaCode());
                    }
                });
    }

    @Override
    public Page<CtgCfgCoaAccResDTO> pageCoaAcc(CoaAccFilter filter, Pageable pageable) {
        return this.repo.pageCoaAcc(filter.getKeyword(), filter.getVersionCode(),filter.getAccTypes(),filter.getIsInternal(),pageable);
    }

    @Override
    public ResponseEntity<byte[]> exportToExcel(String fileName, List<String> isInternals){
        try{
            List<CoaAccExportProjection> lst = this.repo.exportCoaAcc(isInternals);
            return exportExcel.exportExcel(lst, fileName);
        }catch (Exception e){
            throw new BusinessException(FacErrorCode.WRITE_EXCEL_ERROR, e);
        }
    }

    @Override
    public CtgCfgCoaAccDTO getByAccCoaCode(String accCoaCode){
        CtgCfgCoaAccDTO ctgCfgCoaAccDTO=new CtgCfgCoaAccDTO();
        CtgCfgCoaAcc ctgCfgCoaAcc= repo.getByAccCoaCode(accCoaCode);
        ctgCfgCoaAccDTO.setAccCoaName(ctgCfgCoaAcc.getAccCoaName());
        ctgCfgCoaAccDTO.setCoaVersionCode(ctgCfgCoaAcc.getCoaVersionCode());
        return ctgCfgCoaAccDTO;
    }

    @Override
    public List<CtgCfgCoaAccDTO> getAllByIsInternal(String isInternal, String accCoaCode) {
        List<CtgCfgCoaAcc> entities;

        if (accCoaCode == null || accCoaCode.isBlank()) {
            entities = repo.getAllByIsInternal(isInternal);
        } else {
            entities = repo.getAllByIsInternalAndAccCoaCode(isInternal, accCoaCode);
        }

        if (entities.isEmpty()) {
            return Collections.emptyList();
        }

        return entities.stream()
                .map(mapper::toDto)
                .toList();
    }

    @Override
    public CtgCfgCoaAccDTO getByAccCoaCodeAndIsInternal(String accCoaCode,String isInternal){
        return mapper.toDto(repo.getByAccCoaCodeAndIsInternal(accCoaCode,isInternal));
    }
}
