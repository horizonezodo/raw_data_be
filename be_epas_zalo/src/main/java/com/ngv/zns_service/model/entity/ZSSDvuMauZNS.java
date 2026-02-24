package com.ngv.zns_service.model.entity;
import com.ngv.zns_service.model.pk.ZSSDvuMauZNSPK;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import jakarta.persistence.*;


@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@IdClass(ZSSDvuMauZNSPK.class)
@Table(name = "ZSS_DVU_MAU_ZNS")
public class ZSSDvuMauZNS {

    @Id
    @Column(name = "ZOA_ID", length = 32, nullable = false)
    private String zoaId;

    @Id
    @Column(name = "MA_DVU", length = 32, nullable = false)
    private String maDvu;

    @Id
    @Column(name = "MA_MAU", length = 32, nullable = false)
    private String maMau;

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
}

