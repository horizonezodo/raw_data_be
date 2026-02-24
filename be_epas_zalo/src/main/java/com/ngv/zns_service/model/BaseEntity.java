package com.ngv.zns_service.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Getter
@Setter
@MappedSuperclass
public abstract class BaseEntity {

    private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("yyyyMMdd");

    @Column(name = "NGAY_NHAP", length = 16, nullable = false, updatable = false)
    private String ngayNhap;

    @Column(name = "NGAY_SUA", length = 16)
    private String ngaySua;

    @PrePersist
    protected void onCreate() {
        String now = LocalDateTime.now().format(FORMATTER);
        this.ngayNhap = now;
        this.ngaySua = now;
    }

    @PreUpdate
    protected void onUpdate() {
        this.ngaySua = LocalDateTime.now().format(FORMATTER);
    }
}
