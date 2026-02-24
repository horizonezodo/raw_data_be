package ngvgroup.com.hrm.feature.category.mapper;
import com.ngvgroup.bpm.core.persistence.mapper.BaseMapper;
import ngvgroup.com.hrm.feature.category.dto.HrmCfgTitleDTO;
import ngvgroup.com.hrm.feature.category.model.HrmCfgTitle;
import org.mapstruct.Mapper;
import org.mapstruct.NullValuePropertyMappingStrategy;

@Mapper(componentModel = "spring", nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
public interface HrmCfgTitleMapper extends BaseMapper<HrmCfgTitleDTO, HrmCfgTitle> {
}
