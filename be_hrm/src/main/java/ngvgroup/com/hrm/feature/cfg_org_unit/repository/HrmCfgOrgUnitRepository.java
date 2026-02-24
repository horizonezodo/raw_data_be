package ngvgroup.com.hrm.feature.cfg_org_unit.repository;

import com.ngvgroup.bpm.core.persistence.repository.BaseRepository;
import ngvgroup.com.hrm.feature.cfg_org_unit.dto.HrmCfgOrgUnitSearch;
import ngvgroup.com.hrm.feature.cfg_org_unit.model.HrmCfgOrgUnit;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface HrmCfgOrgUnitRepository extends BaseRepository<HrmCfgOrgUnit> {
    Optional<HrmCfgOrgUnit> findByOrgUnitCode(String orgUnitCode);

    List<HrmCfgOrgUnit> findAllByIsDelete(int isDelete);

    @Query("""
        select new ngvgroup.com.hrm.feature.cfg_org_unit.dto.HrmCfgOrgUnitSearch(
                id, orgUnitCode, orgUnitName, parentCode, orgLevelCode, unitTypeCode
                ) from HrmCfgOrgUnit order by modifiedDate desc
        """)
    List<HrmCfgOrgUnitSearch> findAllByOrderByModifiedDateDesc();
}
