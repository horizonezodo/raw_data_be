package com.ngv.zns_service.dto.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ZSSDviThueBaoRequest {
    private String maDvi;
    private String zoaId;
    private String maGoiDvu;
    private String ngayDky;
    private String ngayHluc;
    private Integer tgian;
    private String dviTgian;
    private String cthiGhan;
    private String tthaiNvu;
    private String tenGoiDvu;
    private String tenDmucCtiet;
    private String ngayHetHluc;

}
