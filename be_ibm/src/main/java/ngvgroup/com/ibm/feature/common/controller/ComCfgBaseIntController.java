package ngvgroup.com.ibm.feature.common.controller;

import com.ngvgroup.bpm.core.web.controller.BaseController;
import ngvgroup.com.ibm.feature.common.dto.ComCfgBaseIntDto;
import ngvgroup.com.ibm.feature.common.model.ComCfgBaseInt;
import ngvgroup.com.ibm.feature.common.service.ComCfgBaseIntService;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/base-int")
public class ComCfgBaseIntController extends BaseController<ComCfgBaseInt, ComCfgBaseIntDto, ComCfgBaseIntService> {

    public ComCfgBaseIntController(ComCfgBaseIntService service) {
        super(service);
    }
}
