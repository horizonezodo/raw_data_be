package ngvgroup.com.rpt.features.transactionreport.repository;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.ngvgroup.bpm.core.persistence.repository.BaseRepository;

import ngvgroup.com.rpt.features.transactionreport.model.ComCfgParameter;

public interface ComCfgParameterRepository extends BaseRepository<ComCfgParameter> {

    @Query("SELECT c.paramValue FROM ComCfgParameter c WHERE c.paramCode = :paramCode AND c.orgCode = :orgCode")
    String findParamValueByParamCodeAndOrgCode(@Param("paramCode") String paramCode, @Param("orgCode") String orgCode);

}
