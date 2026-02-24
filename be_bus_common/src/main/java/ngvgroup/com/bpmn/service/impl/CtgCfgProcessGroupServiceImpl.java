package ngvgroup.com.bpmn.service.impl;

import lombok.AllArgsConstructor;
import ngvgroup.com.bpmn.dto.ComCfgProcessType.ComCfgProcessTypeDto;
import ngvgroup.com.bpmn.dto.CtgCfgProcessGroup.CtgCfgProcessGroupDto;
import ngvgroup.com.bpmn.repository.CtgCfgProcessGroupRepository;
import ngvgroup.com.bpmn.service.CtgCfgProcessGroupService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor

public class CtgCfgProcessGroupServiceImpl implements CtgCfgProcessGroupService {

    private final CtgCfgProcessGroupRepository ctgCfgProcessGroupRepository;

    @Override
    public List<CtgCfgProcessGroupDto> getListProcessGroup(){
        return ctgCfgProcessGroupRepository.getListProcessGroup();
    }



}
