package ngvgroup.com.bpm.features.com_cfg_process_file.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.ngvgroup.bpm.core.common.dto.ResponseData;

import lombok.RequiredArgsConstructor;
import ngvgroup.com.bpm.core.base.dto.ComCfgProcessFileDto;
import ngvgroup.com.bpm.features.com_cfg_process_file.service.ComCfgProcessFileService;

@RestController
@RequestMapping("/process-file")
@RequiredArgsConstructor
public class ProcessFileController {

    private final ComCfgProcessFileService service;

    @GetMapping("/get-details/{processTypeCode}")
    public ResponseEntity<ResponseData<List<ComCfgProcessFileDto>>> getProcessFiles(@PathVariable String processTypeCode) {
        return ResponseData.okEntity(service.getDetails(processTypeCode));
    }
}
