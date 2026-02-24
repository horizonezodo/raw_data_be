package ngvgroup.com.rpt.features.smrscore.repository;

import ngvgroup.com.rpt.features.smrscore.dto.BranchScoreCommonInfo;
import ngvgroup.com.rpt.features.smrscore.dto.SmrScoreSearchDto;
import ngvgroup.com.rpt.features.smrscore.dto.SmrScoreExportExcelDto;
import ngvgroup.com.rpt.features.smrscore.model.SmrScoreBranch;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;

@Repository
public interface SmrScoreBranchRepository extends JpaRepository<SmrScoreBranch, Long> {

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
                )FROM SmrScoreBranch sb
                LEFT JOIN DimCiBrD dcbd ON (dcbd.ciId = sb.ciId AND dcbd.ciBrId = sb.ciBrId)
                WHERE sb.scoreInstanceCode =:scoreInstanceCode
                AND sb.ciId = :ciId
                AND (
                    :keyword IS NULL OR
                    LOWER(dcbd.ciBrName) LIKE LOWER(CONCAT('%', :keyword, '%')) OR
                    LOWER(dcbd.ciBrCode) LIKE LOWER(CONCAT('%', :keyword, '%')) OR
                    LOWER(sb.rankValue) LIKE LOWER(CONCAT('%', :keyword, '%')) OR
                    LOWER(TO_CHAR(sb.achievedScore)) LIKE LOWER(CONCAT('%', :keyword, '%')) OR
                    LOWER(sb.rankContent) LIKE LOWER(CONCAT('%', :keyword, '%'))
                )
            """)
    Page<BranchScoreCommonInfo> getBranchResult(
            @Param("keyword") String keyword,
            @Param("ciId") String ciId,
            @Param("scoreInstanceCode") String scoreInstanceCode,
            Pageable pageable);


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
                )FROM SmrScoreBranch sb
                LEFT JOIN DimCiBrD dcbd ON (dcbd.ciId = sb.ciId AND dcbd.ciBrId = sb.ciBrId)
                WHERE sb.scoreInstanceCode =:scoreInstanceCode
                AND sb.ciId = :ciId
                AND (
                    :keyword IS NULL OR
                    LOWER(dcbd.ciBrName) LIKE LOWER(CONCAT('%', :keyword, '%')) OR
                    LOWER(dcbd.ciBrCode) LIKE LOWER(CONCAT('%', :keyword, '%')) OR
                    LOWER(sb.rankValue) LIKE LOWER(CONCAT('%', :keyword, '%')) OR
                    LOWER(TO_CHAR(sb.achievedScore)) LIKE LOWER(CONCAT('%', :keyword, '%')) OR
                    LOWER(sb.rankContent) LIKE LOWER(CONCAT('%', :keyword, '%'))
                )
            """)
    List<BranchScoreCommonInfo> getDataExportBranchResult(
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
                )FROM SmrScoreBranch sb
                LEFT JOIN DimCiBrD dcbd ON (dcbd.ciId = sb.ciId AND dcbd.ciBrId = sb.ciBrId)
                WHERE sb.id = :id
            """)
    BranchScoreCommonInfo getDetail(@Param("id") Long id);


    @Query("""
                SELECT new ngvgroup.com.rpt.features.smrscore.dto.SmrScoreSearchDto(
                    s.id,
                    d.ciId,
                    d.ciName,
                    d.ciCode,
                    s.txnDate,
                    s.makerUserName,
                    s.scoreInstanceCode,
                    s.scorePeriod,
                    s.statScoreTypeName,
                    s.scoreDate
                )FROM SmrScore s
                LEFT JOIN DimCiD d ON s.ciId = d.ciId
                WHERE (
                    :keyword IS NULL OR
                    LOWER(d.ciName) LIKE LOWER(CONCAT('%', :keyword, '%')) OR
                    LOWER(d.ciCode) LIKE LOWER(CONCAT('%', :keyword, '%')) OR
                    LOWER(s.scoreInstanceCode) LIKE LOWER(CONCAT('%', :keyword, '%')) OR
                    LOWER(s.makerUserName) LIKE LOWER(CONCAT('%', :keyword, '%')) OR
                    LOWER(TO_CHAR(s.txnDate, 'DD-MM-YYYY')) LIKE LOWER(CONCAT('%', :keyword, '%'))
                )
                AND (
                    :startDate IS NULL OR
                    s.txnDate >= :startDate
                )
                AND (
                    :endDate IS NULL OR
                    s.txnDate <= :endDate
                )
                AND (
                    :scoreInstanceCode IS NULL OR
                    s.scoreInstanceCode =:scoreInstanceCode
                )
                AND (
                    :scoreTypeCode IS NULL OR
                    s.statScoreTypeCode =:scoreTypeCode
                )
                AND (
                    :ciId IS NULL OR
                    d.ciId=:ciId
                )
            """)
    Page<SmrScoreSearchDto> search(@Param("keyword") String keyword,
                                   @Param("startDate") Date startDate,
                                   @Param("endDate") Date endDate,
                                   @Param("ciId") String ciId,
                                   @Param("scoreInstanceCode") String scoreInstanceCode,
                                   @Param("scoreTypeCode") String scoreTypeCode,
                                   Pageable pageable);

    @Query("""
                SELECT new ngvgroup.com.rpt.features.smrscore.dto.SmrScoreExportExcelDto(
                    s.id,
                    d.ciId,
                    d.ciName,
                    d.ciCode,
                    s.txnDate,
                    s.makerUserName,
                    s.scoreInstanceCode,
                    s.scorePeriod,
                    s.statScoreTypeName
                )FROM SmrScore s
                LEFT JOIN DimCiD d ON s.ciId = d.ciId
                WHERE (
                    :keyword IS NULL OR
                    LOWER(d.ciName) LIKE LOWER(CONCAT('%', :keyword, '%')) OR
                    LOWER(d.ciCode) LIKE LOWER(CONCAT('%', :keyword, '%')) OR
                    LOWER(s.scoreInstanceCode) LIKE LOWER(CONCAT('%', :keyword, '%')) OR
                    LOWER(s.makerUserName) LIKE LOWER(CONCAT('%', :keyword, '%')) OR
                    LOWER(TO_CHAR(s.txnDate, 'DD-MM-YYYY')) LIKE LOWER(CONCAT('%', :keyword, '%'))
                )
                AND (
                    :startDate IS NULL OR
                    s.txnDate >= :startDate
                )
                AND (
                    :endDate IS NULL OR
                    s.txnDate <= :endDate
                )
                AND (
                    :scoreInstanceCode IS NULL OR
                    s.scoreInstanceCode =:scoreInstanceCode
                )
                AND (
                    :scoreTypeCode IS NULL OR
                    s.statScoreTypeCode =:scoreTypeCode
                )
                AND (
                    :ciId IS NULL OR
                    d.ciId=:ciId
                )
            """)
    List<SmrScoreExportExcelDto> getDataExportExcel(@Param("keyword") String keyword,
                                                    @Param("startDate") Date startDate,
                                                    @Param("endDate") Date endDate,
                                                    @Param("ciId") String ciId,
                                                    @Param("scoreInstanceCode") String scoreInstanceCode,
                                                    @Param("scoreTypeCode") String scoreTypeCode
    );
}
