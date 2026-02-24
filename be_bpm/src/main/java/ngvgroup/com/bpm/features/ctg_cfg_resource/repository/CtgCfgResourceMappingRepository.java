package ngvgroup.com.bpm.features.ctg_cfg_resource.repository;

import com.ngvgroup.bpm.core.persistence.repository.BaseRepository;
import ngvgroup.com.bpm.features.ctg_cfg_resource.model.CtgCfgResourceMapping;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CtgCfgResourceMappingRepository extends BaseRepository<CtgCfgResourceMapping> {
    List<CtgCfgResourceMapping> findAllByUserIdAndIsDelete(String userId, int isDelete);

    boolean existsByUserIdAndResourceTypeCodeAndResourceCodeAndIsDelete(String userId, String resourceTypeCode,
            String resourceCode, Integer isDelete);
}
