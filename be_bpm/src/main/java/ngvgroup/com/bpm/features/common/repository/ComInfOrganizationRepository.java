package ngvgroup.com.bpm.features.common.repository;

import ngvgroup.com.bpm.features.common.dto.ComInfOrganizationDto;
import ngvgroup.com.bpm.features.common.model.ComInfOrganization;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ComInfOrganizationRepository extends JpaRepository<ComInfOrganization, Long> {
    @Query(value = "SELECT new ngvgroup.com.bpm.features.common.dto.ComInfOrganizationDto(" +
            "c.orgCode, " +
            "case when c.orgCode = '%' then 'Tất cả' else c.orgName end) " +
            "FROM ComInfOrganization c " +
            "WHERE c.orgName is not null " +
            "AND lower(c.orgName) like lower(concat('%', :filter, '%'))")
    List<ComInfOrganizationDto> listOrganization(@Param("filter") String filter);

    @Query("""
        select o.parentCode
        from ComInfOrganization o
        where o.orgCode = :orgCode
          and o.isDelete = 0
    """)
    String getParentCodeByOrgCode(@Param("orgCode") String orgCode);

    @Query("""
        select o.orgName
        from ComInfOrganization o
        where o.orgCode = :orgCode
          and o.isDelete = 0
    """)
    Optional<String> getOrgNameByOrgCode(@Param("orgCode") String orgCode);
}