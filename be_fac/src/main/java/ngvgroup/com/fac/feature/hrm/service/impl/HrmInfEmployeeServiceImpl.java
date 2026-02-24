package ngvgroup.com.fac.feature.hrm.service.impl;

import lombok.RequiredArgsConstructor;
import ngvgroup.com.fac.feature.hrm.dto.ObjectTxnDto;
import ngvgroup.com.fac.feature.hrm.repository.HrmInfEmployeeRepository;
import ngvgroup.com.fac.feature.hrm.service.HrmInfEmployeeService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class HrmInfEmployeeServiceImpl implements HrmInfEmployeeService {
    private final HrmInfEmployeeRepository repo;

    @Override
    public Page<ObjectTxnDto> getList(Pageable pageable) {
        return repo.getTnxList(pageable);
    }
}
