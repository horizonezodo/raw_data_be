package ngvgroup.com.rpt.features.ctgcfgworkflow.service;

import ngvgroup.com.rpt.features.ctgcfgworkflow.dto.ctgcfgworkflow.CtgCfgWorkflowWithTransitionsDto;

public interface CtgCfgWorkflowWithTransitionsService {

    /**
     * Tạo mới workflow với đầy đủ transitions, conditions và post-functions
     * 
     * @param dto Dữ liệu workflow đầy đủ
     * @return Workflow đã tạo với ID được generate
     */
    CtgCfgWorkflowWithTransitionsDto createWorkflowWithTransitions(CtgCfgWorkflowWithTransitionsDto dto);

    /**
     * Cập nhật workflow với đầy đủ transitions, conditions và post-functions
     * 
     * @param dto          Dữ liệu workflow đầy đủ
     * @param workflowCode Mã workflow cần cập nhật
     * @return Workflow đã cập nhật
     */
    CtgCfgWorkflowWithTransitionsDto updateWorkflowWithTransitions(CtgCfgWorkflowWithTransitionsDto dto,
            String workflowCode);

    /**
     * Lấy thông tin workflow với đầy đủ transitions, conditions và post-functions
     * 
     * @param workflowCode Mã workflow
     * @return Workflow đầy đủ thông tin
     */
    CtgCfgWorkflowWithTransitionsDto getWorkflowWithTransitions(String workflowCode);

    Boolean existsByWorkflowCodeAndTransitionCodeAndIsDeleted( String workflowCode, String transitionCode);
}
