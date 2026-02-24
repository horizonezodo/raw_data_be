package com.ngv.zns_service.model.entity;

import com.ngv.zns_service.model.pk.ZSSMauZNSTsoPK;
import jakarta.persistence.*;
import lombok.Data;

import java.math.BigDecimal;

@Entity
@IdClass(ZSSMauZNSTsoPK.class)
@Table(name = "ZSS_MAU_ZNS_TSO")
@Data
public class ZSSMauZNSTso {

    @Id
    @Column(name = "MA_TSO", precision = 19, scale = 0, nullable = false)
    private Long maTso;

    @Id
    @Column(name = "ZOA_ID", nullable = false, length = 32)
    private String zoaId;

    @Id
    @Column(name = "MA_MAU", nullable = false, length = 32)
    private String maMau;

    @Column(name = "NGUOI_NHAP", length = 32)
    private String nguoiNhap;

    @Column(name = "NGAY_NHAP", length = 32)
    private String ngayNhap;

    @Column(name = "NGUOI_SUA", length = 32)
    private String nguoiSua;

    @Column(name = "NGAY_SUA", length = 32)
    private String ngaySua;

    @Column(name = "MA_DVI", nullable = false, length = 32)
    private String maDvi;

    @Column(name = "TEN_TSO", nullable = false, length = 256)
    private String tenTso;

    @Column(name = "PARAM_TYPE", length = 256)
    private String paramType;

    @Column(name = "NDUNG_TSO", length = 400)
    private String ndungTso;

    @Column(name = "BBUOC", length = 32)
    private String bbuoc;

    @Column(name = "KIEU_TSO", length = 32)
    private String kieuTso;

    @Column(name = "SO_KTU_TDA", precision = 21, scale = 0)
    private BigDecimal soKtuTda;

    @Column(name = "SO_KTU_TTHIEU", precision = 21, scale = 0)
    private BigDecimal soKtuTthieu;

    @Column(name = "NHAN_GTRI_NULL", length = 1)
    private String nhanGtriNull;

    @Column(name = "MA_CTAO_GTRI_TSO", length = 32)
    private String maCtaoGtriTso;

    @Column(name = "TSO_DTAC", length = 256)
    private String tSoDtac;

}
