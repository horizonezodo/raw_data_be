package ngvgroup.com.bpm.features.common.controller;
import lombok.AllArgsConstructor;
import ngvgroup.com.bpm.features.common.service.ComCfgBusModuleService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("bus-module")
@AllArgsConstructor
public class ComCfgBusModuleController {
    private final ComCfgBusModuleService comCfgBusModuleService;

    @GetMapping
    public ResponseEntity<Object> getAll() {
        return comCfgBusModuleService.getAll();
    }
}
