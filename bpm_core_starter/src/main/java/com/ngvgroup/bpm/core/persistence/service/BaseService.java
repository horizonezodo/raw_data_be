package com.ngvgroup.bpm.core.persistence.service;

import java.util.List;

public interface BaseService<E, D> {

    D create(D dto);

    D update(Long id, D dto);

    void delete(Long id);

    D findById(Long id);

    List<D> findAll();
}
