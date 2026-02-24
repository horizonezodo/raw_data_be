package com.naas.category_service.service;

import com.naas.category_service.dto.CtgCfgScoringIndcGroup.CtgCfgScoringIndcGroupDto;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;

import java.util.List;

public interface CtgCfgScoringIndcGroupService {
    Page<CtgCfgScoringIndcGroupDto> searchAll(String keyword, Pageable pageable);

    ResponseEntity<ByteArrayResource> exportToExcel(String keyword, String fileName, List<String> labels);

    void createScoringIndcGroup(CtgCfgScoringIndcGroupDto scoringIndcGroupDto);

    void updateScoringIndcGroup(CtgCfgScoringIndcGroupDto scoringIndcGroupDto);

    void deleteScoringIndcGroup(String scoringIndcGroupCode);

    CtgCfgScoringIndcGroupDto getDetailScoringIndcGroup(String scoringIndcGroupCode);

    boolean checkExist(String code);
}
