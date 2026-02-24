package ngvgroup.com.bpm.features.common.service.impl;
import lombok.AllArgsConstructor;
import ngvgroup.com.bpm.features.common.feign.ComCfgBusModuleFeign;
import ngvgroup.com.bpm.features.common.service.ComCfgBusModuleService;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class ComCfgBusModuleServiceImpl implements ComCfgBusModuleService {
    private final ComCfgBusModuleFeign comCfgBusModuleFeign;

    @Override
    public ResponseEntity<Object> getAll(){
        return ResponseEntity.ok(comCfgBusModuleFeign.getAll());
    }
}
