package ngvgroup.com.bpm.features.com_cfg_process_file.service.impl;

import java.util.List;

import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;
import ngvgroup.com.bpm.core.base.dto.ComCfgProcessFileDto;
import ngvgroup.com.bpm.features.com_cfg_process_file.repository.ComCfgProcessFileRepository;
import ngvgroup.com.bpm.features.com_cfg_process_file.service.ComCfgProcessFileService;

@Service
@RequiredArgsConstructor
public class ComCfgProcessFileServiceImpl implements ComCfgProcessFileService {

    private final ComCfgProcessFileRepository repository;

    @Override
    public List<ComCfgProcessFileDto> getDetails(String processTypeCode) {
        return repository.findByProcessTypeCode(processTypeCode);
    }

}
