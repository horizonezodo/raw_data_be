package ngvgroup.com.crm.features.common.repository;

import java.util.List;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import com.ngvgroup.bpm.core.persistence.repository.BaseRepository;

import ngvgroup.com.crm.features.common.model.ComInfArea;
import ngvgroup.com.crm.features.common.dto.ComInfAreaDto;

public interface ComInfAreaRepository extends BaseRepository<ComInfArea> {

    @Query("""
        SELECT new ngvgroup.com.crm.features.common.dto.ComInfAreaDto(
            e.areaCode,
            e.areaName
        )
        FROM ComInfArea e
        WHERE e.isDelete = 0
        AND (:orgCode IS NULL OR e.orgCode = :orgCode)
        ORDER BY e.areaCode ASC
        """)
    List<ComInfAreaDto> findDtoByOrgCode(@Param("orgCode") String orgCode);

    @Query("""
        SELECT new ngvgroup.com.crm.features.common.dto.ComInfAreaDto(
            e.areaCode,
            e.areaName,
            e.wardCode
        )
        FROM ComInfArea e
        WHERE e.isDelete = 0
        """)
    List<ComInfAreaDto> findAllDto();
}