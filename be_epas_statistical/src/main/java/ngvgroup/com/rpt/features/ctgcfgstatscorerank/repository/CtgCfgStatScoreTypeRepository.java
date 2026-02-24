package ngvgroup.com.rpt.features.ctgcfgstatscorerank.repository;

import ngvgroup.com.rpt.features.ctgcfgstatscorerank.dto.CtgCfgStatScoreTypeDto;
import ngvgroup.com.rpt.features.ctgcfgstatscorerank.model.CtgCfgStatScoreType;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CtgCfgStatScoreTypeRepository extends JpaRepository<CtgCfgStatScoreType,Long> {
    @Query("SELECT new ngvgroup.com.rpt.features.ctgcfgstatscorerank.dto.CtgCfgStatScoreTypeDto(" +
            "c.statScoreTypeCode, c.statScoreTypeName,c.description,c.id) " +
            "FROM CtgCfgStatScoreType c " +
            "WHERE (:keyword IS NULL OR " +
            "LOWER(c.statScoreTypeCode) LIKE LOWER(CONCAT('%', :keyword, '%')) OR " +
            "LOWER(c.statScoreTypeName) LIKE LOWER(CONCAT('%', :keyword, '%'))) " +
            "ORDER BY c.modifiedDate DESC ")
    Page<CtgCfgStatScoreTypeDto> getAll(@Param("keyword") String keyword, Pageable pageable);

    @Query("SELECT new ngvgroup.com.rpt.features.ctgcfgstatscorerank.dto.CtgCfgStatScoreTypeDto(" +
            "c.statScoreTypeCode, c.statScoreTypeName,c.description,c.id) " +
            "FROM CtgCfgStatScoreType c " +
            "WHERE (:keyword IS NULL OR " +
            "LOWER(c.statScoreTypeCode) LIKE LOWER(CONCAT('%', :keyword, '%')) OR " +
            "LOWER(c.statScoreTypeName) LIKE LOWER(CONCAT('%', :keyword, '%'))) " +
            "ORDER BY c.modifiedDate DESC ")
    List<CtgCfgStatScoreTypeDto> exportToExcel(@Param("keyword") String keyword);

    Optional<CtgCfgStatScoreType> findByStatScoreTypeCode(@Param("statScoreTypeCode")String statScoreTypeCode);

    CtgCfgStatScoreType getByStatScoreTypeCode(String statScoreTypeCode);

}
