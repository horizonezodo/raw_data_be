package ngvgroup.com.rpt.features.ctgcfgworkflow.service;

import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.ResponseEntity;

import java.util.List;

public interface CtgCfgWorkflowTransitionService {

    /**
     * Xuất danh sách hành động
     * 
     * @param labels       Danh sách tên cột
     * @param keyword      Từ khóa tìm kiếm
     * @param fileName     Tên file Excel
     * @param workflowCode Mã quy trình
     * @return ResponseEntity chứa ByteArrayResource
     */
    ResponseEntity<ByteArrayResource> exportToExcel(List<String> labels, String keyword, String fileName,
            String workflowCode);
}
