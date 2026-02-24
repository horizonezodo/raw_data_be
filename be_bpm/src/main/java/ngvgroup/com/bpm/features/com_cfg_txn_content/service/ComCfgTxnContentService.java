package ngvgroup.com.bpm.features.com_cfg_txn_content.service;

import com.ngvgroup.bpm.core.persistence.service.BaseService;
import ngvgroup.com.bpm.features.com_cfg_txn_content.dto.*;
import ngvgroup.com.bpm.features.com_cfg_txn_content.model.ComCfgTxnContent;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;

import java.util.List;

public interface ComCfgTxnContentService extends BaseService<ComCfgTxnContent, ComCfgTxnContentDto> {

    List<ComCfgTxnContentDto> findAllByProcessTypeCode(String processTypeCode);

    List<ComCfgTxnContentDto> search();

    ComCfgTxnContentDto getDetail(Long id);

    ResponseEntity<byte[]> exportExcel(List<ComCfgTxnContentDto> list);

    void save(ComCfgTxnContentSaveDto dto);

    boolean existsByContentCode(String contentCode);

    Page<ComCfgTxnContentDtlDto> detailSearch(String contentCode, String keyword, Pageable pageable);
    ResponseEntity<byte[]> exportExcelDetail(List<ComCfgTxnContentSaveDtlDto> comCfgTxnContentSaveDtlDtos, String fileName);
    boolean existsByContentCodeAndContentDtlCode(String contentCode, String contentDtlCode);

}
