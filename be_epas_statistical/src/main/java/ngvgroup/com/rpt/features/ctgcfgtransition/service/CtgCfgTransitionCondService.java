package ngvgroup.com.rpt.features.ctgcfgtransition.service;

import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.ResponseEntity;

import java.util.List;

public interface CtgCfgTransitionCondService {

    /**
     * Xuất danh sách điều kiện
     * 
     * @param labels         Danh sách tên cột
     * @param keyword        Từ khóa tìm kiếm
     * @param fileName       Tên file Excel
     * @param transitionCode Mã hành động
     * @return ResponseEntity chứa ByteArrayResource
     */
    ResponseEntity<ByteArrayResource> exportToExcel(List<String> labels, String keyword, String fileName,
            String transitionCode);
}
