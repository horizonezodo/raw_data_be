package com.ngv.zns_service.dto.request;

import lombok.*;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Data
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class MauZNSRequest {
    private String maDvi;

    private String tenDvi;

    private String zoaId;

    private String maMau;

    private String tenMau;

    private String loaiZns;

    private String maLoaiMau;

    private BigDecimal donGia;

    private String loaiHanh;

    private String logoSang;

    private String tenLogoSang;

    private String logoToi;

    private String tenLogoToi;

    private String anh1;

    private String tenAnh1;

    private String anh2;

    private String tenAnh2;

    private String anh3;

    private String tenAnh3;

    private String tdeMau;

    private String ndungTde;

    private String vban1;

    private String vban2;

    private String vban3;

    private String vban4;

    private List<MauBangRequest> mauBangs = new ArrayList<>();

    private List<MauButtonRequest> mauButtons = new ArrayList<>();

    private List<CaiDatTSoRequest> caiDatTSo = new ArrayList<>();

    private String ghiChu;

    private String maNganHang;

    private String tenTKhoan;

    private String stk;

    private String soTien;

    private String noiDungCKhoan;

    private String tthaiZns;

    private String maCluongMau;

    private String maMauDtac;

    private String lyDo;

    private BigDecimal timeout;

    private String lket;
}
