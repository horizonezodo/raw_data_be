package ngvgroup.com.fac.feature.common.controller;

import com.ngvgroup.bpm.core.common.dto.ResponseData;
import lombok.RequiredArgsConstructor;
import ngvgroup.com.fac.feature.common.dto.ComInfOrganizationDTO;
import ngvgroup.com.fac.feature.common.service.ComInfOrganizationService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/org")
@RequiredArgsConstructor
public class ComInfOrganizationController {
    private final ComInfOrganizationService service;

    @GetMapping
    public ResponseEntity<ResponseData<List<ComInfOrganizationDTO>>> getAll(){
        return ResponseData.okEntity(this.service.getAll());
    }
}
