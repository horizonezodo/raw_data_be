package ngvgroup.com.crm.features.common.repository;

import java.util.List;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import com.ngvgroup.bpm.core.persistence.repository.BaseRepository;

import ngvgroup.com.crm.features.common.model.ComCfgCommon;
import ngvgroup.com.crm.features.common.dto.ComCfgCommonDto;

public interface ComCfgCommonRepository extends BaseRepository<ComCfgCommon> {

    @Query("""
            SELECT new ngvgroup.com.crm.features.common.dto.ComCfgCommonDto(
                e.commonCode,
                e.commonName
            )
            FROM ComCfgCommon e
            WHERE e.commonTypeCode = :commonTypeCode
            AND e.isDelete = 0
            ORDER BY e.sortNumber ASC
            """)
    List<ComCfgCommonDto> findDtoByCommonTypeCode(@Param("commonTypeCode") String commonTypeCode);

    @Query("""
            SELECT new ngvgroup.com.crm.features.common.dto.ComCfgCommonDto(
                e.commonCode,
                e.commonName
            )
            FROM ComCfgCommon e
            WHERE e.commonTypeCode = :commonTypeCode AND e.parentCode = :parentCode
            AND e.isDelete = 0
            ORDER BY e.sortNumber ASC
            """)
    List<ComCfgCommonDto> findDtoByCommonTypeCodeAndParentCode(@Param("commonTypeCode") String commonTypeCode,
            @Param("parentCode") String parentCode);

    ComCfgCommon findByCommonCode(String code);
}