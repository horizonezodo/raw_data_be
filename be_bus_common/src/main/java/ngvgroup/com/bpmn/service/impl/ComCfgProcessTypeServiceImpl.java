package ngvgroup.com.bpmn.service.impl;

import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import ngvgroup.com.bpmn.dto.ComCfgProcessType.ComCfgProcessTypeDto;
import ngvgroup.com.bpmn.repository.ComCfgProcessTypeRepository;
import ngvgroup.com.bpmn.service.ComCfgProcessTypeService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ComCfgProcessTypeServiceImpl implements ComCfgProcessTypeService {
    private final ComCfgProcessTypeRepository comCfgProcessTypeRepository;

    @Override
    public List<ComCfgProcessTypeDto> getListProcessType(){
        return comCfgProcessTypeRepository.getListProcessType();
    }

    @Override
    public List<ComCfgProcessTypeDto> getListProcessTypeByGroupCode(String processGroupCode){
        return comCfgProcessTypeRepository.getListProcessTypeByGroupCode(processGroupCode);
    }
}
