package ngvgroup.com.ibm.feature.common.service.impl;

import com.ngvgroup.bpm.core.persistence.service.impl.BaseServiceImpl;
import ngvgroup.com.ibm.feature.common.dto.ComCfgBaseIntDto;
import ngvgroup.com.ibm.feature.common.mapper.ComCfgBaseIntMapper;
import ngvgroup.com.ibm.feature.common.model.ComCfgBaseInt;
import ngvgroup.com.ibm.feature.common.repository.ComCfgBaseIntRepository;
import ngvgroup.com.ibm.feature.common.service.ComCfgBaseIntService;
import org.springframework.stereotype.Service;

@Service
public class ComCfgBaseIntServiceImpl extends BaseServiceImpl<ComCfgBaseInt, ComCfgBaseIntDto> implements ComCfgBaseIntService {

    protected ComCfgBaseIntServiceImpl(ComCfgBaseIntRepository comCfgBaseIntRepository,
                                       ComCfgBaseIntMapper comCfgBaseIntMapper) {
        super(comCfgBaseIntRepository, comCfgBaseIntMapper);
    }

}
