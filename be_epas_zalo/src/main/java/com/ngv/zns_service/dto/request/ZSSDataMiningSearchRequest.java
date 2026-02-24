package com.ngv.zns_service.dto.request;

import java.util.List;

import lombok.Data;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ZSSDataMiningSearchRequest {
    private String tuNgay;
    private String denNgay;
    private String maDvi;
    private List<String> maDvuList;
    private String tthaiGuizns;
    private List<String> label;
    private PageableRequest pageable;
}
