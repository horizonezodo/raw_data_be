package com.ngv.zns_service.dto.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ZssMauZnsTsoRequest {
    private String maDvi;
    private String maMau;
    private String maMauDtac;
    private String tenDvi;
    private String tenMau;
    private String zoaId;
    private List<CaiDatTSoClientRequest> caiDatTSo;
}
