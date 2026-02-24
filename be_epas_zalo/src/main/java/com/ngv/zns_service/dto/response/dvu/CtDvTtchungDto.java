package com.ngv.zns_service.dto.response.dvu;

import lombok.Data;

@Data
// Thông tin chung
public class CtDvTtchungDto {
    private String maLoaiDvu;
    private String tenLoaiDvu;
    private String tthaiNvu;
    private String maDvu;
    private String tenDvu;
    private String dvuLQuan;
    private String mota;
    private String hthucGui;
    private String tgianGui;
    private String gioBdauGui;
    private String gioKthucGui;
    private Integer soLguiTbai;

    public CtDvTtchungDto() {
    }

    public CtDvTtchungDto(String maLoaiDvu, String tenLoaiDvu, String tthaiNvu, String maDvu, String tenDvu,
                          String dvuLQuan, String mota, String hthucGui, String tgianGui, String gioBdauGui,
                          String gioKthucGui, Integer soLguiTbai) {
        this.maLoaiDvu = maLoaiDvu;
        this.tenLoaiDvu = tenLoaiDvu;
        this.tthaiNvu = tthaiNvu;
        this.maDvu = maDvu;
        this.tenDvu = tenDvu;
        this.dvuLQuan = dvuLQuan;
        this.mota = mota;
        this.hthucGui = hthucGui;
        this.tgianGui = tgianGui;
        this.gioBdauGui = gioBdauGui;
        this.gioKthucGui = gioKthucGui;
        this.soLguiTbai = soLguiTbai;
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

    public String getTthaiNvu() {
        return tthaiNvu;
    }

    public void setTthaiNvu(String tthaiNvu) {
        this.tthaiNvu = tthaiNvu;
    }

    public String getMaDvu() {
        return maDvu;
    }

    public void setMaDvu(String maDvu) {
        this.maDvu = maDvu;
    }

    public String getTenDvu() {
        return tenDvu;
    }

    public void setTenDvu(String tenDvu) {
        this.tenDvu = tenDvu;
    }

    public String getDvuLQuan() {
        return dvuLQuan;
    }

    public void setDvuLQuan(String dvuLQuan) {
        this.dvuLQuan = dvuLQuan;
    }

    public String getMota() {
        return mota;
    }

    public void setMota(String mota) {
        this.mota = mota;
    }

    public String getHthucGui() {
        return hthucGui;
    }

    public void setHthucGui(String hthucGui) {
        this.hthucGui = hthucGui;
    }

    public String getTgianGui() {
        return tgianGui;
    }

    public void setTgianGui(String tgianGui) {
        this.tgianGui = tgianGui;
    }

    public String getGioBdauGui() {
        return gioBdauGui;
    }

    public void setGioBdauGui(String gioBdauGui) {
        this.gioBdauGui = gioBdauGui;
    }

    public String getGioKthucGui() {
        return gioKthucGui;
    }

    public void setGioKthucGui(String gioKthucGui) {
        this.gioKthucGui = gioKthucGui;
    }

    public Integer getSoLguiTbai() {
        return soLguiTbai;
    }

    public void setSoLguiTbai(Integer soLguiTbai) {
        this.soLguiTbai = soLguiTbai;
    }
}
