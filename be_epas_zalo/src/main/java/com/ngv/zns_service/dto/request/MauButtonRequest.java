package com.ngv.zns_service.dto.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class MauButtonRequest {

    private String maMau;

    private String loaiButton;

    private String ndung;

    private String lket;
}
