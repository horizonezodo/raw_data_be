package com.ngv.zns_service.dto.response.dvu;

import lombok.Data;

@Data
// Mapping gói dịch vụ
public class CtDvMapGdvDto {
    private String maGoiDvu;
    private String tenGoiDvu;
    private String mota;
    private String tthaiNvu;
    private String ngayTao;

    public CtDvMapGdvDto() {
    }

    public CtDvMapGdvDto(String maGoiDvu, String tenGoiDvu, String mota, String tthaiNvu, String ngayTao) {
        this.maGoiDvu = maGoiDvu;
        this.tenGoiDvu = tenGoiDvu;
        this.mota = mota;
        this.tthaiNvu = tthaiNvu;
        this.ngayTao = ngayTao;
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

    public String getNgayTao() {
        return ngayTao;
    }

    public void setNgayTao(String ngayTao) {
        this.ngayTao = ngayTao;
    }
}
