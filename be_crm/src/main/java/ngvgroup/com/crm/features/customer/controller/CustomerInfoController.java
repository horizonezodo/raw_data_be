package ngvgroup.com.crm.features.customer.controller;

import java.util.List;

import io.swagger.v3.oas.annotations.Operation;
import ngvgroup.com.crm.features.common.dto.TemplateResDto;
import ngvgroup.com.crm.features.customer.bpm.register.CustomerRegisterStarter;
import ngvgroup.com.crm.features.customer.dto.profile.CustomerProfileDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.ngvgroup.bpm.core.common.dto.ResponseData;
import com.ngvgroup.bpm.core.common.excel.ExportExcel;

import lombok.RequiredArgsConstructor;
import ngvgroup.com.crm.features.customer.dto.CustomerRelationDetailDTO;
import ngvgroup.com.crm.features.customer.dto.CustomerInfoDto;
import ngvgroup.com.crm.features.customer.dto.CustomerSearchRequestDto;
import ngvgroup.com.crm.features.customer.dto.CustomerSearchResultDTO;
import ngvgroup.com.crm.features.customer.service.CustomerInfoService;

@RestController
@RequestMapping("/customer-info")
@RequiredArgsConstructor
public class CustomerInfoController {
    private final CustomerInfoService service;
    private final ExportExcel exportExcel;

    @PostMapping("/search")
    public ResponseEntity<ResponseData<Page<CustomerSearchResultDTO>>> search(@RequestBody CustomerSearchRequestDto dto,
            Pageable pageable) {
        return ResponseData.okEntity(service.searchCustomers(dto, pageable));
    }

    @PostMapping("/export-excel")
    public ResponseEntity<byte[]> exportExcel(@RequestBody CustomerSearchRequestDto dto,
            @RequestParam("type") String type,
            Pageable pageable) throws Exception {
        if ("all".equalsIgnoreCase(type)) {
            pageable = Pageable.unpaged();
        }
        Page<CustomerSearchResultDTO> result = service.searchCustomers(dto, pageable);
        List<CustomerSearchResultDTO> data = result.getContent();
        return exportExcel.exportExcel(data, "Customer_Profiles");
    }

    @GetMapping("/reln-detail/{customerCode}")
    public ResponseEntity<ResponseData<CustomerRelationDetailDTO>> getRelnDetail(@PathVariable String customerCode) {
        return ResponseData.okEntity(service.getCustomerRelationDetail(customerCode));
    }

    @GetMapping("/get-all")
    public ResponseEntity<ResponseData<Page<CustomerRelationDetailDTO>>> getAll(@RequestParam String keyword,
            Pageable pageable) {
        return ResponseData.okEntity(service.searchCustomerRelations(keyword, pageable));
    }

    @GetMapping("/get-detail/{customerCode}")
    public ResponseEntity<ResponseData<CustomerInfoDto>> getDetail(@PathVariable String customerCode) {
        return ResponseData.okEntity(service.getCustomerDetail(customerCode));
    }

    @Operation(summary = "Tải file biểu mẫu chỉnh sửa", description = "Tải file biểu mẫu chỉnh sửa thông tin khách hàng (chưa ký) với thông tin đã điền.")
    @PostMapping(value = "/download-template", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<byte[]> downloadTemplate(
            @RequestBody CustomerProfileDTO dto) {
        TemplateResDto template = service.getTemplateFileDetail();
        return ResponseEntity.ok()
                .header(org.springframework.http.HttpHeaders.CONTENT_DISPOSITION,
                        "attachment; filename=\"" + template.getFileName() + "\"")
                .body(service.generateTemplateFile(dto, template));
    }
}
