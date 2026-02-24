package ngvgroup.com.rpt.features.report.service.impl;

import com.ngvgroup.bpm.core.common.exception.BusinessException;
import com.ngvgroup.bpm.core.common.exception.ErrorCode;
import com.ngvgroup.bpm.core.persistence.service.storedprocedure.BaseStoredProcedureService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import ngvgroup.com.rpt.features.common.dto.CommonDto;
import ngvgroup.com.rpt.features.common.service.ComCfgCommonService;
import ngvgroup.com.rpt.features.excel.service.ExcelService;
import ngvgroup.com.rpt.features.report.dto.ctgcfgreport.*;
import ngvgroup.com.rpt.features.report.dto.ctgcfgreportgroup.CtgCfgReportGroupDto;
import ngvgroup.com.rpt.features.report.dto.ctgcfgreportparam.ReportParamDto;
import ngvgroup.com.rpt.features.report.mapper.CtgCfgReportMapper;
import ngvgroup.com.rpt.features.report.model.CtgCfgReport;
import ngvgroup.com.rpt.features.report.repository.CtgCfgReportRepository;
import ngvgroup.com.rpt.features.report.service.CtgCfgReportService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.math.BigInteger;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Slf4j
@Service
@RequiredArgsConstructor
public class CtgCfgReportServiceImpl extends BaseStoredProcedureService implements CtgCfgReportService {

    @Value("${spring.datasource.username}")
    private String schemaName;

    private final CtgCfgReportRepository ctgCfgReportRepository;
    private final CtgCfgReportMapper mapper;
    private final ExcelService excelService;
    private final ComCfgCommonService ctgComCommonService;


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
        List<CommonDto> commonList = this.ctgComCommonService.getAllCommon();
        Map<String, String> commonMap = commonList.stream()
                .collect(Collectors.toMap(CommonDto::getCommonCode, CommonDto::getCommonName));

        List<ReportTypeDTO> dtos = ctgCfgReportRepository.listGroupNames();

        Map<String, List<ReportTypeDTO>> grouped = dtos.stream()
                .collect(Collectors.groupingBy(ReportTypeDTO::getCommonCode));

        return grouped.entrySet().stream()
                .map(entry -> {
                    String commonCode = entry.getKey();
                    List<ReportTypeDTO> list = entry.getValue();

                    String commonName = commonMap.getOrDefault(commonCode, "Không xác định");

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
                            .toList();

                    // Build menu
                    ReportMenu menu = new ReportMenu();
                    menu.setCommonCode(commonCode);
                    menu.setCommonName(commonName);
                    menu.setReportGroup(groupList);
                    return menu;
                })
                .toList();
    }

    @Override
    public Page<ReportDtoV1> searchListReport(String reportType, String groupCode, String keyword, Pageable pageable) {
        return ctgCfgReportRepository.searchAllReportByGroup(reportType, groupCode, keyword,
                pageable);
    }

    @Override
    public Page<ReportDtoV1> searchListReportByRequest(
            List<ReportSearchRequest> requests, String keyword,
            Pageable pageable) {
        if (requests == null || requests.isEmpty()) {
            return ctgCfgReportRepository.searchAllReportByGroup(null, null, keyword, pageable);
        }

        List<String> reportTypes = requests.stream()
                .map(ReportSearchRequest::getReportType)
                .distinct()
                .toList();

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
                .toList();
        return ctgCfgReportRepository.searchAllReportByGroupIn(reportTypes, groupCodes, keyword, pageable);
    }

    @Transactional
    @Override
    public List<ReportMiningMenu> getMiningMenu() {
        String userId = getCurrentUserId();
        List<ReportDto> reportDtos = ctgCfgReportRepository.findAllReport(userId);
        Map<String, List<ReportDto>> groupedReports = reportDtos.stream()
                .collect(Collectors.groupingBy(ReportDto::getReportGroupName));
        return groupedReports.entrySet().stream()
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
                            }).toList();

                    ReportMiningMenu menu = new ReportMiningMenu();
                    menu.setReportGroupName(groupCodeName);
                    menu.setReports(reportList);
                    return menu;
                })
                .sorted(Comparator.comparing(menu -> {
                    List<ReportDto> reports = menu.getReports();
                    if (reports != null && !reports.isEmpty()) {
                        return reports.get(0).getGroupSortNumber();
                    }
                    return BigInteger.ZERO;
                }, Comparator.nullsLast(Comparator.naturalOrder())))
                .toList();
    }


    @Override
    public CtgCfgReportDTO getReport(String reportCode) {
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
    public boolean checkExist(String code) {
        Optional<CtgCfgReport> cfgReport = ctgCfgReportRepository.findByReportCode(code);

        return cfgReport.isPresent();
    }

}