package ngvgroup.com.rpt.features.ctgcfgstattemplate.service.impl;

import com.ngvgroup.bpm.core.common.exception.BusinessException;
import com.ngvgroup.bpm.core.common.exception.ErrorCode;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import ngvgroup.com.rpt.core.constant.StatisticalErrorCode;
import ngvgroup.com.rpt.core.constant.VariableConstants;
import ngvgroup.com.rpt.features.ctgcfgstattemplate.dto.ctgcfgstatregulatorytype.CtgCfgStatRegulatoryTypeDTO;
import ngvgroup.com.rpt.features.ctgcfgstattemplate.mapper.CtgCfgStatRegulatoryTypeMapper;
import ngvgroup.com.rpt.features.ctgcfgstattemplate.model.CtgCfgStatRegulatoryType;
import ngvgroup.com.rpt.features.ctgcfgstattemplate.repository.CtgCfgStatRegulatoryTypeRepository;
import ngvgroup.com.rpt.features.ctgcfgstattemplate.service.CtgCfgStatRegulatoryTypeService;
import ngvgroup.com.rpt.features.transactionreport.service.CtgCfgStatRegulatoryWfService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class CtgCfgStatRegulatoryTypeServiceImpl implements CtgCfgStatRegulatoryTypeService {
    private final CtgCfgStatRegulatoryTypeRepository repo;
    private final CtgCfgStatRegulatoryTypeMapper mapper;
    private final CtgCfgStatRegulatoryWfService service;

    @Override
    public List<CtgCfgStatRegulatoryTypeDTO> getAllData() {
        return repo.getAllData();
    }

    @Override
    public Page<CtgCfgStatRegulatoryTypeDTO> pageRegulatory(String keyword, Pageable pageable) {
        return repo.pageData(keyword, pageable);
    }

    @Override
    public List<CtgCfgStatRegulatoryTypeDTO> exportExcel(String search) {
        return repo.exportExcelData(search);
    }

    @Override
    public void createRegulatory(CtgCfgStatRegulatoryTypeDTO dto) {
        this.validateBeforeCreate(dto);
        CtgCfgStatRegulatoryType type = this.mapper.toEntity(dto);
        type.setRecordStatus(VariableConstants.DD);
        this.repo.save(type);
        this.service.create(dto);
    }

    @Override
    @Transactional
    public void updateRegulatory(CtgCfgStatRegulatoryTypeDTO dto, Long id) {
        Optional<CtgCfgStatRegulatoryType> opt = this.repo.findById(id);
        if(opt.isEmpty()) throw new BusinessException(ErrorCode.NOT_FOUND,dto.getRegulatoryTypeCode());
        else{
            CtgCfgStatRegulatoryType type =opt.get();
            this.validateBeforeUpdate(dto,type);
            this.mapper.updateEntityFromDto(dto,type);
            this.repo.save(type);
            this.service.update(this.service.findIdByRegulatoryCode(dto.getRegulatoryTypeCode()),dto);
        }
    }

    @Override
    @Modifying
    public void deleteRegulatory(Long id) {
        Optional<CtgCfgStatRegulatoryType> opt = this.repo.findById(id);
        if(opt.isEmpty()) throw new BusinessException(ErrorCode.NOT_FOUND,id);
        else{
            CtgCfgStatRegulatoryType type =opt.get();
            this.repo.delete(type);
            this.service.delete(this.service.findIdByRegulatoryCode(type.getRegulatoryTypeCode()));
        }
    }

    @Override
    public CtgCfgStatRegulatoryTypeDTO getDetail(Long id) {
        return this.repo.getDetailById(id);
    }

    void validateBeforeCreate(CtgCfgStatRegulatoryTypeDTO dto) {
        repo.findByRegulatoryTypeCode(dto.getRegulatoryTypeCode())
                .ifPresent(existing -> {
                    throw new BusinessException(StatisticalErrorCode.NOT_EXISTS,dto.getRegulatoryTypeCode());
                });
    }

    void validateBeforeUpdate(CtgCfgStatRegulatoryTypeDTO dto, CtgCfgStatRegulatoryType entity) {
        repo.findByRegulatoryTypeCode(dto.getRegulatoryTypeCode())
                .ifPresent(existing -> {
                    if (!existing.getId().equals(entity.getId())) {
                        throw new BusinessException(StatisticalErrorCode.NOT_EXISTS,dto.getRegulatoryTypeCode());
                    }
                });
    }
}
