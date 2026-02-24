package ngvgroup.com.rpt.features.ctgcfgtransition.repository;

import ngvgroup.com.rpt.features.ctgcfgtransition.model.CtgCfgTransitionCond;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CtgCfgTransitionCondRepository extends JpaRepository<CtgCfgTransitionCond, Long> {
    List<CtgCfgTransitionCond> findByTransitionCode(String transitionCode);

    // Methods with isDelete filter
    List<CtgCfgTransitionCond> findByTransitionCodeAndIsDelete(String transitionCode, int isDelete);

    Optional<CtgCfgTransitionCond> findByIdAndIsDelete(Long id, int isDelete);

    @Modifying
    @Query("DELETE FROM CtgCfgTransitionCond tc WHERE tc.transitionCode = :transitionCode")
    void deleteByTransitionCode(@Param("transitionCode") String transitionCode);
}
