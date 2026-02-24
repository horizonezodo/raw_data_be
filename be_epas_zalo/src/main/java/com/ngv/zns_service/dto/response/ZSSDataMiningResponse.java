package com.ngv.zns_service.dto.response;

import java.math.BigDecimal;

import lombok.Data;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ZSSDataMiningResponse {
    private String maZns;
    private String maDvi;
    private String tenDvi;
    private String maDvu;
    private String tenDvu;
    private String nDung;
    private String tkhoanNhan;
    private String maKhachHangNhan;
    private String tenKhachHangNhan;
    private String tgianTao;
    private String tgianGuiTcong;
    private String tthaiGuizns;
    private BigDecimal donGia;
}
