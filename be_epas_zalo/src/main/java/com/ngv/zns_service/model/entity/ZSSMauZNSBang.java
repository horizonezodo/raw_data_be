package com.ngv.zns_service.model.entity;

import com.ngv.zns_service.model.pk.ZSSMauZNSBangPK;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@IdClass(ZSSMauZNSBangPK.class)
@Table(name = "ZSS_MAU_ZNS_BANG")
public class ZSSMauZNSBang {
    @Id
    @Column(name = "ZOA_ID", length = 32)
    private String zoaId;

    @Id
    @Column(name = "MA_MAU", length = 32)
    private String maMau;

    @Column(name = "DONG_SO", precision = 19, scale = 0)
    private Long dongSo;

    @Column(name = "NGUOI_NHAP", length = 32)
    private String nguoiNhap;

    @Column(name = "NGAY_NHAP", length = 32)
    private String ngayNhap;

    @Column(name = "NGUOI_SUA", length = 32)
    private String nguoiSua;

    @Column(name = "NGAY_SUA", length = 32)
    private String ngaySua;

    @Column(name = "MA_DVI", length = 32)
    private String maDvi;

    @Column(name = "TDE_BANG", length = 35)
    private String tdeBang;

    @Column(name = "NDUNG_BANG", length = 54)
    private String ndungBang;
}