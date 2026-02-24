package com.ngv.zns_service.model.entity;

import com.ngv.zns_service.model.pk.ZSSDvuGDichPK;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@IdClass(ZSSDvuGDichPK.class)
@Table(name = "ZSS_DVU_GDICH")
public final class ZSSDvuGDich {

    @Id
    @Column(name = "MA_DVU", length = 32, nullable = false)
    private String maDvu;

    @Id
    @Column(name = "MA_LOAI_GDICH", length = 32, nullable = false)
    private String maLoaiGdich;

    @Column(name = "NGUOI_NHAP", length = 32, nullable = false)
    private String nguoiNhap;

    @Column(name = "NGAY_NHAP", length = 32, nullable = false)
    private String ngayNhap;

    @Column(name = "NGUOI_SUA", length = 32, nullable = false)
    private String nguoiSua;

    @Column(name = "NGAY_SUA", length = 32, nullable = false)
    private String ngaySua;
}
