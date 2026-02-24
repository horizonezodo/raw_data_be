package com.ngv.zns_service.dto.response.temp;

import lombok.Data;

@Data
public class TempTenDviDto {
    private String maDvi;
    private String tenDvi;

    public TempTenDviDto(String maDvi, String tenDvi) {
        this.maDvi = maDvi;
        this.tenDvi = tenDvi;
    }

    public String getMaDvi() {
        return maDvi;
    }

    public void setMaDvi(String maDvi) {
        this.maDvi = maDvi;
    }

    public String getTenDvi() {
        return tenDvi;
    }

    public void setTenDvi(String tenDvi) {
        this.tenDvi = tenDvi;
    }
}
