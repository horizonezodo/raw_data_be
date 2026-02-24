package com.ngv.zns_service.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ZSSMauZNZResponse {
    private String maDvi;
    private String tenDvi;
    private String zoaId;
    private String maMau;
    private String maMauDtac;
    private String tenMau;
    private String loaiZns;
    private String maCluongMau ;
    private BigDecimal donGia;
    private String tthaiNv;
    private String tthaiZns;
    private String lyDo;
}
