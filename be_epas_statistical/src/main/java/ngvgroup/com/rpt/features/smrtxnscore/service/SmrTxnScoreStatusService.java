package ngvgroup.com.rpt.features.smrtxnscore.service;

import ngvgroup.com.rpt.features.smrtxnscore.dto.SmrTxnScoreStatusDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface SmrTxnScoreStatusService {
    List<SmrTxnScoreStatusDTO> getAllData();
    List<SmrTxnScoreStatusDTO> getAllRsData();
    Page<SmrTxnScoreStatusDTO> pageData(String keyword,String scoreInstanceCode, Pageable pageable);
}
