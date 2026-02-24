package com.naas.admin_service.features.job.repository;

import com.naas.admin_service.features.job.dto.ComCfgJobDto;
import com.naas.admin_service.features.job.model.ComCfgJob;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface ComCfgJobRepository extends JpaRepository<ComCfgJob, Long> {

    List<ComCfgJob> findByIsActive(Integer isActive);

    Optional<ComCfgJob> findByJobNameAndIsActive(String jobName, Integer isActive);

    Optional<ComCfgJob> findByJobName(String jobName);

    @Query("""
        SELECT new com.naas.admin_service.features.job.dto.ComCfgJobDto(
            j.id,
            j.jobName,
            j.cronExpr,
            j.isActive,
            j.description
        ) FROM ComCfgJob j
        WHERE (:search IS NULL 
        OR LOWER(j.jobName) LIKE LOWER(CONCAT('%', :search, '%'))
        OR LOWER(j.description) LIKE LOWER(CONCAT('%', :search, '%'))
        )
        """)
    Page<ComCfgJobDto> search(String search, Pageable pageable);
}
