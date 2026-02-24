package com.ngv.zns_service.dto.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CaiDatTSoClientRequest {
    private String tenTso;
    private String maCtaoGtriTso;
}
