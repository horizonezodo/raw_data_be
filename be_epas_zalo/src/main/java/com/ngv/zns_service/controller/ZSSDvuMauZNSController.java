package com.ngv.zns_service.controller;


import com.ngvgroup.bpm.core.common.dto.ResponseData;
import com.ngvgroup.bpm.core.persistence.service.storedprocedure.BaseStoredProcedureService;
import com.ngv.zns_service.service.ZSSDvuMauZNSService;


import lombok.RequiredArgsConstructor;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/dvu-mau-zns")
@RequiredArgsConstructor
public class ZSSDvuMauZNSController extends BaseStoredProcedureService {
    private final ZSSDvuMauZNSService zssDvuMauZNSService;
    

    @GetMapping("/sync-data")
    public ResponseEntity<?> getSync() {
        String maDvi = getClientId();
        return ResponseData.okEntity(zssDvuMauZNSService.syncData(maDvi));
    }
}
