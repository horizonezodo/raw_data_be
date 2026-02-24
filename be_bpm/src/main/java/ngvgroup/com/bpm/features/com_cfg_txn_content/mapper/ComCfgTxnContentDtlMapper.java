package ngvgroup.com.bpm.features.com_cfg_txn_content.mapper;

import com.ngvgroup.bpm.core.persistence.mapper.BaseMapper;
import ngvgroup.com.bpm.features.com_cfg_txn_content.dto.ComCfgTxnContentDtlDto;
import ngvgroup.com.bpm.features.com_cfg_txn_content.model.ComCfgTxnContentDtl;
import org.mapstruct.Mapper;
import org.mapstruct.NullValuePropertyMappingStrategy;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring",nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
public interface ComCfgTxnContentDtlMapper extends BaseMapper<ComCfgTxnContentDtlDto, ComCfgTxnContentDtl> {
    ComCfgTxnContentDtlMapper INSTANCE = Mappers.getMapper(ComCfgTxnContentDtlMapper.class);
}
