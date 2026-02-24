package ngvgroup.com.bpm.features.com_cfg_txn_content.controller;

import com.ngvgroup.bpm.core.common.dto.ResponseData;
import com.ngvgroup.bpm.core.web.controller.BaseController;
import ngvgroup.com.bpm.features.com_cfg_txn_content.dto.*;
import ngvgroup.com.bpm.features.com_cfg_txn_content.model.ComCfgTxnContent;
import ngvgroup.com.bpm.features.com_cfg_txn_content.service.ComCfgTxnContentService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/txn-content")
public class ComCfgTxnContentController extends BaseController<
        ComCfgTxnContent,
        ComCfgTxnContentDto,
        ComCfgTxnContentService> {

    public ComCfgTxnContentController(ComCfgTxnContentService comCfgTxnContentService) {
        super(comCfgTxnContentService);
    }

    @PostMapping("/search")
    public ResponseEntity<ResponseData<List<ComCfgTxnContentDto>>> search(
    ) {
        return ResponseData.okEntity(service.search());
    }

    @PostMapping("/export-excel")
    public ResponseEntity<byte[]> exportExcel(
        @RequestBody List<ComCfgTxnContentDto> list
    ) {
        return service.exportExcel(list);
    }

    @PostMapping("/save")
    public ResponseEntity<Void> save(
            @RequestBody ComCfgTxnContentSaveDto dto
    ) {
        service.save(dto);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/check-exist")
    public ResponseEntity<ResponseData<Boolean>> checkExistsByContentCode(@RequestParam String contentCode) {
        return ResponseData.okEntity(service.existsByContentCode(contentCode));
    }

    @GetMapping("/dtl/search")
    public ResponseEntity<ResponseData<Page<ComCfgTxnContentDtlDto>>> search(
            @RequestParam String contentCode,
            @RequestParam(required = false) String keyword,
            Pageable pageable
    ) {
        return ResponseData.okEntity(
                service.detailSearch(contentCode, keyword, pageable)
        );
    }

    @PostMapping("/dtl/export-excel")
    public ResponseEntity<byte[]> exportExcel(
            @RequestBody List<ComCfgTxnContentSaveDtlDto> dtlList,
            @RequestParam String fileName) {
        return service.exportExcelDetail(dtlList, fileName);
    }

    @GetMapping("/dtl/check-exist")
    public ResponseEntity<ResponseData<Boolean>> checkExistComCfgReportParamBase(@RequestParam String contentCode, @RequestParam String contentDtlCode) {
        return ResponseData.okEntity(service.existsByContentCodeAndContentDtlCode(contentCode, contentDtlCode));
    }
}
