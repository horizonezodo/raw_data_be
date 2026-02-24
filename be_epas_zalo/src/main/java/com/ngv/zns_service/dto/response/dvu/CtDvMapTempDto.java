package com.ngv.zns_service.dto.response.dvu;

import lombok.Data;

@Data
// Mapping mẫu biểu
public class CtDvMapTempDto {
    private String maDvi;
    private String tenDvi;
    private String zoaId;
    private String maMau;
    private String tenMau;

    public CtDvMapTempDto(String maDvi, String tenDvi, String zoaId, String maMau, String tenMau) {
        this.maDvi = maDvi;
        this.tenDvi = tenDvi;
        this.zoaId = zoaId;
        this.maMau = maMau;
        this.tenMau = tenMau;
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

    public String getZoaId() {
        return zoaId;
    }

    public void setZoaId(String zoaId) {
        this.zoaId = zoaId;
    }

    public String getMaMau() {
        return maMau;
    }

    public void setMaMau(String maMau) {
        this.maMau = maMau;
    }

    public String getTenMau() {
        return tenMau;
    }

    public void setTenMau(String tenMau) {
        this.tenMau = tenMau;
    }
}
