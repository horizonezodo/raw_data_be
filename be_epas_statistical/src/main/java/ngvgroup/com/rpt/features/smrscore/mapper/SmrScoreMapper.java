package ngvgroup.com.rpt.features.smrscore.mapper;

import ngvgroup.com.rpt.features.smrscore.model.SmrScore;

import ngvgroup.com.rpt.features.smrtxnscore.model.SmrTxnScore;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(componentModel = "spring")
public interface SmrScoreMapper {

    @Mapping(target = "id", ignore = true)
    SmrScore toSmrScore(SmrTxnScore txnScore);

    List<SmrScore> toSmrScoreList(List<SmrTxnScore> txnScores);

}
