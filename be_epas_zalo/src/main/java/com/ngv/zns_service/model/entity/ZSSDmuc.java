package com.ngv.zns_service.model.entity;

import jakarta.persistence.*;
import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "ZSS_DMUC")
public class ZSSDmuc {

    @Id
    @Column(name = "MA_DMUC", length = 64, nullable = false)
    private String maDmuc;

    @Column(name = "NGUOI_NHAP", length = 64, nullable = false)
    private String nguoiNhap;

    @Column(name = "NGAY_NHAP", length = 32, nullable = false)
    private String ngayNhap;

    @Column(name = "NGUOI_SUA", length = 64, nullable = false)
    private String nguoiSua;

    @Column(name = "NGAY_SUA", length = 32, nullable = false)
    private String ngaySua;

    @Column(name = "TEN_DMUC", length = 256, nullable = false)
    private String tenDmuc;
}
