package ngvgroup.com.fac.feature.common.controller;

import com.ngvgroup.bpm.core.common.dto.ResponseData;
import lombok.AllArgsConstructor;
import ngvgroup.com.fac.feature.common.dto.ComCfgProcessFileDto;
import ngvgroup.com.fac.feature.common.service.ComCfgProcessFileService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/process-file")
@AllArgsConstructor
public class ComCfgProcessFileController {
    private ComCfgProcessFileService comCfgProcessFileService;
    @GetMapping("/get-by-process-type-code")
    ResponseEntity<ResponseData<List<ComCfgProcessFileDto>>> getByProcessTypeCode(@RequestParam String processTypeCode){
        return ResponseData.okEntity(comCfgProcessFileService.getByProcessTypeCode(processTypeCode));
    }
}
