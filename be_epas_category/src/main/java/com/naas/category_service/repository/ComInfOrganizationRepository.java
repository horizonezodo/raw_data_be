package com.naas.category_service.repository;


import com.naas.category_service.dto.ComInfOrganizationDto;
import com.naas.category_service.model.ComInfOrganization;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ComInfOrganizationRepository extends JpaRepository<ComInfOrganization, Integer> {

    ComInfOrganization findByOrgCode(String orgCode);

    @Query(value = "SELECT new com.naas.category_service.dto.ComInfOrganizationDto(c.orgCode, c.orgName) " +
            "FROM ComInfOrganization c ")
    List<ComInfOrganizationDto> listBranch();


}
