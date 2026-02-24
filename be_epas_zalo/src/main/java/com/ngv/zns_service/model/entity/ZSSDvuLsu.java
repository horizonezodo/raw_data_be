package com.ngv.zns_service.model.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "ZSS_DVU_LSU")
public class ZSSDvuLsu {
    @Id
    @Column(name = "MA_DVU", length = 32, nullable = false)
    private String maDvu;

    @Column(name = "NGUOI_NHAP", length = 32, nullable = false)
    private String nguoiNhap;

    @Column(name = "NGAY_NHAP", length = 32, nullable = false)
    private String ngayNhap;

    @Column(name = "NGUOI_SUA", length = 32, nullable = false)
    private String nguoiSua;

    @Column(name = "NGAY_SUA", length = 32, nullable = false)
    private String ngaySua;

    @Column(name = "TEN_DVU", length = 256, nullable = false)
    private String tenDvu;

    @Column(name = "MA_LOAI_DVU", length = 32, nullable = false)
    private String maLoaiDvu;

    @Column(name = "SO_LGUI_TBAI", precision = 10, scale = 0, nullable = false)
    private Integer soLguiTbai;

    @Column(name = "HTHUC_GUI", length = 32)
    private String hthucGui;

    @Column(name = "TGIAN_GUI", length = 32)
    private String tgianGui;

    @Column(name = "GIO_BDAU_GUI", length = 8)
    private String gioBdauGui;

    @Column(name = "GIO_KTHUC_GUI", length = 8)
    private String gioKthucGui;

    @Column(name = "SU_KIEN", length = 1024)
    private String suKien;

    @Column(name = "DVU_LQUAN", length = 4)
    private String dvuLquan;

    @Column(name = "MOTA", length = 1024)
    private String mota;

    @Column(name = "TTHAI_NVU", length = 8)
    private String tthaiNvu;
}
