package com.ngvgroup.bpm.core.persistence.service.impl;

import com.ngvgroup.bpm.core.persistence.model.BaseEntitySimple;
import com.ngvgroup.bpm.core.persistence.service.storedprocedure.BaseStoredProcedureService;
import com.ngvgroup.bpm.core.persistence.mapper.BaseMapper;
import com.ngvgroup.bpm.core.persistence.repository.BaseRepository;
import com.ngvgroup.bpm.core.persistence.service.BaseService;
import com.ngvgroup.bpm.core.common.exception.BusinessException;
import com.ngvgroup.bpm.core.common.exception.ErrorCode;
import com.ngvgroup.bpm.core.security.UsernameNormalizer;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Transactional
public abstract class BaseServiceImpl<E extends BaseEntitySimple, D>
        extends BaseStoredProcedureService
        implements BaseService<E, D> {

    protected final BaseRepository<E> repository;
    protected final BaseMapper<D, E> mapper;

    protected BaseServiceImpl(BaseRepository<E> repository,
                              BaseMapper<D, E> mapper) {
        this.repository = repository;
        this.mapper = mapper;
    }

    @Override
    public D create(D dto) {
        validateBeforeCreate(dto);
        E entity = mapper.toEntity(dto);
        beforeSaveCreate(entity, dto);
        repository.save(entity);
        afterSaveCreate(entity);
        return mapper.toDto(entity);
    }

    @Override
    public D update(Long id, D dto) {
        E entity = repository.findById(id)
                .orElseThrow(() -> new BusinessException(ErrorCode.NOT_FOUND, id));
        validateBeforeUpdate(dto, entity);
        mapper.updateEntityFromDto(dto, entity);
        repository.save(entity);
        afterUpdate(entity);
        return mapper.toDto(entity);
    }

    @Override
    public void delete(Long id) {
        repository.deleteById(id);
        // nếu sau này muốn soft-delete thì sửa lại ở đây
    }

    @Override
    public D findById(Long id) {
        return repository.findById(id)
                .map(mapper::toDto)
                .orElseThrow(() -> new BusinessException(ErrorCode.NOT_FOUND, id));
    }

    @Override
    public List<D> findAll() {
        return repository.findAll()
                .stream()
                .map(mapper::toDto)
                .collect(Collectors.toList());
    }

    /**
     * Override trong service cụ thể nếu muốn validate riêng.
     */
    protected void validateBeforeCreate(D dto) {
        // mặc định không validate, override ở service con
    }

    protected void validateBeforeUpdate(D dto, E entity) {
        // mặc định không validate, override ở service con
    }

    protected void beforeSaveCreate(E entity, D dto) {
        // mặc định không làm gì, override nếu cần
    }

    protected void afterSaveCreate(E entity) {
        // mặc định không làm gì, override nếu cần
    }

    protected void afterUpdate(E entity) {
        // mặc định không làm gì, override nếu cần
    }
}
