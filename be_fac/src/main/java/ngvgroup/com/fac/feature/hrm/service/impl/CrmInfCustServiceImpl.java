package ngvgroup.com.fac.feature.hrm.service.impl;

import lombok.RequiredArgsConstructor;
import ngvgroup.com.fac.feature.hrm.dto.ObjectTxnDto;
import ngvgroup.com.fac.feature.hrm.repository.CrmInfCustRepository;
import ngvgroup.com.fac.feature.hrm.service.CrmInfCustService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CrmInfCustServiceImpl implements CrmInfCustService {
    private final CrmInfCustRepository repo;

    @Override
    public Page<ObjectTxnDto> getList(Pageable pageable) {
        return repo.getTxnList(pageable);
    }
}
