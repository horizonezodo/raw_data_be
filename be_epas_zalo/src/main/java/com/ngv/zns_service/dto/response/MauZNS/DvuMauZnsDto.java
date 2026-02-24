package com.ngv.zns_service.dto.response.MauZNS;

import lombok.Data;

@Data
public class DvuMauZnsDto {
    private String zoaId;
    private String maDvu;
    private String maMau;
    private String nguoiNhap;
    private String ngayNhap;
    private String nguoiSua;
    private String ngaySua;
    private String maDvi;

    public DvuMauZnsDto() {
    }

    public DvuMauZnsDto(String zoaId, String maDvu, String maMau, String nguoiNhap, String ngayNhap, String nguoiSua,
                        String ngaySua, String maDvi) {
        this.zoaId = zoaId;
        this.maDvu = maDvu;
        this.maMau = maMau;
        this.nguoiNhap = nguoiNhap;
        this.ngayNhap = ngayNhap;
        this.nguoiSua = nguoiSua;
        this.ngaySua = ngaySua;
        this.maDvi = maDvi;
    }

    public String getZoaId() {
        return zoaId;
    }

    public void setZoaId(String zoaId) {
        this.zoaId = zoaId;
    }

    public String getMaDvu() {
        return maDvu;
    }

    public void setMaDvu(String maDvu) {
        this.maDvu = maDvu;
    }

    public String getMaMau() {
        return maMau;
    }

    public void setMaMau(String maMau) {
        this.maMau = maMau;
    }

    public String getNguoiNhap() {
        return nguoiNhap;
    }

    public void setNguoiNhap(String nguoiNhap) {
        this.nguoiNhap = nguoiNhap;
    }

    public String getNgayNhap() {
        return ngayNhap;
    }

    public void setNgayNhap(String ngayNhap) {
        this.ngayNhap = ngayNhap;
    }

    public String getNguoiSua() {
        return nguoiSua;
    }

    public void setNguoiSua(String nguoiSua) {
        this.nguoiSua = nguoiSua;
    }

    public String getNgaySua() {
        return ngaySua;
    }

    public void setNgaySua(String ngaySua) {
        this.ngaySua = ngaySua;
    }

    public String getMaDvi() {
        return maDvi;
    }

    public void setMaDvi(String maDvi) {
        this.maDvi = maDvi;
    }
}
