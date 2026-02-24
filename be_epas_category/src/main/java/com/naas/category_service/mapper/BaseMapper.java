package com.naas.category_service.mapper;

public interface BaseMapper<Dto, Entity> {
    Entity toEntity(Dto dto);
    Dto toDto(Entity entity);
}
