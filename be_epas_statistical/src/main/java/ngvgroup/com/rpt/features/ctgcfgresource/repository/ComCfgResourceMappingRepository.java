package ngvgroup.com.rpt.features.ctgcfgresource.repository;

import ngvgroup.com.rpt.features.ctgcfgresource.model.ComCfgResourceMapping;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ComCfgResourceMappingRepository extends JpaRepository<ComCfgResourceMapping, Long> {
    boolean existsByUserIdAndResourceCodeAndIsActive(String userId, String resourceCode, Integer isActive);
}
