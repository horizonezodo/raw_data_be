package com.ngv.zns_service.mapper;

public interface BaseMapper<Rq, Rp, E>{
    Rp toResponse(E e);
    E toEntity(Rq rq);
}
