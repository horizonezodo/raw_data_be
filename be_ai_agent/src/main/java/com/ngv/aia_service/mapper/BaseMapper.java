package com.ngv.aia_service.mapper;

public interface BaseMapper<Rq, Rp, E>{
    Rp toResponse(E e);
    E toEntity(Rq rq);
}
