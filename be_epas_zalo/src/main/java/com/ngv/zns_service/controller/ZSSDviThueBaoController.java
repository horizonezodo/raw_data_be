package com.ngv.zns_service.controller;

import com.ngv.zns_service.dto.request.SearchFilterRequest;
import com.ngv.zns_service.dto.request.ZSSDviThueBaoRequest;
import com.ngv.zns_service.dto.request.ZSSDviThueBaoSearchRequest;
import org.springframework.data.domain.Page;
import com.ngvgroup.bpm.core.common.dto.ResponseData;
import com.ngv.zns_service.dto.response.ZSSDviThueBaoResponse;
import com.ngv.zns_service.exception.ValidationException;
import com.ngv.zns_service.service.ZSSDviThueBaoService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/dvi-thue-bao")
public class ZSSDviThueBaoController {

    private final ZSSDviThueBaoService zssDviThueBaoService;

    @PreAuthorize("hasRole(T(com.ngv.zns_service.constant.Roles).USER_VIEW)")
    @PostMapping("/search-all")
    public ResponseEntity<ResponseData<Page<ZSSDviThueBaoResponse>>> searchAll(@RequestBody SearchFilterRequest request) {
        return ResponseData.okEntity(zssDviThueBaoService.searchAll(request));
    }
    @PreAuthorize("hasRole(T(com.ngv.zns_service.constant.Roles).USER_VIEW)")
    @PostMapping("/search-advanced")
    public ResponseEntity<ResponseData<Page<ZSSDviThueBaoResponse>>> findAll(@RequestBody ZSSDviThueBaoSearchRequest request) {
        return ResponseData.okEntity(zssDviThueBaoService.findAll(request));
    }

    @PreAuthorize("hasRole(T(com.ngv.zns_service.constant.Roles).USER_MODIFY)")
    @PostMapping
    public ResponseEntity<ResponseData<Void>> create(@Valid @RequestBody ZSSDviThueBaoRequest request) throws ValidationException {
        zssDviThueBaoService.create(request);
        return ResponseData.createdEntity();
    }

    @PreAuthorize("hasRole(T(com.ngv.zns_service.constant.Roles).USER_MODIFY)")
    @PutMapping("/{maThueBao}")
    public ResponseEntity<ResponseData<Void>> update(@PathVariable String maThueBao,
                                                     @RequestBody ZSSDviThueBaoRequest request) throws ValidationException {
        zssDviThueBaoService.update(maThueBao, request);
        return ResponseData.okEntity(null);
    }

    @PreAuthorize("hasRole(T(com.ngv.zns_service.constant.Roles).USER_DELETE)")
    @DeleteMapping("/{maThueBao}")
    public ResponseEntity<ResponseData<Void>> delete(@PathVariable String maThueBao) throws ValidationException {
        zssDviThueBaoService.delete(maThueBao);
        return ResponseData.noContentEntity();
    }

    @PreAuthorize("hasRole(T(com.ngv.zns_service.constant.Roles).USER_MODIFY)")
    @PutMapping("/stop/{maThueBao}")
    public ResponseEntity<ResponseData<Void>> stop(@PathVariable String maThueBao) throws ValidationException {
        zssDviThueBaoService.stop(maThueBao);
        return ResponseData.okEntity(null);
    }

    @PreAuthorize("hasRole(T(com.ngv.zns_service.constant.Roles).USER_VIEW)")
    @GetMapping("/list-ten-goi-dvu")
    public ResponseEntity<ResponseData<Map<String, String>>> listTenGoiDvu() {
        return ResponseData.okEntity(zssDviThueBaoService.listTenGoiDvu());
    }

    @PreAuthorize("hasRole(T(com.ngv.zns_service.constant.Roles).USER_VIEW)")
    @GetMapping("/list-trang-thai-nv")
    public ResponseEntity<ResponseData<Map<String, String>>> listThai() {
        return ResponseData.okEntity(zssDviThueBaoService.listThai());
    }

    @PreAuthorize("hasRole(T(com.ngv.zns_service.constant.Roles).USER_VIEW)")
    @GetMapping("/list-ten-dvi")
    public ResponseEntity<ResponseData<Map<String, String>>> listTenDvi() {
        return ResponseData.okEntity(zssDviThueBaoService.listTenDvi());
    }

    @PreAuthorize("hasRole(T(com.ngv.zns_service.constant.Roles).USER_VIEW)")
    @GetMapping("/list-zoa-id")
    public ResponseEntity<ResponseData<Map<String, String>>> listZOAID(@RequestParam String maDvi) {
        return ResponseData.okEntity(zssDviThueBaoService.listZOAID(maDvi));
    }

//    @PreAuthorize("hasRole(T(com.ngv.zns_service.constant.Roles).USER_VIEW)")
//    @GetMapping("/list-tthai-nvu")
//    public ResponseEntity<ResponseData<Map<String, String>>> listTThaiNvu() {
//        return ResponseData.okEntity(zssDviThueBaoService.listTThaiNvu());
//    }

    @PreAuthorize("hasRole(T(com.ngv.zns_service.constant.Roles).USER_MODIFY)")
    @PostMapping("/export-excel/{fileName}")
    public ResponseEntity<ByteArrayResource> exportToExcel(@PathVariable String fileName, @RequestBody ZSSDviThueBaoSearchRequest request) {
        return zssDviThueBaoService.exportToExcel(request, fileName);
    }


}
