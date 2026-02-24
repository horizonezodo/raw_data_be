package ngvgroup.com.crm.features.common.repository;

import java.util.List;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import com.ngvgroup.bpm.core.persistence.repository.BaseRepository;

import ngvgroup.com.crm.features.common.model.ComInfWard;
import ngvgroup.com.crm.features.common.dto.ComInfWardDto;

public interface ComInfWardRepository extends BaseRepository<ComInfWard> {

    @Query("""
        SELECT new ngvgroup.com.crm.features.common.dto.ComInfWardDto(
            e.wardCode,
            e.wardName
        )
        FROM ComInfWard e
        WHERE e.provinceCode = :provinceCode
        AND e.isDelete = 0
        ORDER BY e.wardCode ASC
        """)
    List<ComInfWardDto> findDtoByProvinceCode(@Param("provinceCode") String provinceCode);

    @Query("""
        SELECT DISTINCT new ngvgroup.com.crm.features.common.dto.ComInfWardDto(
            e.wardCode,
            e.wardName
        )
        FROM ComInfWard e
        JOIN ComInfArea a ON e.wardCode = a.wardCode
        WHERE (:orgCode IS NULL OR a.orgCode = :orgCode)
        AND e.isDelete = 0 AND a.isDelete = 0
        ORDER BY e.wardCode ASC
        """)
    List<ComInfWardDto> findAllByOrgCode(@Param("orgCode") String orgCode);
}