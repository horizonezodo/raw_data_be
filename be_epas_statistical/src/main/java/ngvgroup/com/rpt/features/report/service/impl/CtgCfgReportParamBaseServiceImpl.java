package ngvgroup.com.rpt.features.report.service.impl;

import com.ngvgroup.bpm.core.common.exception.BusinessException;
import com.ngvgroup.bpm.core.common.exception.ErrorCode;
import lombok.extern.slf4j.Slf4j;
import ngvgroup.com.rpt.features.excel.service.ExcelService;
import ngvgroup.com.rpt.features.report.dto.ComInfOrganizationDto;
import ngvgroup.com.rpt.features.report.dto.ExportExcelRequest;
import ngvgroup.com.rpt.features.report.dto.SearchFilterRequest;
import ngvgroup.com.rpt.features.report.dto.ctgcfgreportparambase.CtgCfgReportParamBaseDto;
import ngvgroup.com.rpt.features.report.dto.ctgcfgreportparambase.CtgCfgReportParamBaseResponse;
import ngvgroup.com.rpt.features.report.helper.PageUtils;
import ngvgroup.com.rpt.features.report.mapper.CtgCfgReportParamBaseMapper;
import ngvgroup.com.rpt.features.report.model.CtgCfgReportParamBase;
import ngvgroup.com.rpt.features.report.repository.CtgCfgReportParamBaseRepository;
import ngvgroup.com.rpt.features.report.service.ComInfOrganizationService;
import ngvgroup.com.rpt.features.report.service.CtgCfgReportParamBaseService;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@Slf4j
public class CtgCfgReportParamBaseServiceImpl implements CtgCfgReportParamBaseService {

    private final CtgCfgReportParamBaseMapper ctgCfgReportParamBaseMapper;
    private final CtgCfgReportParamBaseRepository ctgCfgReportParamBaseRepository;
    private final ComInfOrganizationService comInfOrganizationService;
    private final ExcelService excelService;

    public CtgCfgReportParamBaseServiceImpl(CtgCfgReportParamBaseMapper ctgCfgReportParamBaseMapper,
            CtgCfgReportParamBaseRepository ctgCfgReportParamBaseRepository,
            ComInfOrganizationService comInfOrganizationService, ExcelService excelService) {
        this.ctgCfgReportParamBaseMapper = ctgCfgReportParamBaseMapper;
        this.ctgCfgReportParamBaseRepository = ctgCfgReportParamBaseRepository;
        this.comInfOrganizationService = comInfOrganizationService;
        this.excelService = excelService;
    }

    @Override
    public void createCfgReportParamBase(CtgCfgReportParamBaseDto request) {
        CtgCfgReportParamBase ctgCfgReportParamBase = ctgCfgReportParamBaseMapper.toEntity(request);
        ctgCfgReportParamBase.setRecordStatus("approval");
        ctgCfgReportParamBaseRepository.save(ctgCfgReportParamBase);
    }

    @Override
    public void updateCfgReportParamBase(Long id, CtgCfgReportParamBaseDto request) {
        CtgCfgReportParamBase ctgCfgReportParamBase = ctgCfgReportParamBaseRepository.findById(id)
                .orElseThrow(() -> new BusinessException(ErrorCode.NOT_FOUND, id));
        ctgCfgReportParamBaseMapper.updateComCfgReportParamBase(request, ctgCfgReportParamBase);
        ctgCfgReportParamBaseRepository.save(ctgCfgReportParamBase);
    }

    @Override
    public void deleteCfgReportParamBase(Long id) {
        CtgCfgReportParamBase ctgCfgReportParamBase = ctgCfgReportParamBaseRepository.findById(id)
                .orElseThrow(() -> new BusinessException(ErrorCode.NOT_FOUND, id));
        ctgCfgReportParamBase.setIsDelete(1);
        ctgCfgReportParamBaseRepository.save(ctgCfgReportParamBase);
    }

    @Override
    public CtgCfgReportParamBaseDto getDetailCfgReportParamBase(Long id) {
        return ctgCfgReportParamBaseRepository.findById(id)
                .map(ctgCfgReportParamBaseMapper::toDto)
                .orElseThrow(() -> new BusinessException(ErrorCode.NOT_FOUND, id));
    }

    @Override
    public List<CtgCfgReportParamBaseResponse> searchCfgReportParamBasev2(SearchFilterRequest searchFilterRequest) {
        String filter = searchFilterRequest.getFilter() != null
                ? searchFilterRequest.getFilter().toLowerCase()
                : null;
        Pageable pageable= PageUtils.toPageable(searchFilterRequest.getPageable());
        return this.listCfgReportParamBase(filter, pageable);
    }

    private List<CtgCfgReportParamBaseResponse> listCfgReportParamBase(String filter, Pageable pageable) {
        List<CtgCfgReportParamBaseResponse> lstReport = ctgCfgReportParamBaseRepository
                .listComCfgReportParamBase(filter, pageable);
        List<ComInfOrganizationDto> lstOrg = this.comInfOrganizationService.searchOrganizations(filter);
        Map<String, String> orgCodeToNameMap = lstOrg.stream()
                .collect(Collectors.toMap(ComInfOrganizationDto::getOrgCode, ComInfOrganizationDto::getOrgName));

        return lstReport.stream()
                .map(item -> {
                    String orgName;
                    if ("%".equals(item.getOrgCode())) {
                        orgName = "Tất cả";
                    } else {
                        orgName = orgCodeToNameMap.getOrDefault(item.getOrgCode(), null);
                    }
                    item.setOrgName(orgName); // gán orgName vào DTO
                    return item;
                })
                .toList();
    }

    @Override
    public List<CtgCfgReportParamBaseResponse> getAllCfgReportParamBase() {
        return this.listCfgReportParamBase(null, null);
    }

    @Override
    public ResponseEntity<byte[]> exportExcel(String fileName, ExportExcelRequest request) {
        String filter = request.getFilter() != null
                ? request.getFilter().toLowerCase()
                : null;
        List<CtgCfgReportParamBaseResponse> comCfgReportParamBaseDtos = this.listCfgReportParamBase(filter, null);
        fileName = fileName == null || fileName.trim().isEmpty() ? "ResultExport" : fileName;
        byte[] response = excelService.exportToExcelContent(comCfgReportParamBaseDtos, request.getLabels(),
                CtgCfgReportParamBaseDto.class);
        try {
            return ResponseEntity.ok()
                    .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + fileName + ".xlsx\"")
                    .contentType(MediaType.APPLICATION_OCTET_STREAM)
                    .body(response);
        } catch (Exception e) {
            log.error("Error: Lỗi tạo file Excel {}", e.getMessage());
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @Override
    public boolean isExistedByParamBaseCode(String paramBaseCode) {
        return ctgCfgReportParamBaseRepository.existsByParamBaseCodeAndIsActive(paramBaseCode, 1);
    }

}
