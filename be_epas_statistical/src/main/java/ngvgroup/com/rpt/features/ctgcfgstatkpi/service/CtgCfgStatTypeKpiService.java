package ngvgroup.com.rpt.features.ctgcfgstatkpi.service;

import ngvgroup.com.rpt.features.ctgcfgstatkpi.dto.CtgCfgStatTypeKpiDto;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;

import java.util.List;

public interface CtgCfgStatTypeKpiService {
    List<CtgCfgStatTypeKpiDto> getAll();
    Page<CtgCfgStatTypeKpiDto> pageTypeKpi(String keyword, Pageable pageable);
    void createTypeKpi(CtgCfgStatTypeKpiDto dto);
    void updateTypeKpi(CtgCfgStatTypeKpiDto dto, String kpiTypeCode);
    void deleteTypeKpi(String kpiTypeCode);
    CtgCfgStatTypeKpiDto getOne(String kpiTypeCode);
    ResponseEntity<ByteArrayResource> exportToExcel(List<String> labels, String keyword, String fileName);
    Boolean checkExistStatScoreGroupCode(String kpiTypeCode);
}
