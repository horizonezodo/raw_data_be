package com.ngv.zns_service.dto.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ZSSNDungRequest {
    private String maZns;
    private String zoaId;
    private String jsonTso;
    private String maCdich;
    private String maDvi;
    private String maDvu;
    private String maKhhang;
    private String maMau;
    private String maNdung;
    private String maPhhoi;
    private String ndung;
    private String ngayNhap;
    private String ngaySua;
    private String nguoiNhap;
    private String nguoiSua;
    private String taiKhoanZalo;
    private String tenKhhang;
    private String tgianGuiDukien;
    private String tgianGuiTcong;
    private String tgianTao;
    private String tthaiGuizns;
    private String tthaiNvu;
}
