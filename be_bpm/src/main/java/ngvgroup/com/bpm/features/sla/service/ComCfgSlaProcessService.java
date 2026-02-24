package ngvgroup.com.bpm.features.sla.service;

import ngvgroup.com.bpm.features.common.dto.CommonDto;
import ngvgroup.com.bpm.features.sla.dto.ComCfgSlaProcessDto;
import ngvgroup.com.bpm.features.sla.dto.ComCfgSlaProcessDtlDto;
import ngvgroup.com.bpm.features.sla.dto.ComCfgSlaDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;

import java.util.List;

public interface ComCfgSlaProcessService {

    Page<ComCfgSlaDto.ComCfgSlaView> getListSla(Pageable pageable);

    Page<ComCfgSlaDto.ComCfgSlaView> findSlaByKeyword(String keyword, Pageable pageable);

    ResponseEntity<byte[]> exportToExcel(String keyword, String fileName);

    List<CommonDto> getUnit();

    List<CommonDto> getPriorityLevel();

    void create(ComCfgSlaDto comCfgSlaDto) ;

    void updateProcess(ComCfgSlaDto comCfgSlaDto) ;

    void deleteProcessSla(ComCfgSlaDto comCfgSlaDto) ;

    ComCfgSlaDto getInfoProcess(String orgCode, String processTypeCode);

    void updateSlaWarningPercent(String processDefineCode, String orgCode);

    void createProcess(ComCfgSlaProcessDto comCfgSlaProcessDto) ;

    void createProcessDtl(ComCfgSlaProcessDtlDto comCfgSlaProcessDtlDto) ;
}
