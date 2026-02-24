package ngvgroup.com.fac.feature.common.repository;

import com.ngvgroup.bpm.core.persistence.repository.BaseRepository;
import feign.Param;
import ngvgroup.com.fac.feature.common.dto.ComInfOrganizationDTO;
import ngvgroup.com.fac.feature.common.model.ComInfOrganization;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface ComInfOrganizationRepository extends BaseRepository<ComInfOrganization> {
    @Query("""
            SELECT new ngvgroup.com.fac.feature.common.dto.ComInfOrganizationDTO(
                c.orgCode,
                c.orgName
            )FROM ComInfOrganization c
            """)
    List<ComInfOrganizationDTO> getAllOrganizations();

    @Query("SELECT e.orgName FROM ComInfOrganization e WHERE e.orgCode = :orgCode")
    String findOrgNameByOrgCode(@Param("orgCode") String orgCode);

    ComInfOrganization findByOrgCode(String orgCode);
}
