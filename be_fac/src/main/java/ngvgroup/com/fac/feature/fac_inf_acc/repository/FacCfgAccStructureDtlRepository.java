package ngvgroup.com.fac.feature.fac_inf_acc.repository;

import com.ngvgroup.bpm.core.persistence.repository.BaseRepository;
import ngvgroup.com.fac.feature.fac_inf_acc.model.FacCfgAccStructureDtl;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface FacCfgAccStructureDtlRepository extends BaseRepository<FacCfgAccStructureDtl> {
    Optional<FacCfgAccStructureDtl> findByAccStructureCodeAndSegmentCode(
            String accStructureCode,
            String segmentCode
    );

    List<FacCfgAccStructureDtl>
    findByAccStructureCodeOrderBySortNumber(String accStructureCode);
}
