package com.ngv.zns_service.dto.response.temp;

import lombok.Data;

@Data
public class TempMaMauDto {
    private String maMau;
    private String tenMau;

    public TempMaMauDto(String maMau, String tenMau) {
        this.maMau = maMau;
        this.tenMau = tenMau;
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
