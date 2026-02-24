package ngvgroup.com.loan.feature.scoring_indc.service;

import ngvgroup.com.loan.feature.scoring_indc.dto.CtgCfgScoringIndcAdd;
import ngvgroup.com.loan.feature.scoring_indc.dto.CtgCfgScoringIndcDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;

public interface CtgCfgScoringIndcService {
    Page<CtgCfgScoringIndcDto> getAllScoringIndc(String keyword, Pageable pageable);
    ResponseEntity<byte[]> exportToExcel(CtgCfgScoringIndcDto dto, String keyword, String fileName);
    void addScoringIndc(CtgCfgScoringIndcAdd dto);
    void updateSrocingIndc(String indicatorCode, CtgCfgScoringIndcAdd add);
    void deleteScoringIndc(String indicatorCode);
    CtgCfgScoringIndcAdd getOne(String indicatorCode);
    Page<CtgCfgScoringIndcDto> getAllScoringIndcMapp(String groupCode,String keyword,Pageable pageable);
    boolean checkCode(String indicatorCode);
}
