package com.ngv.zns_service.model.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import java.io.Serializable;

@Getter
@Setter
@Entity
@Table(name = "ZSS_CTAO_GTRI_TSO")
public class ZSSCtaoGtriTso implements Serializable {

    @Id
    @Column(name = "MA_CTAO_GTRI_TSO", length = 32, nullable = false)
    private String maCtaoGtriTso;

    @Column(name = "NGUOI_NHAP", length = 32)
    private String nguoiNhap;

    @Column(name = "NGAY_NHAP", length = 32)
    private String ngayNhap;

    @Column(name = "NGUOI_SUA", length = 32)
    private String nguoiSua;

    @Column(name = "NGAY_SUA", length = 32)
    private String ngaySua;

    @Column(name = "TEN_CTAO_GTRI_TSO", length = 512, nullable = false)
    private String tenCtaoGtriTso;

    @Column(name = "CTHUC_CTAO_GTRI_TSO", length = 1024, nullable = false)
    private String cthucCtaoGtriTso;

    @Column(name = "TTHAI_NVU", length = 8, nullable = false)
    private String tthaiNvu;

    @Column(name = "LOAI_GIA_TRI", length = 16)
    private String loaiGiaTri;

    @Column(name = "MDICH_SDUNG", length = 16)
    private String mdichSdung;
}

