package ngvgroup.com.rpt.features.smrscore.mapper;

import ngvgroup.com.rpt.features.smrscore.model.SmrScoreBranchKpi;
import ngvgroup.com.rpt.features.smrtxnscore.model.SmrTxnScoreBranchKpi;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(componentModel = "spring")
public interface SmrScoreBranchKpiMapper {
    @Mapping(target = "id", ignore = true)
    List<SmrScoreBranchKpi> toSmrScoreBranchList(List<SmrTxnScoreBranchKpi> txnBranches);
}
