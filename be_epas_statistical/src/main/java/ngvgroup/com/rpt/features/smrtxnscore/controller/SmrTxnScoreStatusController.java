package ngvgroup.com.rpt.features.smrtxnscore.controller;

import com.ngvgroup.bpm.core.common.dto.ResponseData;
import com.ngvgroup.bpm.core.logging.activity.annotation.LogActivity;
import lombok.RequiredArgsConstructor;
import ngvgroup.com.rpt.features.smrtxnscore.dto.SmrTxnScoreStatusDTO;
import ngvgroup.com.rpt.features.smrtxnscore.service.SmrTxnScoreStatusService;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("srm-score-status")
public class SmrTxnScoreStatusController {
    private final SmrTxnScoreStatusService service;

    @LogActivity(function = "Lấy tất cả trạng thái điểm")
    @GetMapping
    public ResponseEntity<ResponseData<List<SmrTxnScoreStatusDTO>>> getAll(){
        return ResponseData.okEntity(service.getAllData());
    }

    @LogActivity(function = "Phân trang trạng thái điểm")
    @GetMapping("/page")
    public ResponseEntity<ResponseData<Page<SmrTxnScoreStatusDTO>>> pageData(@RequestParam String keyword, @ParameterObject Pageable pageable,@RequestParam String scoreInstanceCode){
        return ResponseData.okEntity(service.pageData(keyword,scoreInstanceCode, pageable));
    }

    @LogActivity(function = "Lấy tất cả trạng thái kết quả điểm")
    @GetMapping("rs-score-type")
    public ResponseEntity<ResponseData<List<SmrTxnScoreStatusDTO>>> getRsAll(){
        return ResponseData.okEntity(service.getAllRsData());
    }


}
