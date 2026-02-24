package ngvgroup.com.rpt.features.transactionreport.repository;

import ngvgroup.com.rpt.features.transactionreport.model.CtgCfgStatRegulatoryWf;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.ngvgroup.bpm.core.persistence.repository.BaseRepository;


public interface CtgCfgStatRegulatoryWfRepository extends BaseRepository<CtgCfgStatRegulatoryWf> {
    @Query("SELECT r.workflowCode FROM CtgCfgStatRegulatoryWf r WHERE r.regulatoryTypeCode = :code")
    String findWorkflowCodeByRegulatoryType(@Param("code") String code);
    CtgCfgStatRegulatoryWf findByRegulatoryTypeCode(String code);
}
