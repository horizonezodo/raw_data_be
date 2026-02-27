package com.naas.admin_service.features.tenant.repository;

import com.naas.admin_service.features.tenant.dto.ComCfgTenantResponseDto;
import com.naas.admin_service.features.tenant.model.ComCfgTenant;
import com.ngvgroup.bpm.core.persistence.repository.BaseRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface ComCfgTenantRepository extends BaseRepository<ComCfgTenant> {

    @Query("""
        SELECT new com.naas.admin_service.features.tenant.dto.ComCfgTenantResponseDto(
            t.id,
            t.tenantId,
            t.tenantName,
            t.dbType,
            t.username,
            t.active
        )
        FROM ComCfgTenant t
        WHERE (:keyword IS NULL OR :keyword = ''
           OR LOWER(t.tenantId) LIKE LOWER(CONCAT('%', :keyword, '%'))
           OR LOWER(t.tenantName) LIKE LOWER(CONCAT('%', :keyword, '%'))
           OR LOWER(t.username) LIKE LOWER(CONCAT('%', :keyword, '%'))
        )
    """)

    Page<ComCfgTenantResponseDto> search(@Param("keyword") String keyword, Pageable pageable);

    boolean existsByTenantIdAndActive(String tenantId, String active);
}