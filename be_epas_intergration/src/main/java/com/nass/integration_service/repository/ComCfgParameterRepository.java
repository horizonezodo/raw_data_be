package com.nass.integration_service.repository;

import com.nass.integration_service.model.ComCfgParameter;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ComCfgParameterRepository extends JpaRepository<ComCfgParameter, Long> {
    Optional<ComCfgParameter> findByParamCodeAndIsActiveTrueAndIsDeleteFalse(String paramCode);
}
