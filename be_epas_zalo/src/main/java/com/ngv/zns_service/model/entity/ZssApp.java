package com.ngv.zns_service.model.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;

@Data
@Entity
@Table(name = "ZSS_APP")
public class ZssApp {
    @Id
    @Column(name = "APP_ID", length = 32)
    private String appId;

    @Column(name = "MA_DVI", length = 32)
    private String maDvi;

    @Column(name = "APP_NAME", length = 64)
    private String appName;

    @Column(name = "APP_SECRET", length = 128)
    private String appSecret;

    @Column(name = "WH_SECRET", length = 128)
    private String webhookSecret;

    @Column(name = "WH_VERIFICATION_FILE", length = 64)
    private String webhookVerificationFile;

    @Column(name = "WH_VERIFICATION_CONTENT", length = 256)
    private String webhookVerificationContent;

    @Column(name = "PCF_WH_URL", length = 256)
    private String pcfWhUrl;

    @Column(name = "NGUOI_NHAP", length = 32)
    private String nguoiNhap;

    @Column(name = "NGAY_NHAP", length = 32)
    private String ngayNhap;

    @Column(name = "NGUOI_SUA", length = 32)
    private String nguoiSua;

    @Column(name = "NGAY_SUA", length = 32)
    private String ngaySua;

    @Column(name = "TTHAI_NVU", length = 8)
    private String tthaiNvu;
}
