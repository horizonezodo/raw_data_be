package ngvgroup.com.rpt.features.ctgcfgstatscorerank.service;

import ngvgroup.com.rpt.features.ctgcfgstatscorerank.dto.CtgCfgStatScoreTypeDto;
import ngvgroup.com.rpt.features.ctgcfgstatscorerank.model.CtgCfgStatScoreType;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;

import java.util.List;

public interface CtgCfgStatScoreTypeService {
    Page<CtgCfgStatScoreTypeDto> getAll(String keyword,Pageable pageable);
    List<CtgCfgStatScoreType> listAll();

    void create(CtgCfgStatScoreTypeDto ctgCfgStatScoreTypeDto);
    void update(CtgCfgStatScoreTypeDto ctgCfgStatScoreTypeDto);
    void delete(Long id);
    CtgCfgStatScoreTypeDto getDetail(Long id);
    boolean checkExist(String statScoreTypeCode);
    ResponseEntity<ByteArrayResource> exportToExcel(List<String> labels,String keyword, String fileName);
}
