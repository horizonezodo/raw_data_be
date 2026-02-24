package ngvgroup.com.rpt.features.smrtxnscore.service.impl;

import lombok.RequiredArgsConstructor;
import ngvgroup.com.rpt.features.smrtxnscore.dto.SmrTxnScoreStatusDTO;
import ngvgroup.com.rpt.features.smrtxnscore.repository.SmrTxnScoreStatusRepository;
import ngvgroup.com.rpt.features.smrtxnscore.service.SmrTxnScoreStatusService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class SmrTxnScoreStatusServiceImpl implements SmrTxnScoreStatusService {
    private final SmrTxnScoreStatusRepository repo;

    @Override
    public List<SmrTxnScoreStatusDTO> getAllData() {
        return repo.getAllData();
    }

    @Override
    public List<SmrTxnScoreStatusDTO> getAllRsData() {
        return repo.getAllRsData();
    }

    @Override
    public Page<SmrTxnScoreStatusDTO> pageData(String keyword, String scoreInstanceCode, Pageable pageable) {
        return this.repo.pageData(keyword,scoreInstanceCode, pageable);
    }
}
