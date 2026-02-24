package com.ngv.zns_service.model.entity;


import com.ngv.zns_service.model.pk.ZSSNDungLsPK;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@IdClass(ZSSNDungLsPK.class)
@Table(name = "ZSS_NDUNG_LS")
public class ZSSNDungLs {

    @Id
    @Column(name = "ZOA_ID", length = 32, nullable = false)
    private String zoaId;

    @Id
    @Column(name = "MA_ZNS", length = 32, nullable = false)
    private String maZns;


    @Column(name = "NGUOI_NHAP", length = 32, nullable = false)
    private String nguoiNhap;

    @Column(name = "NGAY_NHAP", length = 32, nullable = false)
    private String ngayNhap;

    @Column(name = "NGUOI_SUA", length = 32, nullable = false)
    private String nguoiSua;

    @Column(name = "NGAY_SUA", length = 32, nullable = false)
    private String ngaySua;

    @Column(name = "MA_DVI", length = 32, nullable = false)
    private String maDvi;

    @Column(name = "TAI_KHOAN_ZALO", length = 16, nullable = false)
    private String taiKhoanZalo;

    @Column(name = "NDUNG", length = 1024, nullable = false)
    private String ndung;

    @Column(name = "MA_MAU", length = 32, nullable = false)
    private String maMau;

    @Column(name = "MA_DVU", length = 32, nullable = false)
    private String maDvu;

    @Column(name = "TGIAN_TAO", length = 32, nullable = false)
    private String tgianTao;

    @Column(name = "TGIAN_GUI_DUKIEN", length = 16, nullable = false)
    private String tgianGuiDukien;

    @Column(name = "TTHAI_NVU", length = 8, nullable = false)
    private String tthaiNvu;

    @Column(name = "NGAY_DLIEU", length = 32, nullable = false)
    private String ngayDlieu;

    // ====== Các cột có thể NULL (không đánh dấu nullable = false) ======
    @Column(name = "MA_CDICH", length = 32)
    private String maCdich;

    @Column(name = "MA_KHHANG", length = 32)
    private String maKhhang;

    @Column(name = "TEN_KHHANG", length = 256)
    private String tenKhhang;

    @Column(name = "JSON_TSO", length = 512)
    private String jsonTso;

    @Column(name = "TGIAN_GUI_TCONG", length = 16)
    private String tgianGuiTcong;

    @Column(name = "MA_PHHOI", length = 64)
    private String maPhhoi;
}

