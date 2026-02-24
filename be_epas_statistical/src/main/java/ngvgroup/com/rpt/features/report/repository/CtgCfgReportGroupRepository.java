package ngvgroup.com.rpt.features.report.repository;

import jakarta.transaction.Transactional;
import ngvgroup.com.rpt.features.report.dto.ctgcfgreportgroup.CtgCfgReportGroupDto;
import ngvgroup.com.rpt.features.report.model.CtgCfgReportGroup;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.math.BigInteger;
import java.util.List;
import java.util.Optional;

@Repository
public interface CtgCfgReportGroupRepository extends JpaRepository<CtgCfgReportGroup, Long> {

        @Query("SELECT new ngvgroup.com.rpt.features.report.dto.ctgcfgreportgroup.CtgCfgReportGroupDto(" +
                        "c.reportGroupCode,c.reportGroupName,c.reportGroupNameEn,c.sortNumber) " +
                        "FROM CtgCfgReportGroup c " +
                        "ORDER BY c.modifiedDate DESC ")
        Page<CtgCfgReportGroupDto> getListReportGroup(Pageable pageable);

        @Query("SELECT new ngvgroup.com.rpt.features.report.dto.ctgcfgreportgroup.CtgCfgReportGroupDto(" +
                "c.reportGroupCode, c.reportGroupName, c.reportGroupNameEn, c.sortNumber) " +
                "FROM CtgCfgReportGroup c " +
                "WHERE :keyword IS NULL OR (" +
                "LOWER(c.reportGroupCode) LIKE LOWER(CONCAT('%', :keyword, '%')) OR " +
                "LOWER(c.reportGroupName) LIKE LOWER(CONCAT('%', :keyword, '%')) OR " +
                "LOWER(TO_CHAR(c.sortNumber)) LIKE LOWER(CONCAT('%', :keyword, '%')) OR " +
                "LOWER(c.reportGroupNameEn) LIKE LOWER(CONCAT('%', :keyword, '%'))" +
                ") " +
                "ORDER BY c.modifiedDate DESC ")
        Page<CtgCfgReportGroupDto> findListReportGroupDto(@Param("keyword") String keyword, Pageable pageable);


        @Query("SELECT new ngvgroup.com.rpt.features.report.dto.ctgcfgreportgroup.CtgCfgReportGroupDto(" +
                "c.reportGroupCode, c.reportGroupName, c.reportGroupNameEn, c.sortNumber) " +
                "FROM CtgCfgReportGroup c " +
                "WHERE :keyword IS NULL OR (" +
                "LOWER(c.reportGroupCode) LIKE LOWER(CONCAT('%', :keyword, '%')) OR " +
                "LOWER(c.reportGroupName) LIKE LOWER(CONCAT('%', :keyword, '%')) OR " +
                "LOWER(STR(c.sortNumber)) LIKE LOWER(CONCAT('%', :keyword, '%')) OR " +
                "LOWER(c.reportGroupNameEn) LIKE LOWER(CONCAT('%', :keyword, '%')) " +
                ") " +
                "ORDER BY c.modifiedDate DESC ")
        List<CtgCfgReportGroupDto> exportToExcel(@Param("keyword") String keyword);

        Optional<CtgCfgReportGroup> findComCfgReportGroupByReportGroupCode(String reportGroupCode);

        @Modifying
        @Transactional
        @Query("UPDATE CtgCfgReportGroup c SET " +
                        "c.reportGroupName=:reportGroupName,c.sortNumber=:sortNumber," +
                        "c.description=:description ,c.modifiedDate= CURRENT_TIMESTAMP " +
                        "WHERE c.reportGroupCode=:reportGroupCode")
        void updateReportGroup(@Param("reportGroupCode") String reportGroupCode,@Param("sortNumber") BigInteger sortNumber,
                        @Param("reportGroupName") String reportGroupName, @Param("description") String description);

        @Query("SELECT new ngvgroup.com.rpt.features.report.dto.ctgcfgreportgroup.CtgCfgReportGroupDto(" +
                        "c.reportGroupCode,c.reportGroupName,c.reportGroupNameEn,c.description,c.sortNumber) " +
                        "FROM CtgCfgReportGroup c " +
                        "WHERE c.reportGroupCode=:reportGroupCode")
        CtgCfgReportGroupDto getInfoReportGroup(@Param("reportGroupCode") String reportGroupCode);

        void deleteComCfgReportGroupByReportGroupCode(String reportGroupCode);

        @Query("SELECT new ngvgroup.com.rpt.features.report.dto.ctgcfgreportgroup.CtgCfgReportGroupDto(" +
                        "c.reportGroupCode,c.reportGroupName) " +
                        "FROM CtgCfgReportGroup c")
        List<CtgCfgReportGroupDto> getListReportGroupDto();
}
