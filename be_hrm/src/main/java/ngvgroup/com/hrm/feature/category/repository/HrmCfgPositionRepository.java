package ngvgroup.com.hrm.feature.category.repository;

import com.ngvgroup.bpm.core.persistence.repository.BaseRepository;
import ngvgroup.com.hrm.feature.category.dto.ExportHrmCfgPositionDTO;
import ngvgroup.com.hrm.feature.category.dto.HrmCfgPositionDTO;
import ngvgroup.com.hrm.feature.category.model.HrmCfgPosition;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface HrmCfgPositionRepository extends BaseRepository<HrmCfgPosition> {
    @Query("""
    select new ngvgroup.com.hrm.feature.category.dto.HrmCfgPositionDTO(
        p.id, p.orgCode, p.positionCode, p.positionName, p.isManager, p.orgLevelCode,
        c.commonName, p.titleCode, p.isActive, p.description, t.titleName
    )
    from HrmCfgPosition p
    JOIN HrmCfgTitle t on p.titleCode = t.titleCode
    JOIN CtgComCommon c on p.orgLevelCode = c.commonCode
    where (:keyword is null or :keyword = ''
        or LOWER(p.positionCode) like concat('%', LOWER(:keyword), '%')
        or LOWER(p.positionName) like concat('%', LOWER(:keyword), '%')
        or LOWER(t.titleName) like concat('%', LOWER(:keyword), '%')
        or LOWER(p.orgLevelCode) like concat('%', LOWER(:keyword), '%')
        or LOWER(c.commonName) like concat('%', LOWER(:keyword), '%')
        or (
            (:keyword = 'có' and p.isManager = 1)
            or (:keyword = 'không' and p.isManager = 0)
        )
    )
    order by p.modifiedDate desc
    """)
    Page<HrmCfgPositionDTO> search(@Param("keyword") String keyword, Pageable pageable);

    @Query("""
    select new ngvgroup.com.hrm.feature.category.dto.ExportHrmCfgPositionDTO(
        p.positionCode, p.positionName, t.titleName, p.orgLevelCode, p.isManager
    )
    from HrmCfgPosition p
    JOIN HrmCfgTitle t on p.titleCode = t.titleCode
    """)
    List<ExportHrmCfgPositionDTO> exportData();

    Optional<HrmCfgPosition> findByPositionCode(String positionCode);

    @Query("SELECT p FROM HrmCfgPosition p where p.isDelete = :isDelete and (p.positionName = :positionName or p.positionCode = :positionCOde)")
    HrmCfgPosition checkPosition(@Param("positionCOde") String positionCOde, @Param("positionName") String positionName, @Param("isDelete")int isDelete);

    boolean existsByPositionCode(String positionCode);
}
