package ngvgroup.com.bpm.features.com_cfg_txn_content.service.impl;

import com.ngvgroup.bpm.core.common.excel.ExportExcel;
import com.ngvgroup.bpm.core.common.exception.BusinessException;
import com.ngvgroup.bpm.core.common.exception.ErrorCode;
import com.ngvgroup.bpm.core.persistence.service.impl.BaseServiceImpl;
import lombok.extern.slf4j.Slf4j;
import ngvgroup.com.bpm.core.contants.BpmErrorCode;
import ngvgroup.com.bpm.core.contants.StatusConstants;
import ngvgroup.com.bpm.features.com_cfg_txn_content.dto.*;
import ngvgroup.com.bpm.features.com_cfg_txn_content.mapper.ComCfgTxnContentMapper;
import ngvgroup.com.bpm.features.com_cfg_txn_content.model.ComCfgTxnContent;
import ngvgroup.com.bpm.features.com_cfg_txn_content.model.ComCfgTxnContentDtl;
import ngvgroup.com.bpm.features.com_cfg_txn_content.repository.ComCfgTxnContentDtlRepository;
import ngvgroup.com.bpm.features.com_cfg_txn_content.repository.ComCfgTxnContentRepository;
import ngvgroup.com.bpm.features.com_cfg_txn_content.service.ComCfgTxnContentService;
import ngvgroup.com.bpm.features.common.dto.ComCfgBusModuleDto;
import ngvgroup.com.bpm.features.common.feign.ComCfgBusModuleFeign;
import ngvgroup.com.bpm.features.common.model.CtgComCommon;
import ngvgroup.com.bpm.features.common.repository.CtgComCommonRepository;
import ngvgroup.com.bpm.features.sla.model.ComCfgProcessType;
import ngvgroup.com.bpm.features.sla.repository.ComCfgProcessTypeRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

@Slf4j
@Service
public class ComCfgTxnContentServiceImpl extends BaseServiceImpl<ComCfgTxnContent, ComCfgTxnContentDto> implements ComCfgTxnContentService {

    private final ComCfgTxnContentRepository comCfgTxnContentRepository;
    private final ComCfgProcessTypeRepository comCfgProcessTypeRepository;
    private final ComCfgTxnContentDtlRepository comCfgTxnContentDtlRepository;
    private final CtgComCommonRepository ctgComCommonRepository;
    private final ExportExcel exportExcel;
    private final ComCfgBusModuleFeign feign;

    public ComCfgTxnContentServiceImpl(ComCfgTxnContentRepository comCfgTxnContentRepository, ComCfgProcessTypeRepository comCfgProcessTypeRepository, ComCfgTxnContentMapper mapper, ExportExcel exportExcel, ComCfgTxnContentDtlRepository comCfgTxnContentDtlRepository, CtgComCommonRepository ctgComCommonRepository, ComCfgBusModuleFeign feign) {
        super(comCfgTxnContentRepository, mapper);
        this.comCfgTxnContentRepository = comCfgTxnContentRepository;
        this.comCfgProcessTypeRepository = comCfgProcessTypeRepository;
        this.comCfgTxnContentDtlRepository = comCfgTxnContentDtlRepository;
        this.ctgComCommonRepository = ctgComCommonRepository;
        this.exportExcel = exportExcel;
        this.feign = feign;
    }

    @Override
    public List<ComCfgTxnContentDto> findAllByProcessTypeCode(String processTypeCode) {
        return comCfgTxnContentRepository.findAllByModuleCode(processTypeCode);
    }

    @Override
    public List<ComCfgTxnContentDto> search() {

        List<ComCfgTxnContentDto> parents =
                comCfgTxnContentRepository.search();

        if (parents.isEmpty()) {
            return parents;
        }

        Map<String, String> moduleMap = this.getListModule()
                .stream()
                .collect(Collectors.toMap(
                        ComCfgBusModuleDto::getModuleCode,
                        ComCfgBusModuleDto::getModuleName,
                        (a, b) -> a
                ));

        parents.forEach(item ->
                item.setModuleName(
                        moduleMap.get(item.getModuleCode())
                )
        );

        List<String> contentCodes = parents.stream()
                .map(ComCfgTxnContentDto::getContentCode)
                .filter(Objects::nonNull)
                .toList();

        List<ComCfgTxnContentDtl> details =
                comCfgTxnContentDtlRepository.findByContentCodes(contentCodes);

        Set<String> commonCodes = details.stream()
                .map(ComCfgTxnContentDtl::getContentValueType)
                .filter(Objects::nonNull)
                .collect(Collectors.toSet());

        List<CtgComCommon> commons =
                ctgComCommonRepository.findAllByCommonCodeIn((commonCodes).stream().toList());

        Map<String, String> commonMap = commons.stream()
                .collect(Collectors.toMap(
                        CtgComCommon::getCommonCode,
                        CtgComCommon::getCommonName,
                        (a, b) -> a
                ));

        Map<String, List<ComCfgTxnContentDtlDto>> detailMap =
                details.stream()
                        .collect(Collectors.groupingBy(
                                ComCfgTxnContentDtl::getContentCode,
                                Collectors.mapping(d ->
                                                new ComCfgTxnContentDtlDto(
                                                        d.getId(),
                                                        d.getOrgCode(),
                                                        d.getContentDtlCode(),
                                                        commonMap.get(d.getContentValueType()),
                                                        d.getContentValue(),
                                                        d.getSortNumber(),
                                                        d.getFormatMask(),
                                                        d.getLength()
                                                ),
                                        Collectors.toList()
                                )
                        ));

        parents.forEach(p ->
                p.setDetails(
                        detailMap.getOrDefault(p.getContentCode(), List.of())
                )
        );

        return parents;
    }


    @Override
    public ComCfgTxnContentDto getDetail(Long id) {
        ComCfgTxnContentDto comCfgTxnContentDto = this.findById(id);
        ComCfgProcessType comCfgProcessType = comCfgProcessTypeRepository.findByProcessTypeCode(comCfgTxnContentDto.getModuleCode()).orElseThrow(() -> new BusinessException(ErrorCode.BAD_REQUEST));
        comCfgTxnContentDto.setModuleName(comCfgProcessType.getProcessTypeName());
        return comCfgTxnContentDto;
    }

    @Override
    public ResponseEntity<byte[]> exportExcel(List<ComCfgTxnContentDto> list) {
        try {
            return exportExcel.exportExcel(list, "Cau_truc_dien_giai");
        } catch (Exception e) {
            throw new BusinessException(BpmErrorCode.WRITE_EXCEL_ERROR);
        }
    }

    @Override
    @Transactional
    public void save(ComCfgTxnContentSaveDto dto) {

        ComCfgTxnContent content = (dto.getId() != null)
                ? comCfgTxnContentRepository.findById(dto.getId())
                .orElseThrow(() -> new BusinessException(ErrorCode.BAD_REQUEST))
                : new ComCfgTxnContent();

        content.setContentCode(dto.getContentCode());
        content.setContentName(dto.getContentName());
        content.setOrgCode(dto.getOrgCode());
        content.setModuleCode(dto.getModuleCode());
        content.setLength(dto.getLength());
        content.setContentText(dto.getContentText());
        content.setIsActive(1);
        content.setRecordStatus(StatusConstants.APPROVAL);

        comCfgTxnContentRepository.save(content);

        List<ComCfgTxnContentDtl> dbDetails =
                comCfgTxnContentDtlRepository.findAllByContentCode(dto.getContentCode());

        Map<Long, ComCfgTxnContentDtl> dbDetailMap =
                dbDetails.stream()
                        .filter(d -> d.getId() != null)
                        .collect(Collectors.toMap(
                                ComCfgTxnContentDtl::getId,
                                Function.identity()
                        ));

        Set<Long> incomingIds = new HashSet<>();
        List<ComCfgTxnContentDtl> toSave = new ArrayList<>();

        if (dto.getDetails() != null) {
            for (ComCfgTxnContentSaveDtlDto d : dto.getDetails()) {

                ComCfgTxnContentDtl entity;

                if (d.getId() != null && dbDetailMap.containsKey(d.getId())) {
                    entity = dbDetailMap.get(d.getId());
                    incomingIds.add(d.getId());
                }
                else {
                    entity = new ComCfgTxnContentDtl();
                    entity.setOrgCode(dto.getOrgCode());
                    entity.setContentCode(dto.getContentCode());
                }

                entity.setSortNumber(d.getSortNumber());
                entity.setContentDtlCode(d.getContentDtlCode());
                entity.setContentValueType(d.getContentValueType());
                entity.setContentValue(d.getContentValue());
                entity.setFormatMask(d.getFormatMask());
                entity.setLength(d.getLength());

                toSave.add(entity);
            }
        }

        List<ComCfgTxnContentDtl> toDelete =
                dbDetails.stream()
                        .filter(d -> d.getId() != null && !incomingIds.contains(d.getId()))
                        .toList();

        if (!toDelete.isEmpty()) {
            comCfgTxnContentDtlRepository.deleteAll(toDelete);
        }

        if (!toSave.isEmpty()) {
            comCfgTxnContentDtlRepository.saveAll(toSave);
        }
    }

    @Override
    public boolean existsByContentCode(String contentCode) {
        return comCfgTxnContentRepository.existsByContentCode(contentCode);
    }

    @Override
    @Transactional
    public void delete(Long id) {
        ComCfgTxnContent content = comCfgTxnContentRepository.findById(id)
                .orElseThrow(() -> new BusinessException(ErrorCode.NOT_FOUND));

        String contentCode = content.getContentCode();
        comCfgTxnContentDtlRepository.deleteAllByContentCode(contentCode);
        comCfgTxnContentRepository.deleteById(id);
    }

    public Page<ComCfgTxnContentDtlDto> detailSearch(
            String contentCode,
            String keyword,
            Pageable pageable
    ) {
        return comCfgTxnContentDtlRepository.search(
                contentCode,
                (keyword == null || keyword.isBlank()) ? null : keyword,
                pageable
        );
    }

    @Override
    public ResponseEntity<byte[]> exportExcelDetail(List<ComCfgTxnContentSaveDtlDto> comCfgTxnContentSaveDtlDtos, String fileName) {
        try {
            return exportExcel.exportExcel(comCfgTxnContentSaveDtlDtos, fileName);
        } catch (Exception e) {
            throw new BusinessException(BpmErrorCode.WRITE_EXCEL_ERROR);
        }
    }

    @Override
    public boolean existsByContentCodeAndContentDtlCode(String contentCode, String contentDtlCode) {
        return comCfgTxnContentDtlRepository.existsByContentCodeAndContentDtlCodeAndIsActive(contentCode, contentDtlCode, 1);
    }

    private List<ComCfgBusModuleDto> getListModule() {
        var lst = feign.getAll();
        return lst.getData();
    }

}
