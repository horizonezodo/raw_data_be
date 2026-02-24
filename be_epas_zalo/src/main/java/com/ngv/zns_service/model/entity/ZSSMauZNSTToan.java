package com.ngv.zns_service.model.entity;

import jakarta.persistence.*;
import lombok.*;

import com.ngv.zns_service.model.pk.ZSSMauZNSTToanPK;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@IdClass(ZSSMauZNSTToanPK.class)
@Table(name = "ZSS_MAU_ZNS_TTOAN")
public class ZSSMauZNSTToan {
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

  @Column(name = "MA_DVI", length = 32)
  private String maDvi;

  @Column(name = "MA_NGANHANG", length = 32)
  private String maNganHang;

  @Column(name = "TEN_TKHOAN", length = 256)
  private String tenTkhoan;

  @Column(name = "STK", length = 32)
  private String stk;

  @Column(name = "STIEN", length = 32)
  private String soTien;

  @Column(name = "NDUNG", length = 1024)
  private String noiDungCKhoan;
}
