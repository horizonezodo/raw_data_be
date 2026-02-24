package com.ngv.zns_service.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ZSSDviThueBaoResponse {
    private String maThueBao;
    private String maDvi;
    private String zoaId;
    private String tenDvi;
    private String maGoiDvu;
    private String tenGoiDvu;
    private String ngayDky;
    private String ngayHluc;
    private Integer tgian;
    private String ngayHetHluc;
    private String tthaiNvu;
}
