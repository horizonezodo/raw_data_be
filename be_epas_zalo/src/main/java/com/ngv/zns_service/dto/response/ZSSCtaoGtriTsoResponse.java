package com.ngv.zns_service.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ZSSCtaoGtriTsoResponse {
    private String maCtaoGtriTso;
    private String tenCtaoGtriTso;
    private String cthucCtaoGtriTso;
    private String loaiGiaTri;
    private String mdichSdung;

    public ZSSCtaoGtriTsoResponse(String maCtaoGtriTso, String tenCtaoGtriTso) {
        this.maCtaoGtriTso = maCtaoGtriTso;
        this.tenCtaoGtriTso = tenCtaoGtriTso;
    }
}
