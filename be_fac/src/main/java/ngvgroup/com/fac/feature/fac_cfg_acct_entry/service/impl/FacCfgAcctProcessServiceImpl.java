package ngvgroup.com.fac.feature.fac_cfg_acct_entry.service.impl;

import com.ngvgroup.bpm.core.common.exception.BusinessException;
import com.ngvgroup.bpm.core.persistence.service.impl.BaseServiceImpl;
import ngvgroup.com.fac.core.constant.FacErrorCode;
import ngvgroup.com.fac.core.constant.FacVariableConstants;
import ngvgroup.com.fac.feature.common.component.GenerateCodeService;
import ngvgroup.com.fac.feature.common.dto.ComInfOrganizationDTO;
import ngvgroup.com.fac.feature.common.service.ComInfOrganizationService;
import ngvgroup.com.fac.feature.fac_cfg_acct_entry.dto.request.FacCfgAcctEntryDTO;
import ngvgroup.com.fac.feature.fac_cfg_acct_entry.dto.request.FacCfgAcctEntryDtlDTO;
import ngvgroup.com.fac.feature.fac_cfg_acct_entry.dto.request.FacCfgAcctProcessDTO;
import ngvgroup.com.fac.feature.fac_cfg_acct_entry.dto.response.FacCfgAcctProcessResDTO;
import ngvgroup.com.fac.feature.fac_cfg_acct_entry.dto.search.AcctEntryTreeFilter;
import ngvgroup.com.fac.feature.fac_cfg_acct_entry.dto.search.FacCfgAcctEntrySearch;
import ngvgroup.com.fac.feature.fac_cfg_acct_entry.mapper.FacCfgAcctEntryDtlMapper;
import ngvgroup.com.fac.feature.fac_cfg_acct_entry.mapper.FacCfgAcctEntryMapper;
import ngvgroup.com.fac.feature.fac_cfg_acct_entry.mapper.FacCfgAcctProcessMapper;
import ngvgroup.com.fac.feature.fac_cfg_acct_entry.model.FacCfgAcctEntry;
import ngvgroup.com.fac.feature.fac_cfg_acct_entry.model.FacCfgAcctEntryDtl;
import ngvgroup.com.fac.feature.fac_cfg_acct_entry.model.FacCfgAcctProcess;
import ngvgroup.com.fac.feature.fac_cfg_acct_entry.repository.FacCfgAcctEntryDtlRepository;
import ngvgroup.com.fac.feature.fac_cfg_acct_entry.repository.FacCfgAcctEntryRepository;
import ngvgroup.com.fac.feature.fac_cfg_acct_entry.repository.FacCfgAcctProcessRepository;
import ngvgroup.com.fac.feature.fac_cfg_acct_entry.service.FacCfgAcctProcessService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Stream;

@Service
public class FacCfgAcctProcessServiceImpl
        extends BaseServiceImpl<FacCfgAcctProcess, FacCfgAcctProcessDTO>
        implements FacCfgAcctProcessService {
    private final FacCfgAcctProcessRepository processRepository;
    private final GenerateCodeService generateCodeService;
    private final ComInfOrganizationService organizationService;
    private final FacCfgAcctProcessMapper processMapper;
    private final FacCfgAcctEntryMapper entryMapper;
    private final FacCfgAcctEntryRepository entryRepository;
    private final FacCfgAcctEntryDtlRepository entryDtlRepository;
    private final FacCfgAcctEntryDtlMapper entryDtlMapper;

    public FacCfgAcctProcessServiceImpl(FacCfgAcctProcessRepository processRepository, GenerateCodeService generateCodeService, ComInfOrganizationService organizationService, FacCfgAcctProcessMapper processMapper, FacCfgAcctEntryMapper entryMapper, FacCfgAcctEntryRepository entryRepository, FacCfgAcctEntryDtlRepository entryDtlRepository, FacCfgAcctEntryDtlMapper entryDtlMapper) {
        super(processRepository, processMapper);
        this.processRepository = processRepository;
        this.generateCodeService = generateCodeService;
        this.organizationService = organizationService;
        this.processMapper = processMapper;
        this.entryMapper = entryMapper;
        this.entryRepository = entryRepository;
        this.entryDtlRepository = entryDtlRepository;
        this.entryDtlMapper = entryDtlMapper;
    }

    @Override
    @Transactional
    public FacCfgAcctProcessDTO addProcess(FacCfgAcctProcessDTO processDTO) {
        boolean checkApplyAll = processDTO.getIsApplyAll();
        String processTypeCode = processDTO.getProcessTypeCode();

        if (findExistProcess(processDTO.getOrgCode(), processTypeCode).isEmpty()) {
            if (!checkApplyAll) {
                saveProcessEntity(processDTO);
            } else {
                List<ComInfOrganizationDTO> listOrganizations = organizationService.getAll();
                for (ComInfOrganizationDTO organization : listOrganizations) {
                    processDTO.setOrgCode(organization.getOrgCode());
                    saveProcessEntity(processDTO);
                }
            }
        } else {
            throw new BusinessException(FacErrorCode.ENTRY_ACCT_PROCESS_EXISTED);
        }
        return processDTO;
    }

    private Optional<FacCfgAcctProcess> findExistProcess(String orgCode, String processTypeCode) {
        return processRepository.findByProcessTypeCodeAndOrgCode(
                processTypeCode, orgCode);
    }

    private void saveProcessEntity(FacCfgAcctProcessDTO processDTO) {
        // Create new process entity
        String acctProcessCode = generateAcctProcessCode();
        processDTO.setAcctProcessCode(acctProcessCode);

        FacCfgAcctProcess process = processMapper.toEntity(processDTO);
        String orgCode = processDTO.getOrgCode();
        String modifiedBy = processDTO.getModifiedBy();
        processRepository.save(process);

        if (!CollectionUtils.isEmpty(processDTO.getEntryDTO())) {
            List<FacCfgAcctEntryDTO> entryDTO = processDTO.getEntryDTO();
            saveOrUpdateEntryEntity(entryDTO, orgCode, acctProcessCode, modifiedBy);
        }
    }

    private void saveOrUpdateEntryEntity(
            List<FacCfgAcctEntryDTO> entryDTOList,
            String orgCode,
            String acctProcessCode,
            String modifiedBy) {
        for (FacCfgAcctEntryDTO entryDTO : entryDTOList) {
            FacCfgAcctEntry entry;
            String entryCode;

            if (entryDTO.getId() == null) {
                entry = entryMapper.toEntity(entryDTO);
                entryCode = generateEntryCode();
                entry.setEntryCode(entryCode);
                entry.setOrgCode(orgCode);
                entry.setAcctProcessCode(acctProcessCode);
                entry.setModifiedBy(modifiedBy);
            } else {
                entryCode = entryDTO.getEntryCode();

                entry = entryRepository.findById(entryDTO.getId())
                        .orElseThrow(() -> new BusinessException(FacErrorCode.ENTRY_NOT_EXIST));
                entryMapper.updateEntityFromDto(entryDTO, entry);
                entry.setModifiedBy(modifiedBy);
            }
            entryRepository.save(entry);

            if (!CollectionUtils.isEmpty(entryDTO.getAcctEntryDtlDTO())) {
                List<FacCfgAcctEntryDtlDTO> dtlDTOList = entryDTO.getAcctEntryDtlDTO();
                saveOrUpdateEntityDtl(dtlDTOList, orgCode, entryCode, modifiedBy);
            }

            if (!CollectionUtils.isEmpty(entryDTO.getDeletedIdDtls())) {
                List<Long> deletedIdDtls = entryDTO.getDeletedIdDtls();
                for (long deletedId : deletedIdDtls) {
                    entryDtlRepository.deleteById(deletedId);
                }
            }
        }
    }

    private void saveOrUpdateEntityDtl(List<FacCfgAcctEntryDtlDTO> dtlDTOList, String orgCode, String entryCode, String modifiedBy) {
        List<FacCfgAcctEntryDtl> dtlResult = new ArrayList<>();

        for (FacCfgAcctEntryDtlDTO dtoDtl : dtlDTOList) {
            FacCfgAcctEntryDtl dtl;
            if (dtoDtl.getId() == null) {
                // Create new DTL entity
                dtl = entryDtlMapper.toEntity(dtoDtl);
                dtl.setEntryCode(entryCode);
                dtl.setOrgCode(orgCode);
                dtl.setModifiedBy(modifiedBy);
            } else {
                //update dtl entity
                dtl = entryDtlRepository.findById(dtoDtl.getId())
                        .orElseThrow(() -> new BusinessException(FacErrorCode.ENTRY_DTL_NOT_EXIST));
                entryDtlMapper.updateEntityFromDto(dtoDtl, dtl);
                dtl.setModifiedBy(modifiedBy);
            }
            dtlResult.add(dtl);
        }
        entryDtlRepository.saveAll(dtlResult);
    }

    private String generateAcctProcessCode() {
        return generateCodeService.generateCode(
                "%",
                FacVariableConstants.PREFIX_PROC,
                FacVariableConstants.FAC_CFG_ACCT_PROCESS,
                FacVariableConstants.ACCT_PROCESS_CODE,
                1,
                3,
                ".");
    }

    private String generateEntryCode() {
        return generateCodeService.generateCode(
                "%",
                FacVariableConstants.PREFIX_ENT,
                FacVariableConstants.FAC_CFG_ACCT_ENTRY,
                FacVariableConstants.ENTRY_CODE,
                1,
                3,
                ".");
    }

    @Override
    public Page<FacCfgAcctProcessResDTO> getGeneralList(FacCfgAcctEntrySearch search, Pageable pageable) {
        String keyword = search.getKeyword();
        String orgCode = search.getOrgCode();
        List<AcctEntryTreeFilter> treeFilters = search.getFilters();

        if (treeFilters == null || treeFilters.isEmpty()) {
            return processRepository.getListGeneral(keyword, orgCode, null, null, pageable);
        }

        List<String> moduleCodes = treeFilters.stream()
                .map(AcctEntryTreeFilter::getModuleCode)
                .distinct()
                .toList();

        List<String> processTypeCodes = treeFilters.stream()
                .flatMap(p -> {
                    if (p.getProcessTypeCode() == null || p.getProcessTypeCode().isEmpty()) {
                        return Stream.of((String) null);
                    } else {
                        return p.getProcessTypeCode().stream();
                    }
                })
                .filter(Objects::nonNull)
                .distinct()
                .toList();

        return processRepository.getListGeneralByFilter(keyword, orgCode, moduleCodes, processTypeCodes, pageable);
    }

    @Override
    public List<FacCfgAcctProcessResDTO> exportToExcel() {
        return processRepository.exportToExcel();
    }

    @Override
    @Transactional
    public FacCfgAcctProcessDTO updateAcctProcess(FacCfgAcctProcessDTO processDTO) {

        updateProcessEntity(processDTO);
        return processDTO;
    }

    private void updateProcessEntity(FacCfgAcctProcessDTO processDTO) {
        FacCfgAcctProcess process = processRepository.findByProcessTypeCodeAndOrgCode(
                        processDTO.getProcessTypeCode(), processDTO.getOrgCode())
                .orElseThrow(() -> new BusinessException(FacErrorCode.DATA_NOT_FOUND));

        // Update process entity
        processMapper.updateEntityFromDto(processDTO, process);
        processRepository.save(process);

        String acctProcessCode = process.getAcctProcessCode();
        String orgCode = process.getOrgCode();
        String modifiedBy = process.getModifiedBy();

        if (!CollectionUtils.isEmpty(processDTO.getEntryDTO())) {
            List<FacCfgAcctEntryDTO> entryDTO = processDTO.getEntryDTO();
            saveOrUpdateEntryEntity(entryDTO, orgCode, acctProcessCode, modifiedBy);
        }

        if (!CollectionUtils.isEmpty(processDTO.getDeletedEntryIds())) {
            List<Long> deletedEntryIds = processDTO.getDeletedEntryIds();
            for (Long deletedEntryId : deletedEntryIds) {
                FacCfgAcctEntry facCfgAcctEntry = entryRepository.findById(deletedEntryId)
                        .orElseThrow(null);
                String entryCode = facCfgAcctEntry.getEntryCode();
                entryDtlRepository.deleteByEntryCode(entryCode);

                entryRepository.deleteById(deletedEntryId);
            }
        }
    }

    @Transactional
    @Override
    public void deleteAcctProcess(String processTypeCode, String orgCode) {
        FacCfgAcctProcess process = processRepository.findByProcessTypeCodeAndOrgCode(processTypeCode, orgCode)
                .orElseThrow(() -> new BusinessException(FacErrorCode.DATA_NOT_FOUND));

        String acctProcessCode = process.getAcctProcessCode();
        processRepository.delete(process);

        List<FacCfgAcctEntry> entryList = entryRepository.findByAcctProcessCode(acctProcessCode);
        if (!CollectionUtils.isEmpty(entryList)) {
            deleteEntryEntity(entryList, acctProcessCode);
        }
    }

    public void deleteEntryEntity(List<FacCfgAcctEntry> entryList, String acctProcessCode) {
        entryRepository.deleteByAcctProcessCode(acctProcessCode);
        for (FacCfgAcctEntry entry : entryList) {
            String entryCode = entry.getEntryCode();

            List<FacCfgAcctEntryDtl> dtlList = entryDtlRepository.findByEntryCode(entryCode);
            if (!CollectionUtils.isEmpty(dtlList)) {
                entryDtlRepository.deleteByEntryCode(entryCode);
            }
        }
    }

    @Override
    public boolean checkExistProcess(String processTypeCode, String orgCode, boolean isApplyAll) {
        return isApplyAll ? !processRepository.findByProcessTypeCode(processTypeCode).isEmpty()
                : processRepository.findByProcessTypeCodeAndOrgCode(processTypeCode, orgCode).isPresent();
    }
}
