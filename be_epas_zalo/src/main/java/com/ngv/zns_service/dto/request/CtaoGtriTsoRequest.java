package com.ngv.zns_service.dto.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.RequiredArgsConstructor;

@Data
@RequiredArgsConstructor
@AllArgsConstructor
public class CtaoGtriTsoRequest {
    private String cthucCtaoGtriTso;
    private String tenCtaoGtriTso;
    private String loaiGiaTri;
    private String mdichSdung;
}
