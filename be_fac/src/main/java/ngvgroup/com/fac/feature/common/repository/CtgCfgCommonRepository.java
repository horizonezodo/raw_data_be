package ngvgroup.com.fac.feature.common.repository;

import ngvgroup.com.fac.feature.common.dto.CtgCfgCommonDTO;
import ngvgroup.com.fac.feature.common.model.CtgCfgCommon;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CtgCfgCommonRepository extends JpaRepository<CtgCfgCommon, Long> {

    @Query("""
            SELECT new ngvgroup.com.fac.feature.common.dto.CtgCfgCommonDTO(
            c.commonTypeCode,
            c.commonTypeName,
            c.commonCode,
            c.commonName,
            c.commonValue,
            c.parentCode
            )FROM CtgCfgCommon  c
            WHERE (:commonTypeCode IS NULL OR c.commonTypeCode =:commonTypeCode)
              AND (:parentCode IS NULL OR c.parentCode =:parentCode)
            """)
    List<CtgCfgCommonDTO> getByTypeCodeAndParentCode(
            @Param("commonTypeCode") String commonTypeCode,
            @Param("parentCode") String parentCode);

    @Query("""
            SELECT new ngvgroup.com.fac.feature.common.dto.CtgCfgCommonDTO(
            c.commonTypeCode,
            c.commonTypeName,
            c.commonCode,
            c.commonName,
            c.commonValue,
            c.parentCode)
            FROM CtgCfgCommon c
            WHERE c.commonValue =:commonValue
            """)
    CtgCfgCommonDTO getCommonByCommonValue(@Param("commonValue") String commonValue);

    @Query("""
            SELECT new ngvgroup.com.fac.feature.common.dto.CtgCfgCommonDTO(
            c.commonTypeCode,
            c.commonTypeName,
            c.commonCode,
            c.commonName,
            c.commonValue,
            c.parentCode)
            FROM CtgCfgCommon c
            WHERE c.commonCode in :commonCode
            """)
    List<CtgCfgCommonDTO> getList(@Param("commonCode") List<String> commonCode);

    @Query("""
                select distinct new ngvgroup.com.fac.feature.common.dto.CtgCfgCommonDTO(
                    c.commonTypeCode, c.commonTypeName, c.commonCode, c.commonName, c.commonValue, c.parentCode )
                from CtgCfgCommon c
                where c.commonTypeCode = :commonTypeCode AND
                      c.parentCode = :moduleCode
            """)
    List<CtgCfgCommonDTO> getListEntryTypeCode(@Param("moduleCode") String moduleCode,
                                               @Param("commonTypeCode") String commonTypeCode);
}


