package com.ngv.zns_service.dto.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ZSSMauZNZSearchRequest {
    private String maLoaiMau;
    private String tthaiNvu;
    private String tthaiZns;
    private String maDvi;
    private List<String> label;
    private PageableRequest pageable;
}
