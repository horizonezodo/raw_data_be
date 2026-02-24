package ngvgroup.com.bpmn.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import ngvgroup.com.bpmn.dto.CtgCfgReportParamBase.CtgCfgReportParamBaseResponse;
import ngvgroup.com.bpmn.model.CtgCfgReportParamBase;

import java.util.List;

@Repository
public interface CtgCfgReportParamBaseRepository extends JpaRepository<CtgCfgReportParamBase, Long> {

    @Query("select new ngvgroup.com.bpmn.dto.CtgCfgReportParamBase.CtgCfgReportParamBaseResponse(" +
            "ccrpb.id, ccrpb.paramBaseCode, ccrpb.paramBaseName, ccrpb.orgCode, ccrpb.paramBaseType, ccrpb.description, '') "
            +
            "from CtgCfgReportParamBase ccrpb " +
            "where ccrpb.isActive = 1 and (:filter is null or lower(ccrpb.paramBaseCode) like concat('%', :filter, '%') "
            +
            "or lower(ccrpb.paramBaseName) like concat('%', :filter, '%') " +
            "or lower(ccrpb.paramBaseType) like concat('%', :filter, '%')) ")
    List<CtgCfgReportParamBaseResponse> listComCfgReportParamBase(@Param("filter") String filter);

    boolean existsByParamBaseCodeAndIsActive(String paramBaseCode, Integer isActive);
}
