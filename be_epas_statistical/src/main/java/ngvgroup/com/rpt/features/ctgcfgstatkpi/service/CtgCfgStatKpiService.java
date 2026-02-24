package ngvgroup.com.rpt.features.ctgcfgstatkpi.service;

import ngvgroup.com.rpt.features.ctgcfgstatkpi.dto.CtgCfgStatKpiDto;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;


import java.util.List;

public interface CtgCfgStatKpiService {

    Page<CtgCfgStatKpiDto> searchAllStatKpi( String keyword, List<String> kpiTypeCode, Pageable pageable);

    ResponseEntity<ByteArrayResource> exportToExcel(List<String> labels, List<String> kpiTypeCode, String fileName);

    void create(CtgCfgStatKpiDto ctgCfgStatKpiDto);

    void update(CtgCfgStatKpiDto ctgCfgStatKpiDto);

    void delete(Long id);

    CtgCfgStatKpiDto getDetail(Long id);

    boolean checkExist(String kpiCode);

    Page<CtgCfgStatKpiDto> getAllKpiData(String keyword, Pageable pageable);

    CtgCfgStatKpiDto getByKpiCode(String kpiCode);
}
