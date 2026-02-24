package ngvgroup.com.rpt.features.smrtxnscore.repository;

import ngvgroup.com.rpt.features.smrscore.dto.BranchScoreCommonInfo;
import ngvgroup.com.rpt.features.smrtxnscore.model.SmrTxnScoreBranch;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SmrTxnScoreBranchRepository extends JpaRepository<SmrTxnScoreBranch, Long> {
    List<SmrTxnScoreBranch> findAllByScoreInstanceCode(String scoreInstanceCode);

    @Query("""
                SELECT new ngvgroup.com.rpt.features.smrscore.dto.BranchScoreCommonInfo(
                    stb.id,
                    dcbd.ciBrName,
                    dcbd.ciBrCode,
                    stb.achievedScore,
                    stb.rankValue,
                    stb.scoreInstanceCode,
                    stb.ciId,
                    stb.ciBrId,
                    stb.rankContent
                )FROM SmrTxnScoreBranch stb
                LEFT JOIN DimCiBrD dcbd ON (dcbd.ciId = stb.ciId AND dcbd.ciBrId = stb.ciBrId)
                WHERE stb.scoreInstanceCode =:scoreInstanceCode
                AND stb.ciId = :ciId
                AND (
                    :keyword IS NULL OR
                    LOWER(dcbd.ciBrName) LIKE LOWER(CONCAT('%', :keyword, '%')) OR
                    LOWER(dcbd.ciBrCode) LIKE LOWER(CONCAT('%', :keyword, '%')) OR
                    LOWER(stb.rankValue) LIKE LOWER(CONCAT('%', :keyword, '%')) OR
                    LOWER(dcbd.ciBrCode) LIKE LOWER(CONCAT('%', :keyword, '%')) OR
                    LOWER(TO_CHAR(stb.achievedScore)) LIKE LOWER(CONCAT('%', :keyword, '%')) OR
                    LOWER(stb.rankContent) LIKE LOWER(CONCAT('%', :keyword, '%'))
                )
            """)
    Page<BranchScoreCommonInfo> getBranch(
            @Param("keyword") String keyword,
            @Param("ciId") String ciId,
            @Param("scoreInstanceCode") String scoreInstanceCode,
            Pageable pageable);

    @Query("""
                SELECT new ngvgroup.com.rpt.features.smrscore.dto.BranchScoreCommonInfo(
                    stb.id,
                    dcbd.ciBrName,
                    dcbd.ciBrCode,
                    stb.achievedScore,
                    stb.rankValue,
                    stb.scoreInstanceCode,
                    stb.ciId,
                    stb.ciBrId,
                    stb.rankContent
                )FROM SmrTxnScoreBranch stb
                LEFT JOIN DimCiBrD dcbd ON (dcbd.ciId = stb.ciId AND dcbd.ciBrId = stb.ciBrId)
                WHERE stb.scoreInstanceCode =:scoreInstanceCode
                AND stb.ciId = :ciId
                AND (
                    :keyword IS NULL OR
                    LOWER(dcbd.ciBrName) LIKE LOWER(CONCAT('%', :keyword, '%')) OR
                    LOWER(dcbd.ciBrCode) LIKE LOWER(CONCAT('%', :keyword, '%')) OR
                    LOWER(stb.rankValue) LIKE LOWER(CONCAT('%', :keyword, '%')) OR
                    LOWER(TO_CHAR(stb.achievedScore)) LIKE LOWER(CONCAT('%', :keyword, '%')) OR
                    LOWER(stb.rankContent) LIKE LOWER(CONCAT('%', :keyword, '%'))
                )
            """)
    List<BranchScoreCommonInfo> getDataExportBranch(
            @Param("keyword") String keyword,
            @Param("ciId") String ciId,
            @Param("scoreInstanceCode") String scoreInstanceCode
    );


    @Query("""
                SELECT new ngvgroup.com.rpt.features.smrscore.dto.BranchScoreCommonInfo(
                    sb.id,
                    dcbd.ciBrName,
                    dcbd.ciBrCode,
                    sb.achievedScore,
                    sb.rankValue,
                    sb.scoreInstanceCode,
                    sb.ciId,
                    sb.ciBrId,
                    sb.rankContent
                )FROM SmrTxnScoreBranch sb
                LEFT JOIN DimCiBrD dcbd ON (dcbd.ciId = sb.ciId AND dcbd.ciBrId = sb.ciBrId)
                WHERE sb.id = :id
            """)
    BranchScoreCommonInfo getDetail(@Param("id") Long id);
}
