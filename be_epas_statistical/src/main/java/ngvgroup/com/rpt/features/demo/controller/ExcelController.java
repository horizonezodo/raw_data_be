package ngvgroup.com.rpt.features.demo.controller;

import lombok.RequiredArgsConstructor;
import ngvgroup.com.rpt.features.demo.service.ViewExcelService;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/view-excel")
@CrossOrigin("*")
@RequiredArgsConstructor
public class ExcelController {
    private final ViewExcelService viewExcelService;

}

