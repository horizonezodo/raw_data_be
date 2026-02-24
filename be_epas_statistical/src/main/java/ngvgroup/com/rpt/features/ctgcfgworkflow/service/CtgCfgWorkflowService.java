package ngvgroup.com.rpt.features.ctgcfgworkflow.service;

import ngvgroup.com.rpt.features.ctgcfgworkflow.dto.ctgcfgworkflow.CtgCfgWorkflowDto;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;

import java.util.List;

public interface CtgCfgWorkflowService {
    Page<CtgCfgWorkflowDto> pageWorkflow(String keyword, Pageable pageable);

    void createWorkflow(CtgCfgWorkflowDto dto);

    void updateWorkflow(CtgCfgWorkflowDto dto, String workflowCode);

    void deleteWorkflow(String workflowCode);

    CtgCfgWorkflowDto getDetail(String workflowCode);

    ResponseEntity<ByteArrayResource> exportToExcel(List<String> labels, String keyword, String fileName);

    List<CtgCfgWorkflowDto> getList();

    Boolean existsByWorkflowCode(String workflowCode);
}
