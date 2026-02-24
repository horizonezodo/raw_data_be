package ngvgroup.com.bpm.features.common.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import ngvgroup.com.bpm.features.common.model.ComCfgParameter;

public interface ComCfgParameterRepository extends JpaRepository<ComCfgParameter, Long> {
    Optional<ComCfgParameter> findByParamCodeAndIsActiveTrueAndIsDeleteFalse(String paramCode);
}
