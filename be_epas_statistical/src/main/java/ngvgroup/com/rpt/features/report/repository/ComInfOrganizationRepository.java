package ngvgroup.com.rpt.features.report.repository;

import ngvgroup.com.rpt.features.report.dto.ComInfOrganizationDto;
import ngvgroup.com.rpt.features.report.model.ComInfOrganization;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ComInfOrganizationRepository extends JpaRepository<ComInfOrganization, Integer> {

    ComInfOrganization findByOrgCode(String orgCode);

    @Query(value = "SELECT new ngvgroup.com.rpt.features.report.dto.ComInfOrganizationDto(" +
            "c.orgCode, " +
            "case when c.orgCode = '%' then 'Tất cả' else c.orgName end) " +
            "FROM ComInfOrganization c " +
            "WHERE c.orgName is not null " +
            "AND lower(c.orgName) like lower(concat('%', :filter, '%'))")
    List<ComInfOrganizationDto> listOrganization(@Param("filter") String filter);

    @Query("SELECT c.orgLegalCode FROM ComInfOrganization c WHERE c.parentCode IS NULL AND c.orgCode = :orgCode")
    String findOrgLegalCodeByOrgCode(@Param("orgCode") String orgCode);
}
