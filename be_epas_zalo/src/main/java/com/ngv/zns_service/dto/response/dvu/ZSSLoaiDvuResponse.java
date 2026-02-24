package com.ngv.zns_service.dto.response.dvu;

import lombok.Data;

@Data
public class ZSSLoaiDvuResponse {
    private String maLoaiDvu;
    private String tenLoaiDvu;

    public ZSSLoaiDvuResponse(String maLoaiDvu, String tenLoaiDvu) {
        this.maLoaiDvu = maLoaiDvu;
        this.tenLoaiDvu = tenLoaiDvu;
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
