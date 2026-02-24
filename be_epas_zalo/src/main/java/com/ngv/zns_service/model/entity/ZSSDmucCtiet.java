package com.ngv.zns_service.model.entity;

import com.ngv.zns_service.model.pk.ZSSDmucCtietPK;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@IdClass(ZSSDmucCtietPK.class)
@Table(name = "ZSS_DMUC_CTIET")
public class ZSSDmucCtiet {

    @Id
    @Column(name = "MA_DMUC_CTIET", length = 64, nullable = false)
    private String maDmucCtiet;

    @Id
    @Column(name = "MA_DMUC", length = 64, nullable = false)
    private String maDmuc;

    @Column(name = "NGUOI_NHAP", length = 64, nullable = false)
    private String nguoiNhap;

    @Column(name = "NGAY_NHAP", length = 16, nullable = false)
    private String ngayNhap;

    @Column(name = "NGUOI_SUA", length = 64, nullable = false)
    private String nguoiSua;

    @Column(name = "NGAY_SUA", length = 16, nullable = false)
    private String ngaySua;


    @Column(name = "TEN_DMUC_CTIET", length = 256)
    private String tenDmucCtiet;
}
