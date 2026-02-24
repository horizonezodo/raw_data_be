package com.ngv.zns_service.model.entity;

import com.ngv.zns_service.model.pk.ZSSMauZNZPK;
import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@IdClass(ZSSMauZNZPK.class)
@Table(name = "ZSS_MAU_ZNS")
public class ZSSMauZNZ {

    @Id
    @Column(name = "MA_MAU", length = 32, nullable = false)
    private String maMau;

    @Id
    @Column(name = "ZOA_ID", length = 32, nullable = false)
    private String zoaId;

    @Column(name = "NGUOI_NHAP", length = 32)
    private String nguoiNhap;

    @Column(name = "NGAY_NHAP", length = 32)
    private String ngayNhap;

    @Column(name = "NGUOI_SUA", length = 32)
    private String nguoiSua;

    @Column(name = "NGAY_SUA", length = 32)
    private String ngaySua;

    @Column(name = "MA_DVI", length = 32, nullable = false)
    private String maDvi;

    @Column(name = "TEN_MAU", length = 60)
    private String tenMau;

    @Column(name = "TGIAN_CHO", length = 16)
    private String tgianCho;

    @Column(name = "LKET", length = 1024)
    private String lket;

    @Column(name = "MA_CLUONG_MAU", length = 32)
    private String maCluongMau;

    @Column(name = "LOAI_ZNS", length = 64)
    private String loaiZns;

    @Column(name = "MA_LOAI_MAU", length = 32)
    private String maLoaiMau;

    @Column(name = "DONGIA", precision = 21, scale = 2)
    private BigDecimal donGia;

    @Column(name = "TDE_MAU", length = 65)
    private String tdeMau;

    @Column(name = "NDUNG_TDE", length = 400)
    private String ndungTde;

    @Column(name = "VBAN1", length = 400)
    private String vban1;

    @Column(name = "VBAN2", length = 400)
    private String vban2;

    @Column(name = "VBAN3", length = 400)
    private String vban3;

    @Column(name = "VBAN4", length = 400)
    private String vban4;

    @Column(name = "GHI_CHU", length = 400)
    private String ghiChu;

    @Column(name = "TTHAI_ZNS", length = 64)
    private String tthaiZns;

    @Column(name = "TTHAI_NVU", length = 8, nullable = false)
    private String tthaiNvu;

    @Column(name = "MA_MAU_DTAC", length = 64)
    private String maMauDtac;

    @Column(name = "LY_DO", length = 2048)
    private String lyDo;

    @Column(name = "TIMEOUT", precision = 38, scale = 2)
    private BigDecimal timeout;
}
