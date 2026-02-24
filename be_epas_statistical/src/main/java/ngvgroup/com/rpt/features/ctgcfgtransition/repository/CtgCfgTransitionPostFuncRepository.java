package ngvgroup.com.rpt.features.ctgcfgtransition.repository;

import ngvgroup.com.rpt.features.ctgcfgtransition.model.CtgCfgTransitionPostFunc;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CtgCfgTransitionPostFuncRepository extends JpaRepository<CtgCfgTransitionPostFunc, Long> {
    List<CtgCfgTransitionPostFunc> findByTransitionCode(String transitionCode);

    // Methods with isDelete filter
    List<CtgCfgTransitionPostFunc> findByTransitionCodeAndIsDelete(String transitionCode, int isDelete);

    Optional<CtgCfgTransitionPostFunc> findByIdAndIsDelete(Long id, int isDelete);

    @Modifying
    @Query("DELETE FROM CtgCfgTransitionPostFunc tf WHERE tf.transitionCode = :transitionCode")
    void deleteByTransitionCode(@Param("transitionCode") String transitionCode);
}
