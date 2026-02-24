package ngvgroup.com.rpt.features.ctgcfgstatscorerank.repository;

import ngvgroup.com.rpt.features.ctgcfgstatscorerank.dto.CtgCfgStatScoreGroupDto;
import ngvgroup.com.rpt.features.ctgcfgstatscorerank.dto.CtgCfgStatScoreGroupResponse;
import ngvgroup.com.rpt.features.ctgcfgstatscorerank.dto.StatScoreGroupExcel;
import ngvgroup.com.rpt.features.ctgcfgstatscorerank.model.CtgCfgStatScoreGroup;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CtgCfgStatScoreGroupRepository extends JpaRepository<CtgCfgStatScoreGroup, Long>, JpaSpecificationExecutor<CtgCfgStatScoreGroup> {
    boolean existsByStatScoreGroupCode(String statScoreGroupCode);

    Optional<CtgCfgStatScoreGroup> getByStatScoreGroupCode(String statScoreGroupCode);


    CtgCfgStatScoreGroup getCtgCfgStatScoreGroupByStatScoreGroupCode(String statScoreGroupCode);

    @Query("SELECT new ngvgroup.com.rpt.features.ctgcfgstatscorerank.dto.CtgCfgStatScoreGroupDto(" +
            "c.statScoreGroupCode, c.statScoreGroupName) " +
            "FROM CtgCfgStatScoreGroup c " +
            "WHERE c.statScoreTypeCode = :statScoreTypeCode " +
            "AND (:keyword IS NULL OR " +
            "LOWER(c.statScoreGroupCode) LIKE LOWER(CONCAT('%', :keyword, '%')) OR " +
            "LOWER(c.statScoreGroupName) LIKE LOWER(CONCAT('%', :keyword, '%')))" +
            "ORDER BY c.sortNumber ASC ")
    Page<CtgCfgStatScoreGroupDto> searchAll(@Param("keyword") String keyword,
                                            @Param("statScoreTypeCode") String statScoreTypeCode,
                                            Pageable pageable);

    @Query("""
                            select new ngvgroup.com.rpt.features.ctgcfgstatscorerank.dto.CtgCfgStatScoreGroupResponse(
                            g.orgCode, g.statScoreGroupName, t.statScoreTypeName, g.description, g.isActive, g.statScoreGroupCode, g.statScoreTypeCode, g.recordStatus,
                            g.sortNumber, g.statScoreGroupTypeCode, g.statScoreGroupTypeName, g.weightScore
                            )
                            from CtgCfgStatScoreGroup g
                            left join CtgCfgStatScoreType t on g.statScoreTypeCode = t.statScoreTypeCode
                            where(:keyword IS NULL
                                  OR LOWER(g.statScoreGroupName) LIKE LOWER(CONCAT('%', :keyword, '%'))
                                  OR LOWER(t.statScoreTypeName) LIKE LOWER(CONCAT('%', :keyword, '%')))
            """)
    Page<CtgCfgStatScoreGroupResponse> getAll(@Param("keyword") String keyword, Pageable pageable);

    @Query("""
        SELECT new ngvgroup.com.rpt.features.ctgcfgstatscorerank.dto.StatScoreGroupExcel(
        g.statScoreGroupCode,
        g.statScoreGroupName,
        t.statScoreTypeName,
        g.description
        )
        FROM CtgCfgStatScoreGroup g
        LEFT JOIN CtgCfgStatScoreType t on g.statScoreTypeCode = t.statScoreTypeCode
""")
    List<StatScoreGroupExcel> findAllWithType();

}
