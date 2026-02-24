package com.naas.admin_service.features.category.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.naas.admin_service.features.category.dto.cominforganization.ComInfOrganizationDto;
import com.naas.admin_service.features.category.model.ComInfOrganization;

import java.util.List;

@Repository
public interface ComInfOrganizationRepository extends JpaRepository<ComInfOrganization, Long> {
    boolean existsByOrgCode(String orgCode);

    @Query(value = "SELECT new com.naas.admin_service.features.category.dto.cominforganization.ComInfOrganizationDto(" +
            "c.orgCode, " +
            "case when c.orgCode = '%' then 'Tất cả' else c.orgName end) " +
            "FROM ComInfOrganization c " +
            "WHERE c.orgName is not null " +
            "AND lower(c.orgName) like lower(concat('%', :filter, '%'))")
    List<ComInfOrganizationDto> listOrganization(@Param("filter") String filter);
}
