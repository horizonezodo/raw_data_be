package com.ngvgroup.bpm.core.persistence.mapper;


import org.mapstruct.MappingTarget;

import java.util.List;

public interface BaseMapper<D, E> {
    D toDto(E entity);

    E toEntity(D dto);

    List<D> toListDto(List<E> entity);

    List<E> toListEntity(List<D> dto);

    void updateEntityFromDto(D dto, @MappingTarget E entity);
}

