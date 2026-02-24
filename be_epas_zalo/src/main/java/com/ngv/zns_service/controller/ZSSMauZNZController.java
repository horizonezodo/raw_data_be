package com.ngv.zns_service.controller;

import com.fasterxml.jackson.databind.node.ObjectNode;

import com.ngv.zns_service.dto.request.MauZNSRequest;
import com.ngv.zns_service.dto.request.SearchFilterRequest;
import com.ngv.zns_service.dto.request.ZSSMauZNZSearchRequest;
import com.ngv.zns_service.dto.request.ZssMauZnsTsoRequest;
import com.ngv.zns_service.dto.response.MauZNSResponse;
import org.springframework.data.domain.Page;
import com.ngvgroup.bpm.core.common.dto.ResponseData;
import com.ngvgroup.bpm.core.persistence.service.storedprocedure.BaseStoredProcedureService;
import com.ngv.zns_service.dto.response.ZSSMauZNZResponse;
import com.ngv.zns_service.exception.ValidationException;
import com.ngv.zns_service.service.ZSSMauZNZService;

import com.ngv.zns_service.util.http.ResponseUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.List;
import java.util.Map;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/zns-template")
public class ZSSMauZNZController extends BaseStoredProcedureService {
    private final ZSSMauZNZService zssMauZNZService;
    

    @PreAuthorize("hasRole(T(com.ngv.zns_service.constant.Roles).USER_VIEW)")
    @PostMapping("/search-advanced")
    public ResponseEntity<ResponseData<Page<ZSSMauZNZResponse>>> findAll(
            @RequestBody ZSSMauZNZSearchRequest request) {
        return ResponseData.okEntity(zssMauZNZService.findAll(request));
    }

    @PreAuthorize("hasRole(T(com.ngv.zns_service.constant.Roles).USER_VIEW)")
    @PostMapping("/search-all")
    public ResponseEntity<ResponseData<Page<ZSSMauZNZResponse>>> searchAll(
            @RequestBody SearchFilterRequest request) {
        return ResponseData.okEntity(zssMauZNZService.searchAll(request));
    }

    @PreAuthorize("hasRole(T(com.ngv.zns_service.constant.Roles).USER_MODIFY)")
    @PostMapping("/export-excel/{fileName}")
    public ResponseEntity<ByteArrayResource> exportToExcel(@PathVariable String fileName,
            @RequestBody ZSSMauZNZSearchRequest request) {
        return zssMauZNZService.exportToExcel(request, fileName);
    }

    @PreAuthorize("hasRole(T(com.ngv.zns_service.constant.Roles).USER_VIEW)")
    @GetMapping("/list-loai-ndung-mau")
    public ResponseEntity<ResponseData<Map<String, String>>> listLoaiNdungMau() {
        return ResponseData.okEntity(zssMauZNZService.listLoaiNdungMau());
    }

    @PreAuthorize("hasRole(T(com.ngv.zns_service.constant.Roles).USER_VIEW)")
    @GetMapping("/list-trang-thai-mau")
    public ResponseEntity<ResponseData<Map<String, String>>> listTthaiMau() {
        return ResponseData.okEntity(zssMauZNZService.listTthaiMau());
    }

    @PreAuthorize("hasRole(T(com.ngv.zns_service.constant.Roles).USER_VIEW)")
    @GetMapping("/list-ten-dvi")
    public ResponseEntity<ResponseData<Map<String, String>>> listTenDvi() {
        return ResponseData.okEntity(zssMauZNZService.listTenDvi());
    }

    @PreAuthorize("hasRole(T(com.ngv.zns_service.constant.Roles).USER_VIEW)")
    @GetMapping("/list-muc-dich-gui")
    public ResponseEntity<ResponseData<Map<String, String>>> listMucDichGui() {
        return ResponseData.okEntity(zssMauZNZService.listMucDichGui());
    }

    @PreAuthorize("hasRole(T(com.ngv.zns_service.constant.Roles).USER_VIEW)")
    @GetMapping("/list-loai-button")
    public ResponseEntity<ResponseData<Map<String, String>>> listLoaiButton() {
        return ResponseData.okEntity(zssMauZNZService.listLoaiButton());
    }

    @PreAuthorize("hasRole(T(com.ngv.zns_service.constant.Roles).USER_VIEW)")
    @GetMapping("/list-gia-tri-tso")
    public ResponseEntity<ResponseData<Map<String, String>>> listGiaTriTSo() {
        return ResponseData.okEntity(zssMauZNZService.listGiaTriTSo());
    }

    // @PreAuthorize("hasRole(T(com.ngv.zns_service.constant.Roles).USER_VIEW)")
    @GetMapping("/sync-template-zns")
    public ResponseEntity<ResponseData<List<MauZNSResponse>>> syncTemplateZns() throws ValidationException {
        String maDvi = getClientId();
        return ResponseData.okEntity(zssMauZNZService.syncTemplateZns(maDvi));
    }

    @PreAuthorize("hasRole(T(com.ngv.zns_service.constant.Roles).USER_VIEW)")
    @PostMapping("/sync-template-detail-zns")
    public ResponseEntity<ResponseData<Void>> syncTemplateDetailZns(@RequestHeader String oaId, @RequestParam String templateId) throws ValidationException{
        zssMauZNZService.syncTemplateDetailZns(oaId, templateId);
        return ResponseData.createdEntity();
    }

    @PreAuthorize("hasRole(T(com.ngv.zns_service.constant.Roles).USER_VIEW)")
    @PostMapping("/sync-all-template-zns")
    public ResponseEntity<ResponseData<Void>> syncAllTemplateZns(
            @RequestHeader String oaId,
            @RequestParam(defaultValue = "0") int offset,
            @RequestParam(defaultValue = "100") int limit,
            @RequestParam(defaultValue = "0") int status
    ) throws ValidationException {
        zssMauZNZService.syncAllTemplateZns(oaId, offset, limit, status);
        return ResponseData.createdEntity();
    }

    @PostMapping("/create")
    public ResponseEntity<ResponseData<Void>> createMauZNS(@RequestBody MauZNSRequest request) {
        zssMauZNZService.createMauZns(request);
        return ResponseData.createdEntity();
    }

    @PutMapping("/update")
    public ResponseEntity<ResponseData<Void>> updateMauZNS(@RequestBody MauZNSRequest request) {
        zssMauZNZService.updateMauZns(request);
        return ResponseData.okEntity(null);
    }

    @GetMapping("/detail")
    public ResponseEntity<ResponseData<MauZNSResponse>> detailMauZNS(@RequestParam String maMau) throws ValidationException {
        return ResponseData.okEntity(zssMauZNZService.mauZnsDetail(maMau));
    }

    @DeleteMapping("/delete")
    public ResponseEntity<ResponseData<Void>> deleteMauZNS(@RequestParam String maMau) throws ValidationException {
        zssMauZNZService.deleteMauZns(maMau);
        return ResponseData.noContentEntity();
    }

    @PostMapping("/send-approval")
    public ResponseEntity<com.ngv.zns_service.model.ResponseData<Object>> sendApproval(
            @RequestBody MauZNSRequest request) throws IOException {
        ObjectNode res = zssMauZNZService.sendApproval(request);
        return ResponseUtils.success(res.get("error").asInt(), res.get("message").asText(), res.get("data"));
    }

    @PutMapping("/update-tso")
    public ResponseEntity<ResponseData<Void>> updateMaCtaoGtriTso(@RequestBody ZssMauZnsTsoRequest request) {
        zssMauZNZService.updateMaCtaoGtriTso(request);
        return ResponseData.okEntity(null);
    }

    @PreAuthorize("hasRole(T(com.ngv.zns_service.constant.Roles).USER_VIEW)")
    @GetMapping("/preview-mau")
    public ResponseEntity<ResponseData<String>> previewMau(@RequestParam String maMau) throws ValidationException {
        String link = zssMauZNZService.previewMau(maMau);
        return ResponseData.okEntity(link);
    }
}
