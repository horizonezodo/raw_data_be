package ngvgroup.com.bpm.features.common.controller;

import com.ngvgroup.bpm.core.common.dto.ResponseData;
import io.swagger.v3.oas.annotations.Operation;
import lombok.AllArgsConstructor;
import ngvgroup.com.bpm.features.common.dto.ComInfOrganizationDto;
import ngvgroup.com.bpm.features.common.service.ComInfOrganizationService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/organization")
@AllArgsConstructor
public class ComInfOrganizationController {

    private final ComInfOrganizationService comInfOrganizationService;

    @Operation(
            summary = "Danh sách tổ chức, chi nhánh"
    )
    @GetMapping
    public ResponseEntity<ResponseData<List<ComInfOrganizationDto>>> getListOrganize() {
        List<ComInfOrganizationDto> listOrganizations = comInfOrganizationService.listOrganizations();
        return ResponseData.okEntity(listOrganizations);
    }

    @Operation(
            summary = "Danh sách tổ chức, chi nhánh theo filter"
    )
    @GetMapping("/list")
    public ResponseEntity<ResponseData<List<ComInfOrganizationDto>>> searchOrganization(
            @RequestParam("keyword") String keyword) {
        return searchOrganizationInternal(keyword);
    }

    @Operation(summary = "Danh sách tổ chức, chi nhánh theo filter")
    @GetMapping("/{keyword}")
    public ResponseEntity<ResponseData<List<ComInfOrganizationDto>>> searchOrganizationByFilter(
            @PathVariable("keyword") String keyword) {
        return searchOrganizationInternal(keyword);
    }

    private ResponseEntity<ResponseData<List<ComInfOrganizationDto>>> searchOrganizationInternal(String keyword) {
        List<ComInfOrganizationDto> listOrganizations =
                comInfOrganizationService.searchOrganizations(keyword);
        return ResponseData.okEntity(listOrganizations);
    }
}
