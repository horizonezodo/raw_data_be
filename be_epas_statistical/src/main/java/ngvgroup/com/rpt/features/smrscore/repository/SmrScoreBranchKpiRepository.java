package ngvgroup.com.rpt.features.smrscore.repository;

import ngvgroup.com.rpt.features.smrscore.dto.BranchGroupKpiResultDto;
import ngvgroup.com.rpt.features.smrscore.model.SmrScoreBranchKpi;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SmrScoreBranchKpiRepository extends JpaRepository<SmrScoreBranchKpi,Long> {

    @Query("""
                SELECT new ngvgroup.com.rpt.features.smrscore.dto.BranchGroupKpiResultDto(
                    sbgk.id,
                    sbgk.kpiName,
                    sbgk.kpiValue,
                    sbgk.kpiCode,
                    sbgk.achievedScore,
                    sbgk.weightScore,
                    sbgk.rawScore,
                    sbgk.statScoreGroupCode
                )FROM SmrScoreBranchKpi sbgk
                WHERE sbgk.scoreInstanceCode =:scoreInstanceCode
                AND sbgk.ciId = :ciId
                AND sbgk.ciBrId = :ciBrId
                AND sbgk.statScoreGroupCode = :statScoreGroupCode
            """)
    List<BranchGroupKpiResultDto> getList(
            @Param("scoreInstanceCode")String scoreInstanceCode,
            @Param("ciId")String ciId,
            @Param("ciBrId")String ciBrId,
            @Param("statScoreGroupCode")String statScoreGroupCode
    );
}
