package ngvgroup.com.rpt.features.ctgcfgai.service;

import ngvgroup.com.rpt.features.ctgcfgai.dto.ctgcfgaitool.CtgCfgAiToolDTO;
import ngvgroup.com.rpt.features.ctgcfgai.dto.ctgcfgaitool.CtgCfgAiToolDTOV1;
import ngvgroup.com.rpt.features.ctgcfgai.dto.ctgcfgaitool.CtgCfgAiToolDTOV2;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;

import java.util.List;

public interface CtgCfgAiToolService {
    Page<CtgCfgAiToolDTOV1> pageAiTool(String keyword, Pageable pageable);
    List<CtgCfgAiToolDTOV2> getAllTools();
    void createAiTool(CtgCfgAiToolDTO dto);
    void updateAiTool(CtgCfgAiToolDTO dto,String toolAiCode);
    void deleteAiTool(String toolAiCode);
    CtgCfgAiToolDTO getDetail(String toolAiCode);
    ResponseEntity<ByteArrayResource> exportToExcel(List<String> labels, String keyword, String fileName);
}
