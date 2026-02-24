package ngvgroup.com.rpt.features.common.repository;

import ngvgroup.com.rpt.features.common.dto.ComCfgCommonDto;
import ngvgroup.com.rpt.features.common.dto.CommonDto;
import ngvgroup.com.rpt.features.common.dto.CommonResponse;
import ngvgroup.com.rpt.features.common.model.ComCfgCommon;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ComCfgCommonRepository extends JpaRepository<ComCfgCommon, Long> {

    @Query("""
                SELECT new ngvgroup.com.rpt.features.common.dto.ComCfgCommonDto(
                    c.commonTypeCode,
                    c.commonTypeName,
                    c.commonCode,
                    c.commonName,
                    c.commonValue,
                    true
                )FROM ComCfgCommon c
                WHERE c.isActive = 1
                AND c.commonCode in  (:commonCodes)
            """)
    List<ComCfgCommonDto> findAllByCommonCode(@Param("commonCodes") List<String> commonCodes);

    @Query("""
                SELECT new ngvgroup.com.rpt.features.common.dto.ComCfgCommonDto(
                    c.commonTypeCode,
                    c.commonTypeName,
                    c.commonCode,
                    c.commonName,
                    c.commonValue,
                    true
                )FROM ComCfgCommon c
                WHERE c.isActive = 1
                AND c.commonTypeCode =:commonTypeCode
            """)
    List<ComCfgCommonDto> getAllCommonTypeCode(@Param("commonTypeCode") String commonTypeCode);

    @Query("SELECT new ngvgroup.com.rpt.features.common.dto.ComCfgCommonDto(" +
            "c.commonCode,c.commonName) " +
            "FROM ComCfgCommon c " +
            "WHERE c.commonTypeCode='CM001'")
    List<ComCfgCommonDto> getAllBy();

    @Query("""
                SELECT new ngvgroup.com.rpt.features.common.dto.ComCfgCommonDto(
                    c.commonCode,
                    c.commonName
                ) from ComCfgCommon c
                WHERE c.isActive = 1
                AND c.commonTypeCode = :commonTypeCode
            """)
    List<ComCfgCommonDto> findByCommonTypeCodeAndIsActiveTrue(String commonTypeCode);

    @Query("SELECT DISTINCT new ngvgroup.com.rpt.features.common.dto.CommonResponse(" +
            "c.commonCode," +
            "c.commonName," +
            "c.commonValue" +
            ") FROM ComCfgCommon c " +
            "INNER JOIN CtgCfgStatType t ON c.commonCode = t.reportModuleCode " +
            "WHERE c.commonTypeCode = :commonTypeCode " +
            "AND c.isActive = 1 " +
            "AND t.isActive = 1")
    List<CommonResponse> findGroupsWithData(@Param("commonTypeCode") String commonTypeCode);

    @Query("""
            select new ngvgroup.com.rpt.features.common.dto.CommonResponse (
                c.commonCode,
                c.commonName,
                c.commonValue
                )
            from ComCfgCommon c
            where (c.commonTypeCode = :commonTypeCode)
            """)
    List<CommonResponse> getAllCommonByCommonTypeCode(@Param("commonTypeCode") String commonTypeCode);

    @Query("""
            select new ngvgroup.com.rpt.features.common.dto.CommonResponse (
                 c.commonCode,
                c.commonName,
                c.commonValue
                )
            from ComCfgCommon c
            where c.commonCode = :commonCode
            """)
    CommonResponse getCommonByCommonCode(@Param("commonCode") String commonCode);

    @Query("""
                SELECT new ngvgroup.com.rpt.features.common.dto.ComCfgCommonDto(
                    c.commonTypeCode,
                    c.commonTypeName,
                    c.commonCode,
                    c.commonName,
                    c.commonValue,
                    true
                )FROM ComCfgCommon c
                WHERE c.isActive = 1
                AND c.parentCode=:parentCode
            """)
    List<ComCfgCommonDto> findAllByParentCode(@Param("parentCode") String parentCode);

    @Query("""
                SELECT new ngvgroup.com.rpt.features.common.dto.ComCfgCommonDto(
                    c.commonTypeCode,
                    c.commonTypeName,
                    c.commonCode,
                    c.commonName,
                    c.commonValue,
                    true
                )FROM ComCfgCommon c
                WHERE c.isActive = 1
                AND c.commonTypeCode IN (:commonTypeCodes)
            """)
    List<ComCfgCommonDto> findAllByListCommonTypeCode(@Param("commonTypeCodes") List<String> commonTypeCodes);

    @Query("Select distinct new ngvgroup.com.rpt.features.common.dto.CommonDto(" +
            "cm.commonCode, cm.commonName, cm.parentCode) " +
            "FROM ComCfgCommon cm ")
    List<CommonDto> getAllCommon();


    ComCfgCommon findByCommonTypeCodeAndCommonCode(String type, String code);
    @Query("SELECT c.commonValue FROM ComCfgCommon c WHERE c.commonCode = :code")

    String findValueByCommonCode(@Param("code") String code);

    Optional<ComCfgCommon> findByCommonCode(String code);

    @Query("""
        SELECT new ngvgroup.com.rpt.features.common.dto.ComCfgCommonDto(
            c.commonTypeCode,
            c.commonTypeName,
            c.commonCode,
            c.commonName,
            c.commonValue,
            true
        )FROM ComCfgCommon c
        WHERE c.isActive = 1
        AND (:parentCode IS NULL OR :parentCode = c.parentCode)
        AND (c.commonTypeCode = :commonTypeCode)
    """)
    List<ComCfgCommonDto> findAllByParentCodeAndCommonTypeCode(@Param("parentCode") String parentCode,@Param("commonTypeCode") String commonTypeCode);

}
