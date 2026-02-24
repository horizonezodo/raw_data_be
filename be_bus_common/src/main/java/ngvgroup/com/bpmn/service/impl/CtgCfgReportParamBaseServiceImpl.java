package ngvgroup.com.bpmn.service.impl;

import ngvgroup.com.bpmn.dto.ComInfOrganization.ComInfOrganizationDto;
import ngvgroup.com.bpmn.dto.CtgCfgReportParamBase.CtgCfgReportParamBaseDto;
import ngvgroup.com.bpmn.dto.CtgCfgReportParamBase.CtgCfgReportParamBaseResponse;
import ngvgroup.com.bpmn.dto.request.ExportExcelRequest;
import ngvgroup.com.bpmn.dto.request.SearchFilterRequest;
import ngvgroup.com.bpmn.dto.response.PageResponse;
import ngvgroup.com.bpmn.mapper.CtgCfgReportParamBase.CtgCfgReportParamBaseMapper;
import ngvgroup.com.bpmn.model.CtgCfgReportParamBase;
import ngvgroup.com.bpmn.repository.CtgCfgReportParamBaseRepository;
import ngvgroup.com.bpmn.service.ComInfOrganizationService;
import ngvgroup.com.bpmn.service.CtgCfgReportParamBaseService;
import ngvgroup.com.bpmn.utils.PageUtils;
import com.ngvgroup.bpm.core.exception.BusinessException;
import com.ngvgroup.bpm.core.exception.ErrorCode;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.Collections;
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
        ctgCfgReportParamBase.setIsActive(1);
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
        ctgCfgReportParamBase.setIsActive(0);
        ctgCfgReportParamBase.setIsDelete(1);
        ctgCfgReportParamBaseRepository.save(ctgCfgReportParamBase);
    }

    @Override
    public CtgCfgReportParamBaseDto getDetailCfgReportParamBase(Long id) {
        return ctgCfgReportParamBaseRepository.findById(id)
                .map(ctgCfgReportParamBaseMapper::toDto)
                .orElseThrow(() -> new BusinessException(ErrorCode.NOT_FOUND, id));
    }

    // @Override
    // public PageResponse<CtgCfgReportParamBaseResponse>
    // searchCfgReportParamBase(SearchFilterRequest request) {
    // Pageable pageable = PageUtils.toPageable(request.getPageable());
    // String filter = request.getFilter() != null
    // ? request.getFilter().toLowerCase()
    // : null;
    // Page<CtgCfgReportParamBaseResponse> res =
    // ctgCfgReportParamBaseRepository.searchComCfgReportParamBase(filter,
    // pageable);
    // return new PageResponse<>(res);
    //
    // }

    @Override
    public List<CtgCfgReportParamBaseResponse> searchCfgReportParamBasev2(SearchFilterRequest searchFilterRequest) {
        String filter = searchFilterRequest.getFilter() != null
                ? searchFilterRequest.getFilter().toLowerCase()
                : null;
        return this.listCfgReportParamBase(filter);
    }

    private List<CtgCfgReportParamBaseResponse> listCfgReportParamBase(String filter) {
        List<CtgCfgReportParamBaseResponse> lstReport = ctgCfgReportParamBaseRepository
                .listComCfgReportParamBase(filter);
        List<ComInfOrganizationDto> lstOrg = this.comInfOrganizationService.searchOrganizations(filter);
        Map<String, String> orgCodeToNameMap = lstOrg.stream()
                .collect(Collectors.toMap(ComInfOrganizationDto::getOrgCode, ComInfOrganizationDto::getOrgName));

        // Map report param với orgName tương ứng
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
                .collect(Collectors.toList());
    }

    @Override
    public List<CtgCfgReportParamBaseResponse> getAllCfgReportParamBase() {
        // return ctgCfgReportParamBaseRepository.searchComCfgReportParamBase(null);
        return this.listCfgReportParamBase(null);
    }

    @Override
    public ResponseEntity<byte[]> exportExcel(String fileName, ExportExcelRequest request) {
        String filter = request.getFilter() != null
                ? request.getFilter().toLowerCase()
                : null;
        // List<CtgCfgReportParamBaseResponse> comCfgReportParamBaseDtos =
        // ctgCfgReportParamBaseRepository
        // .searchComCfgReportParamBase(filter);
        List<CtgCfgReportParamBaseResponse> comCfgReportParamBaseDtos = this.listCfgReportParamBase(filter);
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
