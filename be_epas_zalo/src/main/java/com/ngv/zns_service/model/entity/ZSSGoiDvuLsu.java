package com.ngv.zns_service.model.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "ZSS_GOI_DVU_LS")
public class ZSSGoiDvuLsu {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", precision = 19, scale = 0)
    private Long id;

    @Column(name = "MA_GOI_DVU", length = 32, nullable = false)
    private String maGoiDvu;

    @Column(name = "NGUOI_NHAP", length = 32, nullable = false)
    private String nguoiNhap;

    @Column(name = "NGAY_NHAP", length = 32, nullable = false)
    private String ngayNhap;

    @Column(name = "NGUOI_SUA", length = 32, nullable = false)
    private String nguoiSua;

    @Column(name = "NGAY_SUA", length = 32, nullable = false)
    private String ngaySua;

    @Column(name = "TEN_GOI_DVU", length = 256, nullable = false)
    private String tenGoiDvu;

    @Column(name = "TTHAI_NVU", length = 8, nullable = false)
    private String tthaiNvu;

    @Column(name = "MOTA", length = 1024)
    private String mota;
}
