package com.naas.category_service.service;

import com.naas.category_service.dto.CtgCfgScoringType.CtgCfgScoringTypeDto;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.query.Param;
import org.springframework.http.ResponseEntity;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface CtgCfgScoringTypeService {

    List<CtgCfgScoringTypeDto> getAll();

    Page<CtgCfgScoringTypeDto> searchAll(String keyword, Pageable pageable);

    ResponseEntity<ByteArrayResource> exportToExcel(String keyword, String fileName, List<String> labels);

    void create(CtgCfgScoringTypeDto ctgCfgScoringTypeDto);

    void update(CtgCfgScoringTypeDto ctgCfgScoringTypeDto);

    void delete(String scoringTypeCode);

    CtgCfgScoringTypeDto getDetail(String scoringTypeCode);

    void uploadFile(MultipartFile file, String scoringTypeCode) throws Exception;

    ResponseEntity<byte[]> downloadFile(String scoringTypeCode) throws Exception;

    void deleteFile(String scoringTypeCode);

    boolean checkExist(String code);
}
