package com.ngv.zns_service.model.entity;


import com.ngv.zns_service.model.pk.ZSSMauZNSHanhPK;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@IdClass(ZSSMauZNSHanhPK.class)
@Table(name = "ZSS_MAU_ZNS_HANH")
public class ZSSMauZNSHanh {

    @Id
    @Column(name = "ZOA_ID", length = 32)
    private String zoaId;

    @Id
    @Column(name = "MA_MAU", length = 32)
    private String maMau;


    @Column(name = "NGUOI_NHAP", length = 32)
    private String nguoiNhap;

    @Column(name = "NGAY_NHAP", length = 32)
    private String ngayNhap;

    @Column(name = "NGUOI_SUA", length = 32)
    private String nguoiSua;

    @Column(name = "NGAY_SUA", length = 32)
    private String ngaySua;

    @Column(name = "LOAI_BUTTON", precision = 10, scale = 0)
    private Integer loaiButton;


    @Column(name = "LOAI_HANH", length = 32)
    private String loaiHanh;


    @Lob
    @Column(name = "LOGO_SANG")
    private byte[] logoSang;

    @Lob
    @Column(name = "LOGO_TOI")
    private byte[] logoToi;

    @Lob
    @Column(name = "ANH1")
    private byte[] anh1;

    @Lob
    @Column(name = "ANH2")
    private byte[] anh2;

    @Lob
    @Column(name = "ANH3")
    private byte[] anh3;

    @Column(name = "TEN_LGO_SANG", length = 128)
    private String tenLogoSang;

    @Column(name = "TEN_LGO_TOI", length = 128)
    private String tenLogoToi;

    @Column(name = "TEN_ANH1", length = 128)
    private String tenAnh1;

    @Column(name = "TEN_ANH2", length = 128)
    private String tenAnh2;

    @Column(name = "TEN_ANH3", length = 128)
    private String tenAnh3;





}
