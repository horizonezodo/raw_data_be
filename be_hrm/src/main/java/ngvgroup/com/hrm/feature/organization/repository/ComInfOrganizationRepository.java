package ngvgroup.com.hrm.feature.organization.repository;

import ngvgroup.com.hrm.feature.organization.dto.ComInfOrganizationDto;
import ngvgroup.com.hrm.feature.organization.model.ComInfOrganization;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ComInfOrganizationRepository extends JpaRepository<ComInfOrganization, Long> {
    boolean existsByOrgCode(String orgCode);

    @Query(value = "SELECT new ngvgroup.com.hrm.feature.organization.dto.ComInfOrganizationDto(" +
            "c.orgCode, c.orgName) " +
            "FROM ComInfOrganization c " +
            "WHERE c.orgName is not null " +
            "AND lower(c.orgName) like lower(concat('%', :filter, '%'))")
    List<ComInfOrganizationDto> listOrganization(@Param("filter") String filter);
}
