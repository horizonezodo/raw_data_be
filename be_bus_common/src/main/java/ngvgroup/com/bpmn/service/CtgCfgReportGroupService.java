package ngvgroup.com.bpmn.service;

import ngvgroup.com.bpmn.dto.CtgCfgReportGroup.CtgCfgReportGroupDto;
import ngvgroup.com.bpmn.dto.CtgCfgReportGroupDTO.CtgCfgReportGroupDTO;
import ngvgroup.com.bpmn.dto.PageDTO.PageableDTO;
import ngvgroup.com.bpmn.dto.request.SearchFilterRequest;

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
