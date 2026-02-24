package ngvgroup.com.fac.feature.fac_inf_acc.controller;

import com.ngvgroup.bpm.core.web.controller.BaseController;
import ngvgroup.com.fac.feature.fac_inf_acc.dto.FacInfAccBalHistoryDto;
import ngvgroup.com.fac.feature.fac_inf_acc.model.FacInfAccBal;
import ngvgroup.com.fac.feature.fac_inf_acc.service.FacInfAccBalService;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;
import java.util.List;


@RestController
@RequestMapping("fac-inf-acc-bal")
public class FacInfAccBalController extends BaseController<FacInfAccBal, FacInfAccBalHistoryDto, FacInfAccBalService> {

    public FacInfAccBalController(FacInfAccBalService service) {
        super(service);
    }

    @GetMapping("/balance-history")
    public ResponseEntity<List<FacInfAccBalHistoryDto>> getHistory(
            @RequestParam String accNo,
            @RequestParam(required = false)
            @DateTimeFormat(pattern = "dd/MM/yyyy")
            LocalDate fromDate,
            @RequestParam(required = false)
            @DateTimeFormat(pattern = "dd/MM/yyyy")
            LocalDate toDate) {

        return ResponseEntity.ok(
                service.getHistory(accNo, fromDate, toDate)
        );
    }
}
