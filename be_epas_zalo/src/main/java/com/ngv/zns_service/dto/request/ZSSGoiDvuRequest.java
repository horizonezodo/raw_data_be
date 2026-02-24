package com.ngv.zns_service.dto.request;

import lombok.Data;
import org.springframework.data.domain.Pageable;

@Data
public class ZSSGoiDvuRequest {
    private Pageable pageable;

    public ZSSGoiDvuRequest() {
    }

    public ZSSGoiDvuRequest(Pageable pageable) {
        this.pageable = pageable;
    }

    public Pageable getPageable() {
        return pageable;
    }

    public void setPageable(Pageable pageable) {
        this.pageable = pageable;
    }
}
