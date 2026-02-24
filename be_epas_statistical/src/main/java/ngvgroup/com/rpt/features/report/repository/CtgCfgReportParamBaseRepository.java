package ngvgroup.com.rpt.features.report.repository;

import ngvgroup.com.rpt.features.report.dto.ctgcfgreportparambase.CtgCfgReportParamBaseResponse;
import ngvgroup.com.rpt.features.report.model.CtgCfgReportParamBase;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CtgCfgReportParamBaseRepository extends JpaRepository<CtgCfgReportParamBase, Long> {

    @Query("select new ngvgroup.com.rpt.features.report.dto.ctgcfgreportparambase.CtgCfgReportParamBaseResponse(" +
            "ccrpb.id, ccrpb.paramBaseCode, ccrpb.paramBaseName, ccrpb.orgCode, ccrpb.paramBaseType, ccrpb.description, '') " +
            "from CtgCfgReportParamBase ccrpb " +
            "where ccrpb.isActive = 1 and ccrpb.isDelete = 0 and ( :filter is null " +
            "or lower(ccrpb.paramBaseCode) like concat('%', :filter, '%') " +
            "or lower(ccrpb.paramBaseName) like concat('%', :filter, '%') " +
            "or lower(ccrpb.paramBaseType) like concat('%', :filter, '%')) " +
            "order by ccrpb.modifiedDate desc ")
    List<CtgCfgReportParamBaseResponse> listComCfgReportParamBase(@Param("filter") String filter, Pageable pageable);

    boolean existsByParamBaseCodeAndIsActive(String paramBaseCode, Integer isActive);
}
