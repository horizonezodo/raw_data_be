package ngvgroup.com.bpmn.service.impl;

import ngvgroup.com.bpmn.dto.ComCommon.CommonDto;
import ngvgroup.com.bpmn.dto.CtgCfgReport.CtgCfgReportDTO;
import ngvgroup.com.bpmn.dto.CtgCfgReport.ReportDto;
import ngvgroup.com.bpmn.dto.CtgCfgReport.ReportExcelDTO;
import ngvgroup.com.bpmn.dto.CtgCfgReport.ReportJasperDTO;
import ngvgroup.com.bpmn.dto.CtgCfgReport.ReportMenu;
import ngvgroup.com.bpmn.dto.CtgCfgReport.ReportMiningMenu;
import ngvgroup.com.bpmn.dto.CtgCfgReport.ReportSearchRequest;
import ngvgroup.com.bpmn.dto.CtgCfgReport.ReportTypeDTO;
import ngvgroup.com.bpmn.dto.CtgCfgReportGroup.CtgCfgReportGroupDto;
import ngvgroup.com.bpmn.dto.CtgCfgReportParam.ReportParamDto;
import ngvgroup.com.bpmn.dto.CtgCfgResourceMapping.ResourceMappingDto;
import ngvgroup.com.bpmn.mapper.CtgCfgReport.CtgCfgReportMapper;
import ngvgroup.com.bpmn.model.CtgCfgReport;
import ngvgroup.com.bpmn.model.CtgCfgReportGroup;
import ngvgroup.com.bpmn.repository.CtgCfgReportRepository;
import ngvgroup.com.bpmn.service.CtgCfgReportService;
import ngvgroup.com.bpmn.service.CtgComCommonService;
import ngvgroup.com.bpmn.service.CtgCfgResourceMappingService;

import com.ngvgroup.bpm.core.exception.BusinessException;
import com.ngvgroup.bpm.core.exception.ErrorCode;
import com.ngvgroup.bpm.core.service.BaseService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.sf.jasperreports.engine.*;
import net.sf.jasperreports.engine.data.JRMapCollectionDataSource;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.math.BigInteger;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import java.math.BigDecimal;

@Slf4j
@RequiredArgsConstructor
@Service
public class CtgCfgReportServiceImpl extends BaseService implements CtgCfgReportService {

    @Value("${spring.datasource.username}")
    private String schemaName;

    private final CtgCfgReportRepository ctgCfgReportRepository;
    private final CtgCfgReportMapper mapper;
    private final ExcelService excelService;
    private final CtgComCommonService ctgComCommonService;
    private final CtgCfgResourceMappingService ctgCfgResourceMappingService;
    // private final ComCfgTemplateService comCfgTemplateService;
    // private final StoredProcedureExecutor storedProcedureExecutor;
    // private final CtgComCommonFeignClient client;

    @Override
    public CtgCfgReportDTO create(CtgCfgReportDTO report) {
        Optional<CtgCfgReport> reportOptional = ctgCfgReportRepository.findByReportCode(report.getReportCode());
        if (reportOptional.isPresent()) {
            throw new BusinessException(ErrorCode.CONFLICT, report.getReportCode());
        }
        CtgCfgReport ctgCfgReport = mapper.toEntity(report);
        Timestamp current = Timestamp.valueOf(LocalDateTime.now());
        ctgCfgReport.setRecordStatus("approval");
        ctgCfgReport.setCreatedDate(current);
        ctgCfgReport.setCreatedBy(getCurrentUserName());
        ctgCfgReport.setApprovedDate(current);
        ctgCfgReport.setApprovedBy(getCurrentUserName());
        ctgCfgReport.setIsDelete(0);
        ctgCfgReport.setIsActive(1);

        return mapper.toDto(ctgCfgReportRepository.save(ctgCfgReport));
    }

    @Override
    @Transactional
    public CtgCfgReportDTO update(CtgCfgReportDTO report) {
        Optional<CtgCfgReport> reportOptional = ctgCfgReportRepository.findByReportCode(report.getReportCode());
        if (reportOptional.isEmpty()) {
            throw new BusinessException(ErrorCode.NOT_FOUND, report.getReportCode());
        }
        CtgCfgReport ctgCfgReport = mapper.toEntity(report);
        ctgCfgReport.setId(reportOptional.get().getId());
        ctgCfgReport.setCreatedBy(reportOptional.get().getCreatedBy());
        ctgCfgReport.setCreatedDate(reportOptional.get().getCreatedDate());
        ctgCfgReport.setModifiedDate(Timestamp.valueOf(LocalDateTime.now()));
        ctgCfgReport.setModifiedBy(getCurrentUserName());
        ctgCfgReport.setApprovedDate(reportOptional.get().getApprovedDate());
        ctgCfgReport.setApprovedBy(reportOptional.get().getApprovedBy());
        ctgCfgReport.setModifiedBy(getCurrentUserName());
        ctgCfgReport.setIsDelete(reportOptional.get().getIsDelete());
        ctgCfgReport.setIsActive(reportOptional.get().getIsActive());
        ctgCfgReport.setRecordStatus(reportOptional.get().getRecordStatus());

        return mapper.toDto(ctgCfgReportRepository.save(ctgCfgReport));
    }

    @Override
    public List<CtgCfgReport> findAll() {
        return ctgCfgReportRepository.findAll();
    }

    @Override
    public CtgCfgReportDTO findById(Long id) {
        CtgCfgReport ctgCfgReport = ctgCfgReportRepository.findById(id).orElse(null);
        if (ctgCfgReport == null) {
            throw new BusinessException(ErrorCode.NOT_FOUND, id);
        }
        return mapper.toDto(ctgCfgReport);
    }

    @Transactional
    @Override
    public void deleteByReportCode(String reportCode) {
        ctgCfgReportRepository.deleteComCfgReportByReportCode(reportCode);
    }

    @Transactional
    @Override
    public List<ReportMenu> getMenu() {
        // Lấy toàn bộ CommonCode và CommonName từ API
        List<CommonDto> commonList = this.ctgComCommonService.getAllCommon();
        Map<String, String> commonMap = commonList.stream()
                .collect(Collectors.toMap(CommonDto::getCommonCode, CommonDto::getCommonName));

        // Lấy toàn bộ report type + group name
        List<ReportTypeDTO> dtos = ctgCfgReportRepository.listGroupNames();

        // Gom nhóm theo commonCode
        Map<String, List<ReportTypeDTO>> grouped = dtos.stream()
                .collect(Collectors.groupingBy(ReportTypeDTO::getCommonCode));

        return grouped.entrySet().stream()
                .map(entry -> {
                    String commonCode = entry.getKey();
                    List<ReportTypeDTO> list = entry.getValue();

                    // Lấy commonName từ map (nếu có)
                    String commonName = commonMap.getOrDefault(commonCode, "Không xác định");

                    // Chuyển thành DTO nhóm và sắp xếp theo sortNumber
                    List<CtgCfgReportGroupDto> groupList = list.stream()
                            .sorted(Comparator.comparing(ReportTypeDTO::getSortNumber,
                                    Comparator.nullsLast(Comparator.naturalOrder())))
                            .map(dto -> {
                                CtgCfgReportGroupDto g = new CtgCfgReportGroupDto();
                                g.setReportGroupCode(dto.getReportGroupCode());
                                g.setReportGroupName(dto.getReportGroupName());
                                g.setDescription(dto.getDescription());
                                g.setSortNumber(dto.getSortNumber());
                                return g;
                            })
                            .collect(Collectors.toList());

                    // Build menu
                    ReportMenu menu = new ReportMenu();
                    menu.setCommonCode(commonCode);
                    menu.setCommonName(commonName);
                    menu.setReportGroup(groupList);
                    return menu;
                })
                .collect(Collectors.toList());
    }

    // private List<CtgComCommonDTO> getAllCommon() {
    // var response = client.getAllCommon();

    // if (response == null || response.getBody() == null ||
    // response.getBody().getData() == null) {
    // return Collections.emptyList();
    // }
    // return response.getBody().getData();
    // }

    @Override
    public Page<ReportDto> searchListReport(String reportType, String groupCode, String keyword, Pageable pageable) {
        // Repository method already sorts by rg.sortNumber ASC, r.sortNumber ASC
        Page<ReportDto> reportDtos = ctgCfgReportRepository.searchAllReportByGroup(reportType, groupCode, keyword,
                pageable);
        return reportDtos;
    }

    @Override
    public Page<ReportDto> searchListReportByRequest(
            List<ReportSearchRequest> requests, String keyword,
            Pageable pageable) {
        if (requests == null || requests.isEmpty()) {
            // Nếu không có requests, search tất cả theo keyword
            // Repository method already sorts by rg.sortNumber ASC, r.sortNumber ASC
            return ctgCfgReportRepository.searchAllReportByGroup(null, null, keyword, pageable);
        }

        // Tách riêng reportTypes và groupCodes từ requests
        List<String> reportTypes = requests.stream()
                .map(ReportSearchRequest::getReportType)
                .distinct()
                .collect(Collectors.toList());

        List<String> groupCodes = requests.stream()
                .flatMap(r -> {
                    if (r.getListGroupCode() == null || r.getListGroupCode().isEmpty()) {
                        return Stream.of((String) null);
                    } else {
                        return r.getListGroupCode().stream();
                    }
                })
                .filter(Objects::nonNull)
                .distinct()
                .collect(Collectors.toList());

        // Gọi repository method mới với 2 list riêng biệt
        // Repository method already sorts by rg.sortNumber ASC, r.sortNumber ASC
        return ctgCfgReportRepository.searchAllReportByGroupIn(reportTypes, groupCodes, keyword, pageable);
    }

    @Transactional
    @Override
    public List<ReportMiningMenu> getMiningMenu() {
        String userId = getCurrentUserId();
        List<ReportDto> reportDtos = ctgCfgReportRepository.findAllReport(userId);

        // Group by report group name and maintain sorting
        Map<String, List<ReportDto>> groupedReports = reportDtos.stream()
                .collect(Collectors.groupingBy(ReportDto::getReportGroupName));

        // Create ReportMiningMenu list with proper sorting
        List<ReportMiningMenu> result = groupedReports.entrySet().stream()
                .map(entry -> {
                    String groupCodeName = entry.getKey();
                    List<ReportDto> reportList = entry.getValue().stream()
                            .sorted(Comparator.comparing(ReportDto::getSortNumber,
                                    Comparator.nullsLast(Comparator.naturalOrder())))
                            .map(dto -> {
                                ReportDto reportDto = new ReportDto();
                                reportDto.setId(dto.getId());
                                reportDto.setReportCode(dto.getReportCode());
                                reportDto.setReportGroupCode(dto.getReportGroupCode());
                                reportDto.setReportCodeName(dto.getReportCodeName());
                                reportDto.setDataSourceType(dto.getDataSourceType());
                                reportDto.setReportSource(dto.getReportSource());
                                reportDto.setSortNumber(dto.getSortNumber());
                                reportDto.setGroupSortNumber(dto.getGroupSortNumber());
                                return reportDto;
                            })
                            .collect(Collectors.toList());

                    ReportMiningMenu menu = new ReportMiningMenu();
                    menu.setReportGroupName(groupCodeName);
                    menu.setReports(reportList);
                    return menu;
                })
                .sorted(Comparator.comparing(menu -> {
                    // Sort by the first report's group sort number in each menu
                    List<ReportDto> reports = menu.getReports();
                    if (reports != null && !reports.isEmpty()) {
                        return reports.get(0).getGroupSortNumber();
                    }
                    return BigInteger.ZERO;
                }, Comparator.nullsLast(Comparator.naturalOrder())))
                .collect(Collectors.toList());

        return result;
    }



    @Override
    public CtgCfgReportDTO getReport(String reportCode) {
        // String userId = getCurrentUserId();
        // // Lấy danh sách mapping resource của user hiện tại
        // List<ResourceMappingDto> mappings =
        // ctgCfgResourceMappingService.getListCurrentBranch().getData();
        // boolean isMapped = mappings != null && mappings.stream().anyMatch(m ->
        // reportCode.equals(m.getResourceCode()));
        // if (!isMapped) {
        // throw new BusinessException(ErrorCode.UNAUTHORIZED, "User is not authorized
        // to access this report");
        // }
        CtgCfgReport ctgCfgReport = ctgCfgReportRepository.findByReportCode(reportCode).orElse(null);
        if (ctgCfgReport == null) {
            throw new BusinessException(ErrorCode.NOT_FOUND, reportCode);
        }

        return mapper.toDto(ctgCfgReport);
    }

    @Override
    public byte[] generateExcelFile(ReportExcelDTO req, String fileName) {
        List<ReportDto> reportDtos = ctgCfgReportRepository.searchToExportExcel(
                req.getReportGroupCode());

        return excelService.exportToExcelContent(
                reportDtos, req.getLabel(), ReportDto.class);
    }

    // @Override
    // public byte[] generateReport(ReportJasperDTO reportJasperDTO) throws
    // Exception {
    // CtgCfgReportDTO ctgCfgReportDTO = getReport(reportJasperDTO.getReportCode());
    // if (ctgCfgReportDTO == null) {
    // throw new IllegalArgumentException(
    // "Không tìm thấy cấu hình báo cáo với mã: " +
    // reportJasperDTO.getReportCode());
    // }

    // List<Map<String, Object>> data =
    // storedProcedureExecutor.executeStoredProcedureWithCursor(
    // ctgCfgReportDTO.getReportSource(),
    // schemaName,
    // reportJasperDTO.getParams(),
    // ctgCfgReportDTO.getReportCodeName());

    // if (data == null || data.isEmpty()) {
    // log.warn("Không có dữ liệu trả về từ stored procedure: {}",
    // ctgCfgReportDTO.getReportSource());
    // data = new ArrayList<>();
    // }

    // Collection<Map<String, ?>> castData = new ArrayList<>();
    // for (Map<String, Object> map : data) {
    // castData.add(map);
    // }
    // JRDataSource dataSource = new JRMapCollectionDataSource(castData);

    // Map<String, Object> params = new HashMap<>(reportJasperDTO.getParams());
    // params.put("reportName", reportJasperDTO.getParams().get("reportName"));

    // try {
    // byte[] template =
    // comCfgTemplateService.downloadFileTemplate(ctgCfgReportDTO.getTemplateCode());
    // if (template == null || template.length == 0) {
    // throw new IllegalArgumentException(
    // "Không thể tải template báo cáo: " + ctgCfgReportDTO.getTemplateCode());
    // }

    // InputStream jrxmlStream = new ByteArrayInputStream(template);

    // JasperReport jasperReport = JasperCompileManager.compileReport(jrxmlStream);

    // JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport, params,
    // dataSource);

    // JasperUtils jasperUtils = new JasperUtils();
    // switch (reportJasperDTO.getFormat().toLowerCase()) {
    // case "pdf":
    // return JasperExportManager.exportReportToPdf(jasperPrint);
    // case "xlsx":
    // return jasperUtils.exportToExcel(jasperPrint);
    // case "docx":
    // return jasperUtils.exportToWord(jasperPrint);
    // default:
    // throw new IllegalArgumentException("Định dạng không được hỗ trợ: " +
    // reportJasperDTO.getFormat());
    // }
    // } catch (JRException e) {
    // log.error("Lỗi khi tạo báo cáo: ", e);
    // throw new Exception("Không thể tạo báo cáo. Chi tiết: " + e.getMessage());
    // } catch (Exception e) {
    // log.error("Lỗi không mong đợi: ", e);
    // throw new Exception("Lỗi khi tạo báo cáo: " + e.getMessage());
    // }
    // }

    @Override
    public CtgCfgReportDTO exportUrlReport(ReportParamDto reportParamDto) {
        return getReport(reportParamDto.getReportCode());
    }

    @Override
    public void deleteByIds(List<Long> ids) {
        ctgCfgReportRepository.deleteAllById(ids);
    }

    @Override
    public Page<ReportDto> getListReportSetting(String reportType, Pageable pageable) {
        return ctgCfgReportRepository.findByReportType(reportType, pageable);
    }

    @Override
    public boolean checkExist(String code){
        Optional<CtgCfgReport> cfgReport= ctgCfgReportRepository.findByReportCode(code);
        if(!cfgReport.isPresent()){
            return false;
        }
        return true;
    }

}