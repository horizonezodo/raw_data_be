package com.naas.admin_service.features.tenant.controller;

import com.naas.admin_service.features.tenant.dto.TenantRequestDto;
import com.naas.admin_service.features.tenant.dto.TenantResponseDto;
import com.naas.admin_service.features.tenant.model.TenantDbConfig;
import com.naas.admin_service.features.tenant.service.TenantDbConfigService;
import com.ngvgroup.bpm.core.common.dto.ResponseData;
import com.ngvgroup.bpm.core.web.controller.BaseController;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/tenant")
public class TenantDbConfigController extends BaseController<TenantDbConfig, TenantRequestDto, TenantDbConfigService> {
    public TenantDbConfigController(TenantDbConfigService service) {
        super(service);
    }
    @GetMapping("/search")
    public ResponseEntity<ResponseData<Page<TenantResponseDto>>> search(@RequestParam String keyword, Pageable pageable) {
        return ResponseData.okEntity(service.search(keyword, pageable));
    }
    @GetMapping("/exist")
    public ResponseEntity<ResponseData<Boolean>> existsByTenantIdAndActive(@RequestParam String tenantId) {
        return ResponseData.okEntity(service.existsByTenantIdAndActive(tenantId));
    }
}