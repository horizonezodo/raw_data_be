package com.ngv.zns_service.dto.request;

import lombok.Data;
import org.springframework.data.domain.Pageable;

@Data
public class ZSSDvuRequest {
    private String loaiDvu;
    private String trangThaiDvu;
    private Pageable pageable;

    public ZSSDvuRequest() {
    }

    public ZSSDvuRequest(String loaiDvu, String trangThaiDvu, Pageable pageable) {
        this.loaiDvu = loaiDvu;
        this.trangThaiDvu = trangThaiDvu;
        this.pageable = pageable;
    }

    public String getLoaiDvu() {
        return loaiDvu;
    }

    public void setLoaiDvu(String loaiDvu) {
        this.loaiDvu = loaiDvu;
    }

    public String getTrangThaiDvu() {
        return trangThaiDvu;
    }

    public void setTrangThaiDvu(String trangThaiDvu) {
        this.trangThaiDvu = trangThaiDvu;
    }

    public Pageable getPageable() {
        return pageable;
    }

    public void setPageable(Pageable pageable) {
        this.pageable = pageable;
    }
}
