package ngvgroup.com.rpt.features.ctgcfgstattemplate.service;

import ngvgroup.com.rpt.features.ctgcfgstattemplate.dto.CtgCfgStatTypeListDto;
import ngvgroup.com.rpt.features.ctgcfgstattemplate.dto.CtgCfgStatTypeResponse;
import ngvgroup.com.rpt.features.ctgcfgstattemplate.dto.StatFilterTreeDto;
import ngvgroup.com.rpt.features.ctgcfgstattemplate.model.CtgCfgStatType;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;

import java.util.List;

public interface CtgCfgStatTypeService {

    List<CtgCfgStatTypeResponse> getStatTypeByReportModuleCode(String commonCode);

    List<CtgCfgStatTypeResponse> getAllStatType(String keyword);

    List<StatFilterTreeDto> getStatFilterTree();

    Page<CtgCfgStatTypeListDto> search(String keyword, List<String> reportModuleCodes,
                                       Pageable pageable);

    CtgCfgStatType detail(String statTypeCode);

    CtgCfgStatType create(CtgCfgStatType payload);

    CtgCfgStatType update(String statTypeCode, CtgCfgStatType payload);

    void delete(String statTypeCode);

    ResponseEntity<byte[]> exportToExcel(List<String> reportModuleCodes,
                                         String fileName, String keyword, Pageable pageable);

    boolean existsByCode(String statTypeCode);
}
