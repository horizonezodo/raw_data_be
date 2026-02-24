package com.ngv.zns_service.model.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;


@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "ZSS_DVI_THUE_BAO")
@Accessors(chain = true)
public class ZSSDviThueBao {

    @Id
    @Column(name = "MA_THUE_BAO", length = 64, nullable = false)
    private String maThueBao;

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

    @Column(name = "ZOA_ID", length = 32)
    private String zoaId;

    @Column(name = "MA_GOI_DVU", length = 32)
    private String maGoiDvu;

    @Column(name = "NGAY_DKY", length = 8)
    private String ngayDky;

    @Column(name = "NGAY_HLUC", length = 8)
    private String ngayHluc;

    @Column(name = "TGIAN", precision = 10, scale = 0)
    private Integer tgian;

    @Column(name = "DVI_TGIAN", length = 8)
    private String dviTgian;

    @Column(name = "NGAY_HET_HLUC", length = 8)
    private String ngayHetHluc;

    @Column(name = "CTHI_GHAN", length = 4)
    private String cthiGhan;

    @Column(name = "TTHAI_NVU", length = 8)
    private String tthaiNvu;

    @Column(name = "MOTA", length = 1024)
    private String mota;
}

