package com.ngvgroup.bpm.core.persistence.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

@Getter
@Setter
@MappedSuperclass
@EntityListeners(AuditingEntityListener.class)
public abstract class BaseEntitySimple {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

}
