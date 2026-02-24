package com.ngv.zns_service.dto.response.goiDv;

import lombok.*;

@Data
public class GoiDvuDetailMappingDvuDto {
    private String maDvu;
    private String tenDvu;
    private String maLoaiDvu;
    private String tenLoaiDvu;

    public GoiDvuDetailMappingDvuDto() {
    }

    public GoiDvuDetailMappingDvuDto(String maDvu, String tenDvu, String maLoaiDvu, String tenLoaiDvu) {
        this.maDvu = maDvu;
        this.tenDvu = tenDvu;
        this.maLoaiDvu = maLoaiDvu;
        this.tenLoaiDvu = tenLoaiDvu;
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
}
