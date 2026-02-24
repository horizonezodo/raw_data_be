package com.ngv.zns_service.dto.response.dvu;

import java.util.List;

import lombok.*;

@Data

public class ZSSDvuDto {
    private String maDvu;
    private String nguoiNhap;
    private String ngayNhap;
    private String nguoiSua;
    private String ngaySua;
    private String tenDvu;
    private String maLoaiDvu;
    private Integer soLguiTbai;
    private String hthucGui;
    private String tgianGui;
    private String gioBdauGui;
    private String gioKthucGui;
    private String suKien;
    private String dvuLquan;
    private String mota;
    private String tthaiNvu;
    private String maGoiDvu;
    private List<String> listMaLoaiGdich;

    public ZSSDvuDto() {
    }

    public ZSSDvuDto(String maDvu, String nguoiNhap, String ngayNhap, String nguoiSua, String ngaySua,
            String tenDvu, String maLoaiDvu, Integer soLguiTbai, String hthucGui, String tgianGui,
            String gioBdauGui, String gioKthucGui, String suKien, String dvuLquan, String mota, String tthaiNvu,
            String maGoiDvu) {
        this.maDvu = maDvu;
        this.nguoiNhap = nguoiNhap;
        this.ngayNhap = ngayNhap;
        this.nguoiSua = nguoiSua;
        this.ngaySua = ngaySua;
        this.tenDvu = tenDvu;
        this.maLoaiDvu = maLoaiDvu;
        this.soLguiTbai = soLguiTbai;
        this.hthucGui = hthucGui;
        this.tgianGui = tgianGui;
        this.gioBdauGui = gioBdauGui;
        this.gioKthucGui = gioKthucGui;
        this.suKien = suKien;
        this.dvuLquan = dvuLquan;
        this.mota = mota;
        this.tthaiNvu = tthaiNvu;
        this.maGoiDvu = maGoiDvu;
    }

    public String getMaDvu() {
        return maDvu;
    }

    public void setMaDvu(String maDvu) {
        this.maDvu = maDvu;
    }

    public String getNguoiNhap() {
        return nguoiNhap;
    }

    public void setNguoiNhap(String nguoiNhap) {
        this.nguoiNhap = nguoiNhap;
    }

    public String getNgayNhap() {
        return ngayNhap;
    }

    public void setNgayNhap(String ngayNhap) {
        this.ngayNhap = ngayNhap;
    }

    public String getNguoiSua() {
        return nguoiSua;
    }

    public void setNguoiSua(String nguoiSua) {
        this.nguoiSua = nguoiSua;
    }

    public String getNgaySua() {
        return ngaySua;
    }

    public void setNgaySua(String ngaySua) {
        this.ngaySua = ngaySua;
    }

    public String getTenDvu() {
        return tenDvu;
    }

    public void setTenDvu(String tenDvu) {
        this.tenDvu = tenDvu;
    }

    public String getMaLoaiDvu() {
        return maLoaiDvu;
    }

    public void setMaLoaiDvu(String maLoaiDvu) {
        this.maLoaiDvu = maLoaiDvu;
    }

    public Integer getSoLguiTbai() {
        return soLguiTbai;
    }

    public void setSoLguiTbai(Integer soLguiTbai) {
        this.soLguiTbai = soLguiTbai;
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

    public String getSuKien() {
        return suKien;
    }

    public void setSuKien(String suKien) {
        this.suKien = suKien;
    }

    public String getDvuLquan() {
        return dvuLquan;
    }

    public void setDvuLquan(String dvuLquan) {
        this.dvuLquan = dvuLquan;
    }

    public String getMota() {
        return mota;
    }

    public void setMota(String mota) {
        this.mota = mota;
    }

    public String getTthaiNvu() {
        return tthaiNvu;
    }

    public void setTthaiNvu(String tthaiNvu) {
        this.tthaiNvu = tthaiNvu;
    }

    public String getMaGoiDvu() {
        return maGoiDvu;
    }

    public void setMaGoiDvu(String maGoiDvu) {
        this.maGoiDvu = maGoiDvu;
    }

    public List<String> getListMaLoaiGdich() {
        return listMaLoaiGdich;
    }

    public void setListMaLoaiGdich(List<String> listMaLoaiGdich) {
        this.listMaLoaiGdich = listMaLoaiGdich;
    }
}
