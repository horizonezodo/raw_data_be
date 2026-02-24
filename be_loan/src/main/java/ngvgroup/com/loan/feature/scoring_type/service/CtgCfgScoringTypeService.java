package ngvgroup.com.loan.feature.scoring_type.service;

import ngvgroup.com.loan.feature.scoring_type.dto.CtgCfgScoringTypeDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface CtgCfgScoringTypeService {
    List<CtgCfgScoringTypeDTO> getAllData();

    Page<CtgCfgScoringTypeDTO> pageData(String keyword, Pageable pageable);

    List<CtgCfgScoringTypeDTO> getAll();

    Page<CtgCfgScoringTypeDTO> searchAll(String keyword, Pageable pageable);

    ResponseEntity<byte[]> exportToExcel(String keyword, String fileName, List<String> labels);

    void create(CtgCfgScoringTypeDTO scoringTypeDTO);

    void update(CtgCfgScoringTypeDTO scoringTypeDTO);

    void delete(String scoringTypeCode);

    CtgCfgScoringTypeDTO getDetail(String scoringTypeCode);

    void uploadFile(MultipartFile file, String scoringTypeCode);

    ResponseEntity<byte[]> downloadFile(String scoringTypeCode);

    void deleteFile(String scoringTypeCode);

    boolean checkExist(String code);
}
