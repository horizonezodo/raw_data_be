package ngvgroup.com.rpt.features.smrscore.repository;

import ngvgroup.com.rpt.features.smrscore.dto.BranchGroupInfo;
import ngvgroup.com.rpt.features.smrscore.model.SmrScoreBranchGroup;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;


@Repository
public interface SmrScoreBranchGroupRepository extends JpaRepository<SmrScoreBranchGroup, Long> {
    @Query("""
                SELECT new ngvgroup.com.rpt.features.smrscore.dto.BranchGroupInfo(
                    sbg.id,
                    sbg.achievedScore,
                    sbg.weightScore,
                    sbg.rawScore,
                    sbg.statScoreGroupName,
                    sbg.statScoreGroupCode,
                    sbg.scoreInstanceCode,
                    sbg.ciBrId,
                    sbg.ciId
                )FROM SmrScoreBranchGroup sbg
                WHERE sbg.scoreInstanceCode =:scoreInstanceCode
                AND sbg.ciId = :ciId
                AND sbg.ciBrId = :ciBrId
            """)
    List<BranchGroupInfo> getList(
            @Param("scoreInstanceCode") String scoreInstanceCode,
            @Param("ciId") String ciId,
            @Param("ciBrId") String ciBrId);
}
