package ngvgroup.com.loan.feature.ward.service.impl;

import lombok.AllArgsConstructor;
import ngvgroup.com.loan.feature.ward.dto.CtgComWardDto;
import ngvgroup.com.loan.feature.ward.mapper.CtgComWardMapper;
import ngvgroup.com.loan.feature.ward.repository.CtgComWardRepository;
import ngvgroup.com.loan.feature.ward.service.CtgComWardService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
public class CtgComWardServiceImpl implements CtgComWardService {

    private final CtgComWardRepository ctgComWardRepository;
    private final CtgComWardMapper ctgComWardMapper;

    @Override
    public List<CtgComWardDto> getAll() {
        return ctgComWardRepository.findAll().stream()
                .map(ctgComWardMapper::toDto).toList();
    }
}
