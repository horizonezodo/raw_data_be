package com.ngv.zns_service.controller;

import com.ngv.zns_service.dto.request.SearchFilterRequest;
import com.ngv.zns_service.dto.request.ZSSCtaoGtriTsoRequest;
import com.ngv.zns_service.dto.request.ZSSCtaoGtriTsoSearchRequest;
import org.springframework.data.domain.Page;
import com.ngvgroup.bpm.core.common.dto.ResponseData;
import com.ngv.zns_service.dto.response.ZSSCtaoGtriTsoResponse;
import com.ngv.zns_service.exception.ValidationException;
import com.ngv.zns_service.service.ZSSCtaoGtriTsoService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/ctao-gtri-tso")
public class ZSSCtaoGtriTsoController {

    private final ZSSCtaoGtriTsoService ctaoGtriTsoService;
    @PreAuthorize("hasRole(T(com.ngv.zns_service.constant.Roles).USER_VIEW)")
    @PostMapping("/search-all")
    public ResponseEntity<ResponseData<Page<ZSSCtaoGtriTsoResponse>>> searchAll(@RequestBody SearchFilterRequest request) {
        return ResponseData.okEntity(ctaoGtriTsoService.searchAll(request));
    }

    @PreAuthorize("hasRole(T(com.ngv.zns_service.constant.Roles).USER_MODIFY)")
    @PostMapping("/export-excel/{fileName}")
    public ResponseEntity<ByteArrayResource> exportToExcel(@PathVariable String fileName, @RequestBody ZSSCtaoGtriTsoSearchRequest request) {
        return ctaoGtriTsoService.exportToExcel(request, fileName);
    }

    @PreAuthorize("hasRole(T(com.ngv.zns_service.constant.Roles).USER_MODIFY)")
    @PostMapping("/create")
    public ResponseEntity<ResponseData<Void>> create(@Valid @RequestBody ZSSCtaoGtriTsoRequest request) throws ValidationException {
        ctaoGtriTsoService.create(request);
        return ResponseData.createdEntity();
    }

    @PreAuthorize("hasRole(T(com.ngv.zns_service.constant.Roles).USER_MODIFY)")
    @PutMapping("update/{maCtaoGtriTso}")
    public ResponseEntity<ResponseData<Void>> update(@Valid @PathVariable String maCtaoGtriTso,
                                                     @RequestBody ZSSCtaoGtriTsoRequest request) throws ValidationException {
        ctaoGtriTsoService.update(maCtaoGtriTso, request);
        return ResponseData.okEntity(null);
    }

    @PreAuthorize("hasRole(T(com.ngv.zns_service.constant.Roles).USER_DELETE)")
    @DeleteMapping("delete/{maCtaoGtriTso}")
    public ResponseEntity<ResponseData<Void>> delete(@PathVariable String maCtaoGtriTso) throws ValidationException {
        ctaoGtriTsoService.delete(maCtaoGtriTso);
        return ResponseData.noContentEntity();
    }
}

