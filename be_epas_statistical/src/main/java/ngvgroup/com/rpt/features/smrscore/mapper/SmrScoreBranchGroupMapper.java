package ngvgroup.com.rpt.features.smrscore.mapper;

import ngvgroup.com.rpt.features.smrscore.model.SmrScoreBranchGroup;
import ngvgroup.com.rpt.features.smrtxnscore.model.SmrTxnScoreBranchGroup;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(componentModel = "spring")
public interface SmrScoreBranchGroupMapper {
    @Mapping(target = "id", ignore = true)
    List<SmrScoreBranchGroup> toSmrScoreBranchList(List<SmrTxnScoreBranchGroup> txnBranches);
}
