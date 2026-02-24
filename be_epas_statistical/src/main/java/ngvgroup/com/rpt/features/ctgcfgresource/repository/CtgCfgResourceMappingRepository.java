package ngvgroup.com.rpt.features.ctgcfgresource.repository;

import ngvgroup.com.rpt.features.ctgcfgresource.dto.CtgCfgResourceMappingDTO;
import ngvgroup.com.rpt.features.ctgcfgresource.model.CtgCfgResourceMapping;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CtgCfgResourceMappingRepository extends JpaRepository<CtgCfgResourceMapping,Long> {
    @Query("""
    SELECT new ngvgroup.com.rpt.features.ctgcfgresource.dto.CtgCfgResourceMappingDTO(
        c.resourceCode,
        c.resourceName
    )FROM CtgCfgResourceMapping c
    WHERE c.isActive = 1
    AND c.resourceTypeCode=:resourceTypeCode
    AND c.userId =:userId
""")
    List<CtgCfgResourceMappingDTO> getAllData(@Param("resourceTypeCode")String resourceTypeCode,@Param("userId")String userId);
}
