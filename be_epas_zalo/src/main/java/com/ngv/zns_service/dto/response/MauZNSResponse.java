package com.ngv.zns_service.dto.response;

import com.ngv.zns_service.dto.request.CaiDatTSoRequest;
import com.ngv.zns_service.dto.request.MauBangRequest;
import com.ngv.zns_service.dto.request.MauButtonRequest;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.Base64;
import java.util.List;

@Data
@NoArgsConstructor
public class MauZNSResponse {

    private String maDvi;
    private String tenDvi;
    private String zoaId;
    private String maMau;
    private String tenMau;
    private String loaiZns;
    private String maLoaiMau;
    private BigDecimal donGia;
    private String loaiHanh;
    private byte[] logoSang;
    private String tenLogoSang;
    private byte[] logoToi;
    private String tenLogoToi;
    private byte[] anh1;
    private String tenAnh1;
    private byte[] anh2;
    private String tenAnh2;
    private byte[] anh3;
    private String tenAnh3;
    private String tdeMau;
    private String ndungTde;
    private String vban1;
    private String vban2;
    private String vban3;
    private String vban4;
    private String tgianCho;
    private String tthaiZns;
    private List<MauBangRequest> mauBangs;
    private List<MauButtonRequest> mauButtons;
    private List<CaiDatTSoRequest> tSoRequests;
    private String ghiChu;
    private String lket;
    private String maMauDtac;
    private String maNganHang;
    private String tenTKhoan;
    private String stk;
    private String soTien;
    private String noiDungCKhoan;
    private BigDecimal timeout;

    public MauZNSResponse(String maDvi, String tenDvi, String zoaId, String maMau, String tenMau, String loaiZns,
            String maLoaiMau, BigDecimal donGia, String loaiHanh, byte[] logoSang, String tenLogoSang, byte[] logoToi,
            String tenLogoToi, byte[] anh1, String tenAnh1, byte[] anh2, String tenAnh2, byte[] anh3, String tenAnh3,
            String tdeMau, String ndungTde, String vban1, String vban2, String vban3, String vban4, String ghiChu,
            String maMauDtac, BigDecimal timeout) {
        this.maDvi = maDvi;
        this.tenDvi = tenDvi;
        this.zoaId = zoaId;
        this.maMau = maMau;
        this.tenMau = tenMau;
        this.loaiZns = loaiZns;
        this.maLoaiMau = maLoaiMau;
        this.donGia = donGia;
        this.loaiHanh = loaiHanh;
        this.logoSang = logoSang;
        this.tenLogoSang = tenLogoSang;
        this.logoToi = logoToi;
        this.tenLogoToi = tenLogoToi;
        this.anh1 = anh1;
        this.tenAnh1 = tenAnh1;
        this.anh2 = anh2;
        this.tenAnh2 = tenAnh2;
        this.anh3 = anh3;
        this.tenAnh3 = tenAnh3;
        this.tdeMau = tdeMau;
        this.ndungTde = ndungTde;
        this.vban1 = vban1;
        this.vban2 = vban2;
        this.vban3 = vban3;
        this.vban4 = vban4;
        this.ghiChu = ghiChu;
        this.maMauDtac = maMauDtac;
        this.timeout = timeout;
    }

    public MauZNSResponse(String maDvi, String tenDvi, String zoaId, String maMau, String tenMau, String loaiZns,
            String maLoaiMau, BigDecimal donGia, String loaiHanh, byte[] logoSang, String tenLogoSang, byte[] logoToi,
            String tenLogoToi, byte[] anh1, String tenAnh1, byte[] anh2, String tenAnh2, byte[] anh3, String tenAnh3,
            String tdeMau, String ndungTde, String vban1, String vban2, String vban3, String vban4, String tgianCho,
            String tthaiZns, String ghiChu, String lket, String maMauDtac, BigDecimal timeout) {
        this.maDvi = maDvi;
        this.tenDvi = tenDvi;
        this.zoaId = zoaId;
        this.maMau = maMau;
        this.tenMau = tenMau;
        this.loaiZns = loaiZns;
        this.maLoaiMau = maLoaiMau;
        this.donGia = donGia;
        this.loaiHanh = loaiHanh;
        this.logoSang = logoSang;
        this.tenLogoSang = tenLogoSang;
        this.logoToi = logoToi;
        this.tenLogoToi = tenLogoToi;
        this.anh1 = anh1;
        this.tenAnh1 = tenAnh1;
        this.anh2 = anh2;
        this.tenAnh2 = tenAnh2;
        this.anh3 = anh3;
        this.tenAnh3 = tenAnh3;
        this.tdeMau = tdeMau;
        this.ndungTde = ndungTde;
        this.vban1 = vban1;
        this.vban2 = vban2;
        this.vban3 = vban3;
        this.vban4 = vban4;
        this.tgianCho = tgianCho;
        this.tthaiZns = tthaiZns;
        this.ghiChu = ghiChu;
        this.lket = lket;
        this.maMauDtac = maMauDtac;
        this.timeout = timeout;
    }

    public MauZNSResponse(String maDvi, String tenDvi, String zoaId, String maMau, String tenMau, String loaiZns,
            String maLoaiMau, BigDecimal donGia, String loaiHanh, byte[] logoSang, String tenLogoSang, byte[] logoToi,
            String tenLogoToi, byte[] anh1, String tenAnh1, byte[] anh2, String tenAnh2, byte[] anh3, String tenAnh3,
            String tdeMau, String ndungTde, String vban1, String vban2, String vban3, String vban4, String tgianCho,
            String tthaiZns, String ghiChu, String lket, String maMauDtac, String maNganHang, String tenTKhoan,
            String stk, String soTien, String noiDungCKhoan) {
        this.maDvi = maDvi;
        this.tenDvi = tenDvi;
        this.zoaId = zoaId;
        this.maMau = maMau;
        this.tenMau = tenMau;
        this.loaiZns = loaiZns;
        this.maLoaiMau = maLoaiMau;
        this.donGia = donGia;
        this.loaiHanh = loaiHanh;
        this.logoSang = logoSang;
        this.tenLogoSang = tenLogoSang;
        this.logoToi = logoToi;
        this.tenLogoToi = tenLogoToi;
        this.anh1 = anh1;
        this.tenAnh1 = tenAnh1;
        this.anh2 = anh2;
        this.tenAnh2 = tenAnh2;
        this.anh3 = anh3;
        this.tenAnh3 = tenAnh3;
        this.tdeMau = tdeMau;
        this.ndungTde = ndungTde;
        this.vban1 = vban1;
        this.vban2 = vban2;
        this.vban3 = vban3;
        this.vban4 = vban4;
        this.tgianCho = tgianCho;
        this.tthaiZns = tthaiZns;
        this.ghiChu = ghiChu;
        this.lket = lket;
        this.maMauDtac = maMauDtac;
        this.maNganHang = maNganHang;
        this.tenTKhoan = tenTKhoan;
        this.stk = stk;
        this.soTien = soTien;
        this.noiDungCKhoan = noiDungCKhoan;
    }

    public String getLogoSang() {
        return encodeBase64(logoSang);
    }

    public String getLogoToi() {
        return encodeBase64(logoToi);
    }

    public String getAnh1() {
        return encodeBase64(anh1);
    }

    public String getAnh2() {
        return encodeBase64(anh2);
    }

    public String getAnh3() {
        return encodeBase64(anh3);
    }

    private String encodeBase64(byte[] data) {
        return (data != null) ? Base64.getEncoder().encodeToString(data) : null;
    }
}
