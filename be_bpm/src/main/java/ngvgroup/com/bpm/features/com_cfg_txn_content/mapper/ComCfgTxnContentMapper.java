package ngvgroup.com.bpm.features.com_cfg_txn_content.mapper;

import com.ngvgroup.bpm.core.persistence.mapper.BaseMapper;
import ngvgroup.com.bpm.features.com_cfg_txn_content.dto.ComCfgTxnContentDto;
import ngvgroup.com.bpm.features.com_cfg_txn_content.model.ComCfgTxnContent;
import org.mapstruct.Mapper;
import org.mapstruct.NullValuePropertyMappingStrategy;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring",nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
public interface ComCfgTxnContentMapper extends BaseMapper<ComCfgTxnContentDto, ComCfgTxnContent> {
    ComCfgTxnContentMapper INSTANCE = Mappers.getMapper(ComCfgTxnContentMapper.class);
}
