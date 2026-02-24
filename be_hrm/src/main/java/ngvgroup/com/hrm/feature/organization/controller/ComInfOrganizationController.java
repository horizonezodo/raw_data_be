package ngvgroup.com.hrm.feature.organization.controller;

import com.ngvgroup.bpm.core.common.dto.ResponseData;
import io.swagger.v3.oas.annotations.Operation;
import lombok.AllArgsConstructor;
import ngvgroup.com.hrm.feature.organization.dto.ComInfOrganizationDto;
import ngvgroup.com.hrm.feature.organization.service.ComInfOrganizationService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/organization")
@AllArgsConstructor
public class ComInfOrganizationController {

    private final ComInfOrganizationService comInfOrganizationService;

    @Operation(
            summary = "Danh sách tổ chức"
    )
    @GetMapping
    public ResponseEntity<ResponseData<List<ComInfOrganizationDto>>> getListOrganize() {
        List<ComInfOrganizationDto> listOrganizations = comInfOrganizationService.listOrganizations();
        return ResponseData.okEntity(listOrganizations);
    }
}
