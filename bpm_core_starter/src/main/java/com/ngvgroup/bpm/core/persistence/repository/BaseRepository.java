package com.ngvgroup.bpm.core.persistence.repository;

import com.ngvgroup.bpm.core.persistence.model.BaseEntity;
import com.ngvgroup.bpm.core.persistence.model.BaseEntitySimple;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.NoRepositoryBean;


@NoRepositoryBean
public interface BaseRepository<E extends BaseEntitySimple> extends JpaRepository<E, Long> {
}
