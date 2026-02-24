package ngvgroup.com.rpt.features.comcfg.service;

import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;

public interface ComCfgTemplateService {

    ResponseEntity<Object> getAll(String keyword, Pageable pageable);

}
