package com.ngv.zns_service.controller;

import com.ngv.zns_service.dto.request.ZSSDvuRequest;
import com.ngvgroup.bpm.core.common.dto.ResponseData;
import com.ngv.zns_service.dto.response.dvu.*;
import com.ngv.zns_service.exception.ValidationException;
import com.ngv.zns_service.repository.ZSSDvuRepository;
import com.ngv.zns_service.service.ZSSDvuService;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/danh-sach-dvu")
public class ZSSDvuController {
    private final ZSSDvuService zssDvuService;
    private final ZSSDvuRepository zssDvuRepository;

    public ZSSDvuController(ZSSDvuService zssDvuService, ZSSDvuRepository zssDvuRepository) {
        this.zssDvuService = zssDvuService;
        this.zssDvuRepository = zssDvuRepository;
    }

    @PreAuthorize("hasRole(T(com.ngv.zns_service.constant.Roles).USER_VIEW)")
    @PostMapping("/get-list")
    public ResponseEntity<?> searchAll(@RequestBody ZSSDvuRequest request) {
        return ResponseData.okEntity(zssDvuService.getList(request));
    }

    @DeleteMapping("/delete")
    public ResponseEntity<?> delete(@RequestBody List<String> ids) throws ValidationException {
        zssDvuService.deleteIds(ids);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @PostMapping("/download")
    public ResponseEntity<byte[]> downloadExcel(@RequestBody ZSSDvuRequest request) throws IOException {
        byte[] excelData = zssDvuService.exportExcel(request);

        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=DichVu.xlsx")
                .contentType(MediaType.APPLICATION_OCTET_STREAM)
                .body(excelData);
    }

    @GetMapping("/list-loaidvu")
    public ResponseEntity<?> getLoaiDvu(@RequestParam("maLoaiDvu") String maLoaiDvu) {
        return ResponseData.okEntity(zssDvuService.listLoaiDvu(maLoaiDvu));
    }

    @PostMapping("/get-detail")
    public ResponseEntity<?> detail(@RequestParam("maDichVu") String maDichVu) {
        ChiTietDvuDto dto = zssDvuService.getDetail(maDichVu);
        return new ResponseEntity<>(dto, HttpStatus.OK);
    }

    @PostMapping("/create")
    public ResponseEntity<?> create(@RequestBody ChiTietDvuDto request) throws ValidationException {
        ChiTietDvuDto dto = zssDvuService.create(request);
        return new ResponseEntity<>(dto, HttpStatus.OK);
    }

    @PostMapping("/update")
    public ResponseEntity<?> update(@RequestBody ChiTietDvuDto request) throws ValidationException {
        ChiTietDvuDto dto = zssDvuService.update(request);
        return new ResponseEntity<>(dto, HttpStatus.OK);
    }

    // get-all mapping gói dịch vụ
    @GetMapping("/mapping-gdv")
    public ResponseEntity<?> mappingGdv() {
        List<CtDvMapGdvDto> mapGdvDtos = zssDvuService.listMapGdvDtos();
        return ResponseData.okEntity(mapGdvDtos);
    }

    // get-all mapping giao dich
    @GetMapping("/mapping-gd")
    public ResponseEntity<?> mappingGd() {
        List<CtDvMapGdDto> mapGdvDtos = zssDvuService.listMapGdDtos();
        return ResponseData.okEntity(mapGdvDtos);
    }

    // HÌNH THỨC GỬI, TGIAN GỬI
    @GetMapping("/list-common")
    public ResponseEntity<?> mappingGd(@RequestParam("mdm") String mdm, @RequestParam("mdmct") String mdmct) {
        List<ZSSLoaiDvuDto> listCommon = zssDvuService.listCommon(mdm, mdmct);
        return ResponseData.okEntity(listCommon);
    }

    @GetMapping("/list-dvi")
    public ResponseEntity<?> listDvi() {
        return ResponseData.okEntity(this.zssDvuRepository.listTen());
    }

    @GetMapping("/list-zoaId")
    public ResponseEntity<?> listZoaId(@RequestParam("maDvi") String maDvi) {
        return ResponseData.okEntity(this.zssDvuRepository.listZoaId(maDvi));
    }

    @GetMapping("/list-maMau")
    public ResponseEntity<?> listMaMau(@RequestParam("zoaId") String zoaId) {
        return ResponseData.okEntity(this.zssDvuRepository.listTenMau(zoaId));
    }

    @PostMapping("/create-temp")
    public ResponseEntity<?> createTemp(@RequestBody CtDvMapTempDto dto) {
        CtDvMapTempDto res = zssDvuService.saveTemp(dto);
        return ResponseData.okEntity(res);
    }

    @GetMapping("/grouped")
    public ResponseEntity<ResponseData<Map<String, List<Map<String, String>>>>> getGroupedDvu() {
        return ResponseData.okEntity(zssDvuService.getGroupedDvu());
    }
}
