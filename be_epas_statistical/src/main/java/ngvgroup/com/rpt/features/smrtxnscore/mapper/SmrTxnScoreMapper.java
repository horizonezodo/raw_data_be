package ngvgroup.com.rpt.features.smrtxnscore.mapper;

import ngvgroup.com.rpt.features.smrtxnscore.dto.SmrTxnScoreDTO;

import com.ngvgroup.bpm.core.persistence.mapper.BaseMapper;
import ngvgroup.com.rpt.features.smrtxnscore.model.SmrTxnScore;
import org.mapstruct.Mapper;
import org.mapstruct.NullValuePropertyMappingStrategy;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring",nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
public interface SmrTxnScoreMapper extends BaseMapper<SmrTxnScoreDTO, SmrTxnScore> {
    SmrTxnScoreMapper INSTANCE= Mappers.getMapper(SmrTxnScoreMapper.class);
}
