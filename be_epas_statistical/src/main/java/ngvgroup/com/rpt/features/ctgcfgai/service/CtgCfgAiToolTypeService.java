package ngvgroup.com.rpt.features.ctgcfgai.service;

import ngvgroup.com.rpt.features.ctgcfgai.dto.ctgcfgaitooltype.CtgCfgAiToolTypeDTO;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;

import java.util.List;

public interface CtgCfgAiToolTypeService {
    void createAiTool(CtgCfgAiToolTypeDTO dto);
    void updateAiTool(CtgCfgAiToolTypeDTO dto, String toolAiTypeCode);
    void deleteAiTool(String toolAiTypeCode);
    Page<CtgCfgAiToolTypeDTO> page(String keyword, Pageable pageable);
    CtgCfgAiToolTypeDTO getDetail(String toolAiTypeCode);
    ResponseEntity<ByteArrayResource> exportToExcel(List<String> labels, String keyword, String fileName);
    List<CtgCfgAiToolTypeDTO> listAiTool();
}
