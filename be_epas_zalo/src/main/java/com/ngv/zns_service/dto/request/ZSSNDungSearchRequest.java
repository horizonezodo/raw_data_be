package com.ngv.zns_service.dto.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ZSSNDungSearchRequest {
    private String tuNgay;
    private String denNgay;
    private String maDvu;
    private String maDvi;
    private String tthaiGuizns;
    private List<String> label;
    private PageableRequest pageable;
}
