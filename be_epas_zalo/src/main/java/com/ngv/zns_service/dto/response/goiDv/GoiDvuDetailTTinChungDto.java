package com.ngv.zns_service.dto.response.goiDv;
import lombok.*;

@Data
public class GoiDvuDetailTTinChungDto {
    private String maGoiDvu;
    private String tenGoiDvu;
    private String tThaiNvu;
    private String moTa;

    public GoiDvuDetailTTinChungDto() {
    }

    public GoiDvuDetailTTinChungDto(String maGoiDvu, String tenGoiDvu, String tThaiNvu, String moTa) {
        this.maGoiDvu = maGoiDvu;
        this.tenGoiDvu = tenGoiDvu;
        this.tThaiNvu = tThaiNvu;
        this.moTa = moTa;
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

    public String gettThaiNvu() {
        return tThaiNvu;
    }

    public void settThaiNvu(String tThaiNvu) {
        this.tThaiNvu = tThaiNvu;
    }

    public String getMoTa() {
        return moTa;
    }

    public void setMoTa(String moTa) {
        this.moTa = moTa;
    }
}
