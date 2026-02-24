package ngvgroup.com.rpt.features.ctgcfgstatscorerank.service;

import ngvgroup.com.rpt.features.ctgcfgstatscorerank.dto.CtgCfgStatScoreGroupDto;
import ngvgroup.com.rpt.features.ctgcfgstatscorerank.dto.StatScoreGroupRequest;
import ngvgroup.com.rpt.features.ctgcfgstatscorerank.dto.StatScoreGroupSearch;
import ngvgroup.com.rpt.features.ctgcfgstatscorerank.dto.CtgCfgStatScoreGroupResponse;
import ngvgroup.com.rpt.features.ctgcfgstatscorerank.model.CtgCfgStatScoreGroup;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;

import java.util.List;

public interface CtgCfgStatScoreGroupService {
    Page<CtgCfgStatScoreGroupDto> searchAll(String keyword, String statScoreTypeCode, Pageable pageable);

    Page<CtgCfgStatScoreGroupResponse> getAll(StatScoreGroupSearch search, Pageable pageable);

    CtgCfgStatScoreGroup add(StatScoreGroupRequest req);

    CtgCfgStatScoreGroupResponse getDetail(String statScoreGroupCode);

    CtgCfgStatScoreGroup edit(StatScoreGroupRequest req);

    void delete(String statScoreGroupCode);

    ResponseEntity<ByteArrayResource> exportToExcel(List<String> labels, List<String> statScoreGroupCode,
                                                    String fileName);

    Boolean checkExistStatScoreGroupCode(String statScoreGroupCode);
}
