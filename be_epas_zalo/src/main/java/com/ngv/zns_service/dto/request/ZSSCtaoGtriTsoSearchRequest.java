package com.ngv.zns_service.dto.request;

import lombok.Data;

import java.util.List;

@Data
public class ZSSCtaoGtriTsoSearchRequest {
    private String maCtaoGtriTso;
    private String tenCtaoGtriTso;
    private String cthucCtaoGtriTso;
    private List<String> label;
    private PageableRequest pageable;
}
