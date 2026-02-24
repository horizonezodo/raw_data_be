package com.ngv.zns_service.model.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;

@Data
@Entity
@Table(name = "ZSS_TKHOAN_ZOA")
public class ZssTKhoanZoa {
    
    @Id
    @Column(name = "MA_ZOA", length = 64)
    private String maZoa;
    
    @Column(name = "MA_DVI", length = 32)
    private String maDvi;

    @Column(name = "APP_ID", length = 32)
    private String appId;
    
    @Column(name = "TEN_ZOA", length = 64)
    private String tenZoa;
    
    @Column(name = "ACCESS_TOKEN", length = 4000)
    private String accessToken;
    
    @Column(name = "REFRESH_TOKEN", length = 4000)
    private String refreshToken;

    @Column(name = "NGAY_DHAN_TK", precision = 19, scale = 0)
    private Long ngayDhanTk;
}
