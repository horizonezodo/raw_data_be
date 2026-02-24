package com.ngv.zns_service.dto.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CaiDatTSoRequest {
    private String maMau;
    private String tenTso;
    private String maCtaoGtriTso;
    private String paramType;
    private String ndungTso;
    private String bbuoc;
    private String kieuTso;
    private BigDecimal soKtuTda;
    private BigDecimal soKtuTthieu;
    private String nhanGtriNull;
    private List<CtaoGtriTsoRequest> ctaoGtriTsoRequests;

    public CaiDatTSoRequest(String maMau, String tenTso, String maCtaoGtriTso, String paramType, String ndungTso, String bbuoc, String kieuTso, BigDecimal soKtuTda, BigDecimal soKtuTthieu, String nhanGtriNull) {
        this.maMau = maMau;
        this.tenTso = tenTso;
        this.maCtaoGtriTso = maCtaoGtriTso;
        this.paramType = paramType;
        this.ndungTso = ndungTso;
        this.bbuoc = bbuoc;
        this.kieuTso = kieuTso;
        this.soKtuTda = soKtuTda;
        this.soKtuTthieu = soKtuTthieu;
        this.nhanGtriNull = nhanGtriNull;
    }
}
