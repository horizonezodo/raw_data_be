package ngvgroup.com.rpt.features.ctgcfgstattemplate.service.impl;

import com.ngvgroup.bpm.core.common.exception.BusinessException;
import com.ngvgroup.bpm.core.common.exception.ErrorCode;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import ngvgroup.com.rpt.core.constant.StatisticalErrorCode;
import ngvgroup.com.rpt.core.constant.VariableConstants;
import ngvgroup.com.rpt.features.ctgcfgstattemplate.dto.ctgcfgstattemplate.*;
import ngvgroup.com.rpt.features.ctgcfgstattemplate.mapper.CtgCfgStatTemplateMapper;
import ngvgroup.com.rpt.features.ctgcfgstattemplate.model.CtgCfgStatTemplate;
import ngvgroup.com.rpt.features.ctgcfgstattemplate.repository.CtgCfgStatTemplateRepository;
import ngvgroup.com.rpt.features.ctgcfgstattemplate.service.CtgCfgStatTemplateDeadLineService;
import ngvgroup.com.rpt.features.ctgcfgstattemplate.service.CtgCfgStatTemplateKpiService;
import ngvgroup.com.rpt.features.ctgcfgstattemplate.service.CtgCfgStatTemplateService;
import ngvgroup.com.rpt.features.ctgcfgstattemplate.service.CtgCfgStatTemplateSheetService;
import ngvgroup.com.rpt.features.excel.service.ExcelService;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CtgCfgStatTemplateServiceImpl implements CtgCfgStatTemplateService {
    private final CtgCfgStatTemplateRepository ctgCfgStatTemplateRepository;
    private final ExcelService excelService;
    private final CtgCfgStatTemplateMapper ctgCfgStatTemplateMapper;

    private final CtgCfgStatTemplateSheetService ctgCfgStatTemplateSheetService;
    private final CtgCfgStatTemplateKpiService ctgCfgStatTemplateKpiService;
    private final CtgCfgStatTemplateDeadLineService service;

    @Override
    public List<TreeData> getTreeData() {
        List<Object[]> rows = ctgCfgStatTemplateRepository.findAllCirculars();
        // Lọc bỏ những dòng có circularCode null
        Map<String, List<Object[]>> grouped = rows.stream()
                .filter(r -> r[1] != null)
                .collect(Collectors.groupingBy(r -> (String) r[1])); // group theo circularCode
        List<TreeData> result = new ArrayList<>();

        for (Map.Entry<String, List<Object[]>> entry : grouped.entrySet()) {
            String circularCode = entry.getKey();
            List<Object[]> groupRows = entry.getValue();

            // Lấy circularName từ dòng đầu tiên
            String circularName = (String) groupRows.get(0)[0];

            // Tìm danh sách template theo circularCode
            List<CtgCfgStatTemplateDtoV2> templates =
                    ctgCfgStatTemplateRepository.findAllByCircularCode(circularCode);

            TreeData dto = new TreeData();
            dto.setCircularName(circularName);
            dto.setCircularCode(circularCode);
            dto.setCtgCfgStatTemplateDtos(templates);
            dto.setChecked(false);

            result.add(dto);
        }

        return result;
    }

    @Override
    public Page<CtgCfgStatTemplateDtoV3> pageTemplate(
            String keyword,
            List<String> templateGroupCodes,
            List<String> circularCodes,
            Pageable pageable
    ) {
        // Chuẩn hóa list filter: nếu null hoặc rỗng thì đặt null
        List<String> templateGroupCodesList = (templateGroupCodes != null && !templateGroupCodes.isEmpty())
                ? templateGroupCodes
                : null;

        List<String> circularCodesList = (circularCodes != null && !circularCodes.isEmpty())
                ? circularCodes
                : null;

        // Lấy tất cả template theo keyword và circularCodes
        List<CtgCfgStatTemplateDtoV3> listData =
                ctgCfgStatTemplateRepository.listTemplateByCirculars(keyword, circularCodesList);

        // Nếu có filter templateGroupCodes thì lọc luôn
        if (templateGroupCodesList != null) {
            listData = listData.stream()
                    .filter(dto -> templateGroupCodesList.contains(dto.getTemplateGroupCode()))
                    .toList();
        }

        // Trả kết quả phân trang
        return getPagedResult(listData, pageable);
    }

    private Page<CtgCfgStatTemplateDtoV3> getPagedResult(List<CtgCfgStatTemplateDtoV3> list, Pageable pageable) {
        int start = (int) pageable.getOffset();
        int end = Math.min(start + pageable.getPageSize(), list.size());
        List<CtgCfgStatTemplateDtoV3> content = list.subList(start, end);
        return new PageImpl<>(content, pageable, list.size());
    }

    @Override
    public ResponseEntity<ByteArrayResource> exportToExcel(List<String> labels,
                                                           Map<String, List<String>> frequenceMap,
                                                           String fileName) {
        List<String> circularCodes = Optional.ofNullable(frequenceMap)
                .map(map -> map.get("CIRCULAR_CODE"))
                .filter(list -> !list.isEmpty())
                .orElse(null);

        List<String> templateGroupCodes = Optional.ofNullable(frequenceMap)
                .map(map -> map.get("TEMPLATE_GROUP_CODE"))
                .filter(list -> !list.isEmpty())
                .orElse(null);



        List<ExportExcelData> filteredList = new ArrayList<>();

        List<ExportExcelData> listData =
                ctgCfgStatTemplateRepository.exportExcelData(templateGroupCodes,circularCodes);

        if (frequenceMap != null && !frequenceMap.isEmpty()) {
            filteredList = listData.stream()
                    .filter(dto -> {
                        List<String> allowedFreqs = frequenceMap.get(dto.getTemplateGroupCode());
                        return allowedFreqs == null || allowedFreqs.contains(dto.getFrequency());
                    })
                    .map(dto -> {
                        dto.setFrequency("");
                        return dto;
                    })
                    .toList();
        }
        return excelService.exportToExcelV2(filteredList, labels, ExportExcelData.class, fileName);
    }

    @Override
    @Transactional
    @Modifying
    public void deleteTemplate(String templateCode) {
        this.ctgCfgStatTemplateRepository.deleteTemplateByTemplateCode(templateCode);
        this.ctgCfgStatTemplateKpiService.deleteTemplateKpi(templateCode);
        this.ctgCfgStatTemplateSheetService.deleteTemplateSheet(templateCode);
        this.service.deleteAllByTemplateCode(templateCode);
    }

    @Override
    public CtgCfgStatTemplateDto getOneTemplate(String templateCode) {
        return this.getOne(templateCode);
    }

    @Override
    public void createTemplate(CreateFormDTO dto, MultipartFile templateFile, MultipartFile templateReportFile, MultipartFile userGuideFile) {
        this.validateData(dto.getCtgCfgStatTemplateDto());
        this.create(dto.getCtgCfgStatTemplateDto(), templateFile, templateReportFile, userGuideFile);
        this.ctgCfgStatTemplateSheetService.createTemplateSheet(dto.getCtgCfgStatTemplateSheetDTOS(), dto.getCtgCfgStatTemplateDto().getTemplateCode());
        this.ctgCfgStatTemplateKpiService.createTemplateKpi(dto.getCtgCfgStatTemplateKpiDTOS(), dto.getCtgCfgStatTemplateDto().getTemplateCode());
        this.service.createDeadLine(dto.getCtgCfgStatTemplateDeadLineDTOS(), dto.getCtgCfgStatTemplateDto().getTemplateCode());
    }

    private void create(CtgCfgStatTemplateDto dto, MultipartFile templateFile, MultipartFile templateReportFile, MultipartFile userGuideFile) {
        this.validateData(dto);
        this.checkTemplateCodeAndOrgCode(dto.getTemplateCode(), "%");
        CtgCfgStatTemplate template = this.ctgCfgStatTemplateMapper.toEntity(dto);
        FileUploadRequest fileUploadRequest = FileUploadRequest.builder()
                .templateFileName(dto.getTemplateFileName())
                .templateReportFileName(dto.getTemplateReportFileName())
                .userGuideFileName(dto.getUserGuideFileName())
                .isCreate(true)
                .build();
        this.uploadFile(template,templateFile,templateReportFile,userGuideFile,fileUploadRequest);
        template.setOrgCode(VariableConstants.ORG);
        template.setRecordStatus(VariableConstants.DD);
        this.ctgCfgStatTemplateRepository.save(template);
    }

    private void checkTemplateCodeAndOrgCode(String templateCode, String orgCode) {
        if (this.ctgCfgStatTemplateRepository.existsByTemplateCodeAndOrgCode(templateCode, orgCode))
            throw new BusinessException(StatisticalErrorCode.TEMPLATE_IS_NOT_EXISTS);
    }

    @Override
    public Page<CtgCfgStatTemplateDtoV1> getPageTemplateGroupCode(String keyword, Pageable pageable) {
        return this.ctgCfgStatTemplateRepository.pageTemplateGroupCode(keyword, pageable);
    }

    @Override
    public void updateTemplate(CreateFormDTO dto, String templateCode, MultipartFile templateFile, MultipartFile templateReportFile, MultipartFile userGuideFile) {
        this.validateData(dto.getCtgCfgStatTemplateDto());
        this.update(dto.getCtgCfgStatTemplateDto(), templateCode, templateFile, templateReportFile, userGuideFile);
        this.ctgCfgStatTemplateKpiService.updateTemplateKpi(dto.getCtgCfgStatTemplateKpiDTOS(), templateCode);
        this.ctgCfgStatTemplateSheetService.updateTemplateSheet(dto.getCtgCfgStatTemplateSheetDTOS(), templateCode);
        this.service.updateDeadLine(dto.getCtgCfgStatTemplateDeadLineDTOS(), dto.getCtgCfgStatTemplateDto().getTemplateCode());
    }

    @Override
    public CreateFormDTO getDetail(String templateCode) {
        CreateFormDTO dto = new CreateFormDTO();
        dto.setCtgCfgStatTemplateDto(this.getOneTemplate(templateCode));
        dto.setCtgCfgStatTemplateSheetDTOS(this.ctgCfgStatTemplateSheetService.getAllTemplateSheetByTemplateCode(templateCode));
        dto.setCtgCfgStatTemplateKpiDTOS(this.ctgCfgStatTemplateKpiService.getAllByTemplateCode(templateCode));
        dto.setCtgCfgStatTemplateDeadLineDTOS(this.service.getAllByTemplateCode(templateCode));
        return dto;
    }

    @Override
    public Page<ReportRuleDto> getListReportRule(
            FilterReportRuleDto filter,
            String search,
            Pageable pageable) {
        List<String> templateGroupCodes = Optional.ofNullable(filter)
                .map(FilterReportRuleDto::getTemplateGroupCodes)
                .filter(list -> !list.isEmpty())
                .orElse(null);

        List<String> circularCodes = Optional.ofNullable(filter)
                .map(FilterReportRuleDto::getCircularCodes)
                .filter(list -> !list.isEmpty())
                .orElse(null);

        List<String> defaultCircularCodes = Optional.ofNullable(filter)
                .map(FilterReportRuleDto::getDefaultCircularCodes)
                .filter(list -> !list.isEmpty())
                .orElse(null);

        String regulatoryTypeCode = Optional.ofNullable(filter)
                .map(FilterReportRuleDto::getRegulatoryTypeCode)
                .orElse(null);

        String commonCode = Optional.ofNullable(filter)
                .map(FilterReportRuleDto::getCommonCode)
                .orElse(null);

        if(templateGroupCodes == null && circularCodes == null) {
            return Page.empty(pageable);
        }

        return ctgCfgStatTemplateRepository.getListReportRule(
                regulatoryTypeCode,
                commonCode,
                templateGroupCodes,
                circularCodes,
                defaultCircularCodes,
                search,
                pageable
        );
    }

    @Override
    public List<CtgCfgStatTemplateDtoV1> findALlTemplate() {
        return this.ctgCfgStatTemplateRepository.getAllDistinct();
    }

    private void update(CtgCfgStatTemplateDto dto, String templateCode, MultipartFile templateFile, MultipartFile templateReportFile, MultipartFile userGuideFile) {
        this.validateData(dto);
        CtgCfgStatTemplate ctgCfgStatTemplate = this.ctgCfgStatTemplateRepository.findByTemplateCodeAndOrgCode(templateCode, "%");
        if (ctgCfgStatTemplate == null) throw new BusinessException(StatisticalErrorCode.TEMPLATE_NOT_FOUND);
        FileUploadRequest fileUploadRequest = FileUploadRequest.builder()
                .templateFileName(dto.getTemplateFileName())
                .templateReportFileName(dto.getTemplateReportFileName())
                .userGuideFileName(dto.getUserGuideFileName())
                .isCreate(false)
                .build();
        this.uploadFile(ctgCfgStatTemplate,templateFile,templateReportFile,userGuideFile, fileUploadRequest);

        this.ctgCfgStatTemplateMapper.updateEntityFromDto(dto, ctgCfgStatTemplate);
        this.ctgCfgStatTemplateRepository.save(ctgCfgStatTemplate);
    }

    private void uploadFile(CtgCfgStatTemplate ctgCfgStatTemplate,
                            MultipartFile templateFile,
                            MultipartFile templateReportFile,
                            MultipartFile userGuideFile,
                            FileUploadRequest request){
        try {
            if (templateFile != null && !templateFile.isEmpty()
                    && (!Objects.equals(ctgCfgStatTemplate.getTemplateFileName(), request.getTemplateFileName()) || request.isCreate())) {
                ctgCfgStatTemplate.setTemplateFile(templateFile.getBytes());
            }

            if (templateReportFile != null && !templateReportFile.isEmpty()
                    && (!Objects.equals(ctgCfgStatTemplate.getTemplateReportFileName(), request.getTemplateReportFileName()) || request.isCreate())) {
                ctgCfgStatTemplate.setTemplateReportFile(templateReportFile.getBytes());
            }

            if (userGuideFile != null && !userGuideFile.isEmpty()
                    && (!Objects.equals(ctgCfgStatTemplate.getUserGuideFileName(), request.getUserGuideFileName()) || request.isCreate())) {
                ctgCfgStatTemplate.setUserGuideFile(userGuideFile.getBytes());
            }

        } catch (Exception e){
            throw new BusinessException(StatisticalErrorCode.UPLOAD_TEMPLATE_FILE_FALL);
        }
    }


    private CtgCfgStatTemplateDto getOne(String templateCode) {
        return Optional.ofNullable(ctgCfgStatTemplateRepository.findByTemplateCode(templateCode))
                .map(ctgCfgStatTemplateMapper::toDto)
                .orElseThrow(() -> new BusinessException(StatisticalErrorCode.TEMPLATE_NOT_FOUND));
    }

    private void validateData(CtgCfgStatTemplateDto dto) {
        if (dto == null) {
            throw new BusinessException(StatisticalErrorCode.TEMPLATE_DTO_IS_EMPTY);
        }

        validateField(dto.getTemplateCode(), StatisticalErrorCode.TEMPLATE_CODE_INVALID);
        validateField(dto.getTemplateName(), StatisticalErrorCode.TEMPLATE_NAME_INVALID);
        validateField(dto.getTemplateGroupCode(), StatisticalErrorCode.TEMPLATE_GROUP_CODE_INVALID);
        validateField(dto.getFrequency(), StatisticalErrorCode.TEMPLATE_FREQUENCY_IS_EMPTY);
        validateField(dto.getRegulatoryTypeCode(), StatisticalErrorCode.REGULATORY_CODE_INVALID);
        validateField(dto.getRegulatoryTypeName(), StatisticalErrorCode.REGULATORY_NAME_INVALID);

        validateEffectiveAndExpiryDate(dto.getEffectiveDate(), dto.getExpiryDate());
    }

    private void validateField(String value, ErrorCode errorCode) {
        if (value == null || value.isBlank()) {
            throw new BusinessException(errorCode);
        }
    }

    private void validateEffectiveAndExpiryDate(Date effectiveDate, Date expiryDate) {
        if ((effectiveDate != null && expiryDate != null && effectiveDate.after(expiryDate))
                || (expiryDate != null && effectiveDate == null)) {
            throw new BusinessException(StatisticalErrorCode.TEMPLATE_TIME_INVALID);
        }
    }

    @Override
    public List<TreeData> getTreeDataV2() {
        List<Object[]> rows = ctgCfgStatTemplateRepository.findAllTemplateGroupCode();
        // Lọc bỏ những dòng có circularCode null
        Map<String, List<Object[]>> grouped = rows.stream()
                .filter(r -> r[1] != null)
                .collect(Collectors.groupingBy(r -> (String) r[1])); // group theo circularCode
        List<TreeData> result = new ArrayList<>();

        for (Map.Entry<String, List<Object[]>> entry : grouped.entrySet()) {
            String templateGroupCode = entry.getKey();
            List<Object[]> groupRows = entry.getValue();

            // Lấy circularName từ dòng đầu tiên
            String templateGroupName = (String) groupRows.get(0)[0];
            // Tìm danh sách template theo circularCode
            List<CtgCfgStatTemplateDtoV2> templates =
                    ctgCfgStatTemplateRepository.findAllByTemplateGroupCode(templateGroupCode);

            TreeData dto = new TreeData();
            dto.setTemplateGroupName(templateGroupName);
            dto.setTemplateGroupCode(templateGroupCode);
            dto.setCtgCfgStatTemplateDtos(templates);
            dto.setChecked(false);

            result.add(dto);
        }

        return result;
    }
}
