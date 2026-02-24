package com.ngv.zns_service.dto.response.goiDv;

import lombok.Data;

@Data
public class ZSSTthaiHdongDto {
    private String maDmucCtiet;
    private String tenDmucCtiet;

    public ZSSTthaiHdongDto() {
    }

    public ZSSTthaiHdongDto(String maDmucCtiet, String tenDmucCtiet) {
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
