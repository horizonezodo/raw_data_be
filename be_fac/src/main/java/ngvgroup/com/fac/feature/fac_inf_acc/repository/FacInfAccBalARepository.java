package ngvgroup.com.fac.feature.fac_inf_acc.repository;

import com.ngvgroup.bpm.core.persistence.repository.BaseRepository;
import feign.Param;
import ngvgroup.com.fac.feature.fac_inf_acc.model.FacInfAccBalA;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface FacInfAccBalARepository extends BaseRepository<FacInfAccBalA> {

    @Query("""
                SELECT
                    h
                FROM FacInfAccBalA h
                WHERE h.accNo = :accNo
                ORDER BY h.dataTime ASC
            """)
    List<FacInfAccBalA> findBalA(@Param("accNo") String accNo);

}
