package com.ngv.zns_service.dto.response.dvu;

import lombok.Data;

@Data
public class ZSSDvuExcelDto {
    private String maDvu;
    private String tenDvu;
    private String maLoaiDvu;
    private String tenLoaiDvu;
    private String tthaiNvu;
    private String ngayNhap;

    public ZSSDvuExcelDto() {
    }

    public ZSSDvuExcelDto(String maDvu, String tenDvu, String maLoaiDvu, String tenLoaiDvu, String tthaiNvu, String ngayNhap) {
        this.maDvu = maDvu;
        this.tenDvu = tenDvu;
        this.maLoaiDvu = maLoaiDvu;
        this.tenLoaiDvu = tenLoaiDvu;
        this.tthaiNvu = tthaiNvu;
        this.ngayNhap = ngayNhap;
    }

    public String getMaDvu() {
        return maDvu;
    }

    public void setMaDvu(String maDvu) {
        this.maDvu = maDvu;
    }

    public String getTenDvu() {
        return tenDvu;
    }

    public void setTenDvu(String tenDvu) {
        this.tenDvu = tenDvu;
    }

    public String getMaLoaiDvu() {
        return maLoaiDvu;
    }

    public void setMaLoaiDvu(String maLoaiDvu) {
        this.maLoaiDvu = maLoaiDvu;
    }

    public String getTenLoaiDvu() {
        return tenLoaiDvu;
    }

    public void setTenLoaiDvu(String tenLoaiDvu) {
        this.tenLoaiDvu = tenLoaiDvu;
    }

    public String getTthaiNvu() {
        return tthaiNvu;
    }

    public void setTthaiNvu(String tthaiNvu) {
        this.tthaiNvu = tthaiNvu;
    }

    public String getNgayNhap() {
        return ngayNhap;
    }

    public void setNgayNhap(String ngayNhap) {
        this.ngayNhap = ngayNhap;
    }
}
