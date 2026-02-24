package com.ngv.zns_service.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ZSSNDungResponse {
    private String maMau;
    private String maDvi;
    private String tenDvi;
    private String maDvu;
    private String tenDvu;
    private String nDung;
    private String taiKhoanZalo;
    private String tgianTao;
    private String tthaiGuizns;
    private String tgianGuiTcong;
}