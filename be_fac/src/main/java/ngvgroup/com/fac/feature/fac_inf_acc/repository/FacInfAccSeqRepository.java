package ngvgroup.com.fac.feature.fac_inf_acc.repository;

import com.ngvgroup.bpm.core.persistence.repository.BaseRepository;
import feign.Param;
import ngvgroup.com.fac.feature.fac_inf_acc.model.FacInfAccSeq;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface FacInfAccSeqRepository extends BaseRepository<FacInfAccSeq> {

    @Query("""
        select s
        from FacInfAccSeq s
        where s.accStructureCode = :accStructureCode
          and s.accType = :accType
          and (:orgCode is null or s.orgCode = :orgCode)
          and (:accDomain is null or s.accDomain = :accDomain)
          and (:currencyCode is null or s.currencyCode = :currencyCode)
        order by s.modifiedDate desc
    """)
    List<FacInfAccSeq> findLatestList(
            @Param("accStructureCode") String accStructureCode,
            @Param("accType") String accType,
            @Param("orgCode") String orgCode,
            @Param("accDomain") String accDomain,
            @Param("currencyCode") String currencyCode
    );

    default FacInfAccSeq findLatest(
            String accStructureCode,
            String accType,
            String orgCode,
            String accDomain,
            String currencyCode
    ) {
        return findLatestList(
                accStructureCode,
                accType,
                orgCode,
                accDomain,
                currencyCode
        ).stream().findFirst().orElse(null);
    }
}
