package ngvgroup.com.crm.features.common.repository;

import java.util.List;

import org.springframework.data.jpa.repository.Query;

import com.ngvgroup.bpm.core.persistence.repository.BaseRepository;

import ngvgroup.com.crm.features.common.dto.ComInfOrganizationDto;
import ngvgroup.com.crm.features.common.model.ComInfOrganization;

public interface ComInfOrganizationRepository extends BaseRepository<ComInfOrganization> {
    @Query("""
        SELECT new ngvgroup.com.crm.features.common.dto.ComInfOrganizationDto(
            o.orgCode,
            o.orgName
        )
        FROM ComInfOrganization o
        WHERE o.isDelete = 0
            """)
    List<ComInfOrganizationDto> getAllOrganizations();

    ComInfOrganization findByOrgCode(String orgCode);
}
