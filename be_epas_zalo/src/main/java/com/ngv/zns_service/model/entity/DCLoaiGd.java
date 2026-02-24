package com.ngv.zns_service.model.entity;

import jakarta.persistence.*;
import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "DC_LOAI_GD")
public class DCLoaiGd {

    @Id
    @Column(name = "MA_LOAI_GDICH", length = 32, nullable = false)
    private String maLoaiGdich;

    @Column(name = "TEN_LOAI_GDICH", length = 256, nullable = false)
    private String tenLoaiGdich;

    @Column(name = "NGUOI_NHAP", length = 32, nullable = false)
    private String nguoiNhap;

    @Column(name = "NGAY_NHAP", length = 32, nullable = false)
    private String ngayNhap;

    @Column(name = "NGUOI_SUA", length = 32, nullable = false)
    private String nguoiSua;

    @Column(name = "NGAY_SUA", length = 32, nullable = false)
    private String ngaySua;
}
