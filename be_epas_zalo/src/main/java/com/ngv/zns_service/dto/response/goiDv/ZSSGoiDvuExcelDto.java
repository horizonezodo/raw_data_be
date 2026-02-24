package com.ngv.zns_service.dto.response.goiDv;

import lombok.Data;

@Data

public class ZSSGoiDvuExcelDto {
    private String maGoiDvu;
    private String tenGoiDvu;
    private String mota;
    private String tthaiNvu;
    private String ngayNhap;

    public ZSSGoiDvuExcelDto() {
    }

    public ZSSGoiDvuExcelDto(String maGoiDvu, String tenGoiDvu, String mota, String tthaiNvu, String ngayNhap) {
        this.maGoiDvu = maGoiDvu;
        this.tenGoiDvu = tenGoiDvu;
        this.mota = mota;
        this.tthaiNvu = tthaiNvu;
        this.ngayNhap = ngayNhap;
    }

    public String getMaGoiDvu() {
        return maGoiDvu;
    }

    public void setMaGoiDvu(String maGoiDvu) {
        this.maGoiDvu = maGoiDvu;
    }

    public String getTenGoiDvu() {
        return tenGoiDvu;
    }

    public void setTenGoiDvu(String tenGoiDvu) {
        this.tenGoiDvu = tenGoiDvu;
    }

    public String getMota() {
        return mota;
    }

    public void setMota(String mota) {
        this.mota = mota;
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
