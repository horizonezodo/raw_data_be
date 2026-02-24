package com.ngv.zns_service.dto.response.dvu;

import lombok.Data;

@Data
public class ZSSLoaiDvuDto {
    private String maDmucCtiet;
    private String tenDmucCtiet;

    public ZSSLoaiDvuDto() {
    }

    public ZSSLoaiDvuDto(String maDmucCtiet, String tenDmucCtiet) {
        this.maDmucCtiet = maDmucCtiet;
        this.tenDmucCtiet = tenDmucCtiet;
    }

    public String getMaDmucCtiet() {
        return maDmucCtiet;
    }

    public void setMaDmucCtiet(String maDmucCtiet) {
        this.maDmucCtiet = maDmucCtiet;
    }

    public String getTenDmucCtiet() {
        return tenDmucCtiet;
    }

    public void setTenDmucCtiet(String tenDmucCtiet) {
        this.tenDmucCtiet = tenDmucCtiet;
    }
}
