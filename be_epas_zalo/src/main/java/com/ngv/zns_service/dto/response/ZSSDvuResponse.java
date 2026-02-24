package com.ngv.zns_service.dto.response;

import lombok.Data;

@Data
public class ZSSDvuResponse {
    private String maDvu;
    private String tenDvu;
    private String maLoaiDvu;
    private String tenLoaiDvu;
    private String trangThai;
    private String ngayNhap;

    public ZSSDvuResponse() {
    }

    public ZSSDvuResponse(String maDvu, String tenDvu, String maLoaiDvu, String tenLoaiDvu, String trangThai, String ngayNhap) {
        this.maDvu = maDvu;
        this.tenDvu = tenDvu;
        this.maLoaiDvu = maLoaiDvu;
        this.tenLoaiDvu = tenLoaiDvu;
        this.trangThai = trangThai;
        this.ngayNhap = ngayNhap;
    }
}
