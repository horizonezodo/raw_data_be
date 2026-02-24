package ngvgroup.com.loan.feature.type_of_capital_use.service.impl;

import com.ngvgroup.bpm.core.common.excel.ExportExcel;
import com.ngvgroup.bpm.core.common.exception.BusinessException;
import com.ngvgroup.bpm.core.common.exception.ErrorCode;
import ngvgroup.com.loan.core.constant.LoanErrorCode;
import ngvgroup.com.loan.core.constant.LoanVariableConstants;
import ngvgroup.com.loan.core.constant.VariableConstants;
import ngvgroup.com.loan.feature.organization.dto.ComInfOrganizationDto;
import ngvgroup.com.loan.feature.organization.service.ComInfOrganizationService;
import ngvgroup.com.loan.feature.type_of_capital_use.dto.LnmCfgCapitalUseDTO;
import ngvgroup.com.loan.feature.type_of_capital_use.mapper.LnmCfgCapitalUseMapper;
import ngvgroup.com.loan.feature.type_of_capital_use.model.LnmCfgCapitalUse;
import ngvgroup.com.loan.feature.type_of_capital_use.repository.LnmCfgCapitalUseRepository;
import ngvgroup.com.loan.feature.type_of_capital_use.service.LnmCfgCapitalUseRateService;
import ngvgroup.com.loan.feature.type_of_capital_use.service.LnmCfgCapitalUseService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class LnmCfgCapitalUseServiceImpl  implements LnmCfgCapitalUseService {
    private final LnmCfgCapitalUseRepository capitalUseRepository;
    private final ExportExcel exportExcel;
    private final LnmCfgCapitalUseMapper mapper;
    private final LnmCfgCapitalUseRateService capitalUseRateService;
    private final ComInfOrganizationService organizationService;

    protected LnmCfgCapitalUseServiceImpl(
            LnmCfgCapitalUseRepository repository,
            LnmCfgCapitalUseMapper mapper,
            ExportExcel exportExcel,
            LnmCfgCapitalUseRateService capitalUseRateService,
            ComInfOrganizationService organizationService) {
        this.capitalUseRepository = repository;
        this.exportExcel =exportExcel;
        this.mapper = mapper;
        this.capitalUseRateService = capitalUseRateService;
        this.organizationService = organizationService;
    }

    private void validateBeforeCreate(LnmCfgCapitalUseDTO dto) {
        this.capitalUseRepository.findByCapitalUseCodeAndOrgCode(dto.getCapitalUseCode(), dto.getOrgCode())
                .ifPresent(existing -> {
                    throw new BusinessException(ErrorCode.CONFLICT, dto.getCapitalUseCode());
                });
    }

    private void validateBeforeUpdate(LnmCfgCapitalUseDTO dto, LnmCfgCapitalUse entity) {
        this.capitalUseRepository.findByCapitalUseCodeAndOrgCode(dto.getCapitalUseCode(),dto.getOrgCode())
                .ifPresent(existing -> {
                    if (!existing.getId().equals(entity.getId())) {
                        throw new BusinessException(ErrorCode.NOT_FOUND, dto.getCapitalUseCode());
                    }
                });
    }


    private void beforeSaveCreate(LnmCfgCapitalUse entity) {
        entity.setRecordStatus(VariableConstants.APPROVE);
    }

    @Override
    public Page<LnmCfgCapitalUseDTO> search(String keyword, Pageable pageable) {
        return capitalUseRepository.search(keyword, pageable);
    }

    @Override
    public ResponseEntity<byte[]> exportToExcel(String fileName) {
        try{
            List<LnmCfgCapitalUseDTO> lst = this.mapper.toListDto(this.capitalUseRepository.findAll());
            String exportFileName ="Danh_sach_loai_su_dung_von";
            return exportExcel.exportExcel(lst, exportFileName);
        }catch (Exception e){
            throw new BusinessException(LoanErrorCode.WRITE_EXCEL_ERROR, e);
        }
    }

    @Override
    public List<String> getAllCode() {
        return capitalUseRepository.getAllCapitalCode();
    }

    @Override
    @Transactional
    public LnmCfgCapitalUseDTO create(LnmCfgCapitalUseDTO dto) {
        if (!"%".equals(dto.getOrgCode())) {
            LnmCfgCapitalUse entity = createSingleOrg(dto, dto.getOrgCode());
            return this.mapper.toDto(entity);
        }

        // Nếu orgCode == "%" -> tạo cho tất cả org
        List<ComInfOrganizationDto> orgCodes = organizationService.listOrganizations(); // bạn chỉnh lại tên method cho đúng

        LnmCfgCapitalUse lastEntity = null;
        for (ComInfOrganizationDto orgCode : orgCodes) {
            lastEntity = createSingleOrg(dto, orgCode.getOrgCode());
        }

        return this.mapper.toDto(lastEntity);
    }

    private LnmCfgCapitalUse createSingleOrg(LnmCfgCapitalUseDTO dto, String orgCode) {
        dto.setOrgCode(orgCode);

        this.validateBeforeCreate(dto);

        LnmCfgCapitalUse entity = this.mapper.toEntity(dto);
        this.beforeSaveCreate(entity);
        this.capitalUseRepository.save(entity);

        dto.getCapitalUseRateDTOS().forEach(d -> {
            d.setCapitalUseCode(dto.getCapitalUseCode());
            d.setOrgCode(orgCode);
        });

        this.capitalUseRateService.saveOrUpdateAll(dto.getCapitalUseRateDTOS(), LoanVariableConstants.CREATE);

        return entity;
    }


    @Override
    @Transactional
    public LnmCfgCapitalUseDTO update(Long id, LnmCfgCapitalUseDTO dto) {
        if (!"%".equals(dto.getOrgCode())) {
            LnmCfgCapitalUse entity = updateSingleOrg(id, dto, dto.getOrgCode());
            return this.mapper.toDto(entity);
        }

        // Nếu orgCode == "%" -> update cho tất cả org
        List<ComInfOrganizationDto> orgCodes = organizationService.listOrganizations(); // chỉnh lại method cho đúng

        LnmCfgCapitalUse lastEntity = null;

        for (ComInfOrganizationDto orgCode : orgCodes) {
            LnmCfgCapitalUse entity = this.capitalUseRepository
                    .findByCapitalUseCodeAndOrgCode(dto.getCapitalUseCode(), orgCode.getOrgCode())
                    .orElseThrow(() -> new BusinessException(ErrorCode.NOT_FOUND, dto.getCapitalUseCode()));

            lastEntity = updateSingleOrg(entity.getId(), dto, orgCode.getOrgCode());
        }

        return this.mapper.toDto(lastEntity);
    }

    private LnmCfgCapitalUse updateSingleOrg(Long id, LnmCfgCapitalUseDTO dto, String orgCode) {
        LnmCfgCapitalUse entity = this.capitalUseRepository
                .findById(id)
                .orElseThrow(() -> new BusinessException(ErrorCode.NOT_FOUND, id));

        dto.setOrgCode(orgCode);

        this.validateBeforeUpdate(dto, entity);

        this.mapper.updateEntityFromDto(dto, entity);
        this.capitalUseRepository.save(entity);

        dto.getCapitalUseRateDTOS().forEach(d -> {
            d.setCapitalUseCode(dto.getCapitalUseCode());
            d.setOrgCode(orgCode);
        });

        this.capitalUseRateService.saveOrUpdateAll(dto.getCapitalUseRateDTOS(), LoanVariableConstants.UPDATE);

        return entity;
    }


    @Override
    @Transactional
    @Modifying
    public void delete(Long id) {
        LnmCfgCapitalUse capitalUse = this.capitalUseRepository.findById(id).orElseThrow(() -> new BusinessException(ErrorCode.NOT_FOUND, id));
        this.capitalUseRateService.deleteAll(capitalUse.getCapitalUseCode(), capitalUse.getOrgCode());
        this.capitalUseRepository.deleteById(id);
    }

    @Override
    public LnmCfgCapitalUseDTO findById(Long id) {
        LnmCfgCapitalUseDTO cfgCapitalUseDTO = this.mapper.toDto(this.capitalUseRepository.findById(id).orElseThrow(() -> new BusinessException(ErrorCode.NOT_FOUND, id)));
        cfgCapitalUseDTO.setCapitalUseRateDTOS(this.capitalUseRateService.getAllByCapitalUseCodeAndOrgCode(cfgCapitalUseDTO.getCapitalUseCode(), cfgCapitalUseDTO.getOrgCode()));
        return cfgCapitalUseDTO;
    }

    @Override
    public List<LnmCfgCapitalUseDTO> findAll() {
         return this.mapper.toListDto(this.capitalUseRepository.findAll());
    }
}
