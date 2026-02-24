package com.ngv.zns_service.model.entity;


import com.ngv.zns_service.model.pk.ZSSGoiDvuCtietPK;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@IdClass(ZSSGoiDvuCtietPK.class)
@Table(name = "ZSS_GOI_DVU_CTIET")
public class ZSSGoiDvuCTiet {

    @Id
    @Column(name = "MA_GOI_DVU", length = 32, nullable = false)
    private String maGoiDvu;

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
}
