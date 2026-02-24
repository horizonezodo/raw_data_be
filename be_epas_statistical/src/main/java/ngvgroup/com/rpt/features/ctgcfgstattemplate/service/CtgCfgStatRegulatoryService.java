package ngvgroup.com.rpt.features.ctgcfgstattemplate.service;

import com.ngvgroup.bpm.core.common.exception.BusinessException;
import ngvgroup.com.rpt.features.ctgcfgstattemplate.dto.ctgcfgstatregulatory.CtgCfgStatRegulatoryDto;
import ngvgroup.com.rpt.features.ctgcfgstattemplate.dto.ctgcfgstatregulatory.StatRegulatoryInfo;
import ngvgroup.com.rpt.features.ctgcfgstattemplate.dto.ctgcfgstatregulatory.CtgCfgStatRegulatoryRequest;
import ngvgroup.com.rpt.features.ctgcfgstattemplate.dto.ctgcfgstatregulatory.CtgCfgStatRegulatorySearch;
import ngvgroup.com.rpt.features.ctgcfgstattemplate.dto.ctgcfgstatregulatory.CtgCfgStatRegulatoryResponse;
import ngvgroup.com.rpt.features.ctgcfgstattemplate.model.CtgCfgStatRegulatory;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;

import java.util.List;

public interface CtgCfgStatRegulatoryService {
    Page<CtgCfgStatRegulatoryDto> getAllCtgCfgStatRegulatory(CtgCfgStatRegulatorySearch request, Pageable pageable);

    CtgCfgStatRegulatory addCtgCfgStatRegulatory(CtgCfgStatRegulatoryRequest request) throws BusinessException;

    CtgCfgStatRegulatory updateCtgCfgStatRegulatory(CtgCfgStatRegulatoryRequest request);

    CtgCfgStatRegulatoryResponse getDetail(String statRegulatoryCode);

    void deleteCtgCfgStatRegulatory(Long id) throws BusinessException;

    List<StatRegulatoryInfo> getStatRegulatoryByStatTypeCode(String statTypeCode, String keyword);
    ResponseEntity<ByteArrayResource> exportToExcel(List<String> labels, List<String> reportModuleCodes,
                                                    String fileName);

}
