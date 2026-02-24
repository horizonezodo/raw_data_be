package ngvgroup.com.fac.feature.fac_inf_acc.repository;

import com.ngvgroup.bpm.core.persistence.repository.BaseRepository;
import feign.Param;
import jakarta.persistence.LockModeType;
import ngvgroup.com.fac.feature.fac_inf_acc.dto.FacInfAccDto;
import ngvgroup.com.fac.feature.fac_inf_acc.dto.FacInfAccDtoRes;
import ngvgroup.com.fac.feature.fac_inf_acc.model.FacInfAcc;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

@Repository
public interface FacInfAccRepository extends BaseRepository<FacInfAcc> {
    Optional<FacInfAcc> findByAccNo(String accNo);

    @Lock(LockModeType.PESSIMISTIC_WRITE)
    List<FacInfAcc> findByAccNoIn(Collection<String> accNos);

    @Query("""
                select distinct new ngvgroup.com.fac.feature.fac_inf_acc.dto.FacInfAccDto(
                    fa.id, fa.accNo, fa.accName, fa.bal, c.commonName, fa.accClassCode, c1.commonName, fa.balAvailable, fa.balActual, fa.modifiedDate)
                from FacInfAcc fa
                left join CtgCfgCommon c on c.commonValue = fa.accNature
                left join CtgCfgCommon c1 on c1.commonValue = fa.accStatus
                where (:accScope is null or fa.accScope in :accScope)
                    and (fa.isDelete = 0 or fa.isDelete is null)
                order by fa.modifiedDate desc
            """)
    Page<FacInfAccDto> search(
            @Param("accScope") List<String> accScope,
            Pageable pageable);

    @Query("""
                select distinct new ngvgroup.com.fac.feature.fac_inf_acc.dto.FacInfAccDto(
                    fa.id, fa.accNo, fa.accName, fa.bal, c.commonName, fa.accClassCode, c1.commonName, fa.balAvailable, fa.balActual, fa.modifiedDate)
                from FacInfAcc fa
                left join CtgCfgCommon c on c.commonValue = fa.accNature
                left join CtgCfgCommon c1 on c1.commonValue = fa.accStatus
                where (fa.isDelete = 0 or fa.isDelete is null)
                order by fa.modifiedDate desc
            """)
    List<FacInfAccDto> exportExcel();

    @Query("""
                select fa.accNo
                from FacInfAcc fa
                where fa.accClassCode = :accClassCode
                    and fa.orgCode = :orgCode
            """)
    List<String> getAccNo(
            @Param("accClassCode") String accClassCode,
            @Param("orgCode") String orgCode);

    FacInfAccDtoRes findAllByAccNo(String accNo);

    @Query("SELECT distinct f.accClassCode FROM FacInfAcc f WHERE f.accScope = 'OFF'")
    List<String> findAccClassCodesForEntry();

    @Query("SELECT new ngvgroup.com.fac.feature.fac_inf_acc.dto.FacInfAccDto(" +
            "f.id, f.accNo, f.accName, f.bal, f.accNature, f.accClassCode, f.accStatus, f.balAvailable, f.balActual, f.modifiedDate) " +
            "FROM FacInfAcc f " +
            "WHERE f.accClassCode = :accClassCode AND f.orgCode = :orgCode AND f.accScope = 'OFF'")
    List<FacInfAccDto> findAccountsByClassAndOrg(@Param("accClassCode") String accClassCode,
                                                 @Param("orgCode") String orgCode);


    @Query("SELECT new ngvgroup.com.fac.feature.fac_inf_acc.dto.FacInfAccDto(" +
            "f.accNo,f.accName,f.bal,f.accNature,f.accStatus) " +
            "FROM FacInfAcc f " +
            "WHERE f.orgCode=:orgCode")
    List<FacInfAccDto> getByOrgCode(@Param("orgCode") String orgCode);


    FacInfAcc getFacInfAccByAccNo(@Param("accNo") String accNo);
}
