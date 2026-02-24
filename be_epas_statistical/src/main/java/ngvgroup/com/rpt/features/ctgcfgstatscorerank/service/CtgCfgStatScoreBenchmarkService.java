package ngvgroup.com.rpt.features.ctgcfgstatscorerank.service;

import ngvgroup.com.rpt.features.ctgcfgstatscorerank.dto.StatScoreBenchmarkDto;
import ngvgroup.com.rpt.features.ctgcfgstatscorerank.dto.StatScoreBenchmarkRequest;
import ngvgroup.com.rpt.features.ctgcfgstatscorerank.model.CtgCfgStatScoreBenchmark;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;

import java.util.List;

public interface CtgCfgStatScoreBenchmarkService {
    Page<StatScoreBenchmarkDto> getAll(String keyword, Pageable pageable);

    CtgCfgStatScoreBenchmark add(StatScoreBenchmarkRequest req);

    StatScoreBenchmarkDto getDetail(String statScoreGroupCode);

    CtgCfgStatScoreBenchmark edit(StatScoreBenchmarkRequest req);

    void delete(String benchmarkCode);

    ResponseEntity<ByteArrayResource> exportToExcel(List<String> labels, List<String> benchmarkCode,
                                                    String fileName);

    List<CtgCfgStatScoreBenchmark> getByCode(String statScoreTypeCode);
}
