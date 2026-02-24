package ngvgroup.com.rpt.features.smrscore.mapper;

import ngvgroup.com.rpt.features.smrscore.model.SmrScoreBranch;
import ngvgroup.com.rpt.features.smrtxnscore.model.SmrTxnScoreBranch;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(componentModel = "spring")
public interface SmrScoreBranchMapper {
    @Mapping(target = "id", ignore = true)
    List<SmrScoreBranch> toSmrScoreBranchList(List<SmrTxnScoreBranch> txnBranches);
}
