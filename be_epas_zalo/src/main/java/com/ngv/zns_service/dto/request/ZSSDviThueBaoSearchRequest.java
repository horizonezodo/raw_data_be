package com.ngv.zns_service.dto.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ZSSDviThueBaoSearchRequest {
    private String maGoiDvu;
    private String tthaiNvu;
    private String tuNgayDky;
    private String denNgayDky;
    private String tuNgayHetHluc;
    private String ngayHetHluc;
    private List<String> label;
    private PageableRequest pageable;
}

