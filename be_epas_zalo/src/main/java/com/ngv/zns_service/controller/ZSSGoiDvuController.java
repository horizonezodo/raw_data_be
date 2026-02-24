package com.ngv.zns_service.controller;


import com.ngv.zns_service.dto.request.ZSSGoiDvuRequest;
import com.ngvgroup.bpm.core.common.dto.ResponseData;
import com.ngvgroup.bpm.core.persistence.service.storedprocedure.BaseStoredProcedureService;
import com.ngv.zns_service.dto.response.goiDv.ZSSGoiDvuDetailDto;
import com.ngv.zns_service.dto.response.goiDv.ZSSTthaiHdongDto;
import com.ngv.zns_service.exception.ValidationException;
import com.ngv.zns_service.service.ZSSGoiDvuService;


import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/goi-dvu")
public class ZSSGoiDvuController extends BaseStoredProcedureService {
    private final ZSSGoiDvuService zssGoiDvuService;
    


    @GetMapping("/sync-data")
    public ResponseEntity<?> getSync() {
        String maDvi = getClientId();
        return ResponseData.okEntity(zssGoiDvuService.syncData(maDvi));
    }

    @PreAuthorize("hasRole(T(com.ngv.zns_service.constant.Roles).USER_VIEW)")
    @PostMapping("/get-all")
    public ResponseEntity<?> getListGoiDvu(@RequestBody ZSSGoiDvuRequest request) {
        return ResponseData.okEntity(zssGoiDvuService.listGoiDvu(request.getPageable()));
    }

    @PostMapping("/create")
    public ResponseEntity<?> create(@RequestBody ZSSGoiDvuDetailDto dto) throws ValidationException {
            return ResponseData.okEntity(zssGoiDvuService.create(dto));
    }

    @PostMapping("/update")
    public ResponseEntity<?> update(@RequestBody ZSSGoiDvuDetailDto dto) throws ValidationException {
            return ResponseData.okEntity(zssGoiDvuService.update(dto));
    }

    @DeleteMapping("/delete")
    public ResponseEntity<?> delete(@RequestBody List<String> ids) throws ValidationException {
            zssGoiDvuService.deleteIds(ids);
            return new ResponseEntity<>(HttpStatus.OK);
    }

    @GetMapping("/get-detail")
    public ResponseEntity<?> detail(@RequestParam("mgdv") String mgdv) {
            ZSSGoiDvuDetailDto dto = zssGoiDvuService.getDetail(mgdv);
            return new ResponseEntity<>(dto, HttpStatus.OK);
    }

    @GetMapping("/list-tthaihd")
    public ResponseEntity<?> listTthaiHd(@RequestParam("mdm") String mdm) {
        List<ZSSTthaiHdongDto>  dtos = zssGoiDvuService.listTthaiHd(mdm);
        return new ResponseEntity<>(dtos, HttpStatus.OK);
    }

    @GetMapping("/download")
    public ResponseEntity<byte[]> downloadExcel() throws IOException {
        byte[] excelData = zssGoiDvuService.exportExcel();

        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=GoiDichVu.xlsx")
                .contentType(MediaType.APPLICATION_OCTET_STREAM)
                .body(excelData);
    }
}
