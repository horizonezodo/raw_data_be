package com.ngv.zns_service.dto.response.dvu;

import lombok.Data;

@Data
public class KeyTempDto {
    private String zoaId;
    private String maMau;

    public KeyTempDto(String zoaId, String maMau) {
        this.zoaId = zoaId;
        this.maMau = maMau;
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
}
