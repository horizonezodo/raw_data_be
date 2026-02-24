package com.ngv.zns_service.model.entity;

import com.ngv.zns_service.model.pk.ZSSMauZNSButtonPK;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@IdClass(ZSSMauZNSButtonPK.class)
@Table(name = "ZSS_MAU_ZNS_BUTTON")
public class ZSSMauZNSButton {
    @Id
    @Column(name = "MA_MAU", length = 32, nullable = false)
    private String maMau;
    @Id
    @Column(name = "ZOA_ID", length = 32, nullable = false)
    private String zoaId;

    @Id
    @Column(name = "MA_BUTTON", length = 64, nullable = false)
    private String maButton;

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

    @Column(name = "LOAI_BUTTON", length = 256)
    private String loaiButton;

    @Column(name = "NDUNG", length = 1024)
    private String ndung;

    @Column(name = "LKET", length = 1024)
    private String lket;
}

