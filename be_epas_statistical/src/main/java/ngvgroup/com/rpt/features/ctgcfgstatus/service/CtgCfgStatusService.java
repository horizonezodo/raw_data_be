package ngvgroup.com.rpt.features.ctgcfgstatus.service;

import ngvgroup.com.rpt.features.ctgcfgstatus.dto.CtgCfgStatusDto;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;

import java.util.List;

public interface CtgCfgStatusService {
    Page<CtgCfgStatusDto> pageStatus(String keyword, Pageable pageable);

    List<CtgCfgStatusDto> getAllStatusByType(String type);

    void createStatus(CtgCfgStatusDto dto);

    void updateStatus(CtgCfgStatusDto dto, String statusCode);

    void deleteStatus(String statusCode);

    CtgCfgStatusDto getDetail(String statusCode);

    ResponseEntity<ByteArrayResource> exportToExcel(List<String> labels, String keyword, String fileName);

    List<String> getAllCode();
}
