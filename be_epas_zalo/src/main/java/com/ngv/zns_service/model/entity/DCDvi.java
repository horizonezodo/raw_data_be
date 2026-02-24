package com.ngv.zns_service.model.entity;


import jakarta.persistence.*;
import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "DC_DVI")
public class DCDvi {

    @Id
    @Column(name = "MA_DVI", length = 32, nullable = false)
    private String maDvi;

    @Column(name = "NGUOI_NHAP", length = 32, nullable = false)
    private String nguoiNhap;

    @Column(name = "NGAY_NHAP", length = 32, nullable = false)
    private String ngayNhap;

    @Column(name = "NGUOI_SUA", length = 32, nullable = false)
    private String nguoiSua;

    @Column(name = "NGAY_SUA", length = 32, nullable = false)
    private String ngaySua;

    @Column(name = "TEN_DVI", length = 256, nullable = false)
    private String tenDvi;

    @Column(name = "TEN_VTAT", length = 64)
    private String tenVtat;

    @Column(name = "DIACHI", length = 256)
    private String diaChi;

    @Column(name = "SDT", length = 32)
    private String sdt;

    @Column(name = "EMAIL", length = 128)
    private String email;

    @Column(name = "GDOC", length = 128)
    private String giamDoc;

    @Column(name = "MA_NHNN", length = 12)
    private String maNhhn;

    @Column(name = "TSO_PGD", precision = 10, scale = 0)
    private Integer tsoPgd;

    @Column(name = "MA_TINHTP", length = 32)
    private String maTinhTp;
}
