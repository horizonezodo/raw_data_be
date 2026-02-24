package ngvgroup.com.fac.feature.common.service.impl;

import lombok.AllArgsConstructor;
import ngvgroup.com.fac.feature.common.dto.ComCfgProcessFileDto;
import ngvgroup.com.fac.feature.common.repository.ComCfgProcessFileRepository;
import ngvgroup.com.fac.feature.common.service.ComCfgProcessFileService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
public class ComCfgProcessFileServiceImpl implements ComCfgProcessFileService {
    private ComCfgProcessFileRepository comCfgProcessFileRepository;

    @Override
    public List<ComCfgProcessFileDto> getByProcessTypeCode(String processTypeCode){
        return comCfgProcessFileRepository.getByProcessTypeCode(processTypeCode);
    }
}
