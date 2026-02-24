package com.ngv.zns_service.dto.response.dvu;

import lombok.Data;

@Data
// Mapping giao dịch
public class CtDvMapGdDto {
    private String maLoaiGdich;
    private String tenLoaiGdich;

    public CtDvMapGdDto() {
    }

    public CtDvMapGdDto(String maLoaiGdich, String tenLoaiGdich) {
        this.maLoaiGdich = maLoaiGdich;
        this.tenLoaiGdich = tenLoaiGdich;
    }

    public String getMaLoaiGdich() {
        return maLoaiGdich;
    }

    public void setMaLoaiGdich(String maLoaiGdich) {
        this.maLoaiGdich = maLoaiGdich;
    }

    public String getTenLoaiGdich() {
        return tenLoaiGdich;
    }

    public void setTenLoaiGdich(String tenLoaiGdich) {
        this.tenLoaiGdich = tenLoaiGdich;
    }

}
