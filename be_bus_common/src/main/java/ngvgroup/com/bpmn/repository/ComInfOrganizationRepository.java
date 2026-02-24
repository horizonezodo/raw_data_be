package ngvgroup.com.bpmn.repository;

import ngvgroup.com.bpmn.dto.ComInfOrganization.ComInfOrganizationDto;
import ngvgroup.com.bpmn.model.ComInfOrganization;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface ComInfOrganizationRepository extends JpaRepository<ComInfOrganization, Integer> {

    ComInfOrganization findByOrgCode(String orgCode);

    @Query(value = "SELECT new ngvgroup.com.bpmn.dto.ComInfOrganization.ComInfOrganizationDto(" +
            "c.orgCode, " +
            "case when c.orgCode = '%' then 'Tất cả' else c.orgName end) " +
            "FROM ComInfOrganization c " +
            "WHERE c.orgName is not null " +
            "AND lower(c.orgName) like lower(concat('%', :filter, '%'))")
    List<ComInfOrganizationDto> listOrganization(@Param("filter") String filter);
}
