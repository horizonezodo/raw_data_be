package ngvgroup.com.rpt.features.report.service;

import ngvgroup.com.rpt.features.report.dto.ctgcfgreportgroup.CtgCfgReportGroupDto;
import ngvgroup.com.rpt.features.report.dto.ctgcfgreportgroupdto.CtgCfgReportGroupDTO;
import ngvgroup.com.rpt.features.report.dto.SearchFilterRequest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;

import java.util.List;

public interface CtgCfgReportGroupService {

    Page<CtgCfgReportGroupDto> getListReportGroups(Pageable pageable);

    Page<CtgCfgReportGroupDto> findListReportGroups(SearchFilterRequest searchFilterRequest);

    ResponseEntity<byte[]> exportToExcel(CtgCfgReportGroupDto ctgCfgReportGroupDto, String fileName);

    void createReportGroup(CtgCfgReportGroupDto ctgCfgReportGroupDto) ;

    void updateReportGroup(CtgCfgReportGroupDto ctgCfgReportGroupDto) ;

    CtgCfgReportGroupDto getInfoReportGroup(String reportGroupCode);

    void deleteReportGroup(String reportGroupCode) ;


    List<CtgCfgReportGroupDTO> getAll();

    List<CtgCfgReportGroupDto> getListReportGroups();

    boolean checkExist(String code);
}
