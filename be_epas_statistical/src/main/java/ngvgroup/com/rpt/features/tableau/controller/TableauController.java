package ngvgroup.com.rpt.features.tableau.controller;

import com.ngvgroup.bpm.core.common.dto.ResponseData;
import com.ngvgroup.bpm.core.logging.activity.annotation.LogActivity;
import lombok.RequiredArgsConstructor;
import ngvgroup.com.rpt.features.tableau.service.TableauService;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;

@RequestMapping("/reports")
@RequiredArgsConstructor
@RestController
public class TableauController {

    private final TableauService tableauService;

    @LogActivity(function = "Lấy Tableau ticket")
    @GetMapping("/get-ticket")
    public ResponseEntity<ResponseData<String>> getTicket(@RequestParam(name = "targetSite", required = false) String targetSite) {
        return ResponseData.okEntity(tableauService.getTicket(targetSite));
    }
}
