package com.ngv.zns_service.model.entity;


import com.ngv.zns_service.model.pk.ZSSNDungPK;
import jakarta.persistence.*;
import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@IdClass(ZSSNDungPK.class)
@Table(name = "ZSS_NDUNG")
public class ZSSNDung {
    @Column(name = "ZOA_ID", length = 32, nullable = false)
    private String zoaId;

    @Column(name = "MA_MAU", length = 32, nullable = false)
    private String maMau;

    @Column(name = "JSON_TSO", length = 512)
    private String jsonTso;

    @Column(name = "MA_CDICH", length = 32)
    private String maCdich;

    @Column(name = "MA_DVI", length = 32, nullable = false)
    private String maDvi;

    @Column(name = "MA_DVU", length = 32, nullable = false)
    private String maDvu;

    @Column(name = "MA_KHHANG", length = 32)
    private String maKhhang;

    @Id
    @Column(name = "MA_NDUNG", length = 32, nullable = false)
    private String maNdung;

    @Column(name = "MA_PHHOI", length = 64)
    private String maPhhoi;

    @Column(name = "NDUNG", length = 1024, nullable = false)
    private String ndung;

    @Column(name = "NGAY_NHAP", length = 32, nullable = false)
    private String ngayNhap;

    @Column(name = "NGAY_SUA", length = 32, nullable = false)
    private String ngaySua;

    @Column(name = "NGUOI_NHAP", length = 32, nullable = false)
    private String nguoiNhap;

    @Column(name = "NGUOI_SUA", length = 32, nullable = false)
    private String nguoiSua;

    @Column(name = "TAI_KHOAN_ZALO", length = 16, nullable = false)
    private String taiKhoanZalo;

    @Column(name = "TEN_KHHANG", length = 256)
    private String tenKhhang;

    @Column(name = "TGIAN_GUI_DUKIEN", length = 16, nullable = false)
    private String tgianGuiDukien;

    @Column(name = "TGIAN_GUI_TCONG", length = 16)
    private String tgianGuiTcong;

    @Column(name = "TGIAN_TAO", length = 32, nullable = false)
    private String tgianTao;

    @Column(name = "TTHAI_GUIZNS", length = 64)
    private String tthaiGuizns;

    @Column(name = "TTHAI_NVU", length = 8, nullable = false)
    private String tthaiNvu;

    @Column(name = "MA_MAU_DTAC", length = 64, nullable = false)
    private String maMauDtac;

    @Column(name = "ID_MESSAGE", length = 64)
    private String idMessage;

    @Column(name = "TGIAN_GUI", length = 32)
    private String tgianGui;
}
