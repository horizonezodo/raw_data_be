package ngvgroup.com.loan.feature.common.feign;

import com.ngvgroup.bpm.core.common.dto.ResponseData;
import ngvgroup.com.loan.feature.common.dto.ComCfgBusModuleDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;

@FeignClient(name = "ComCfgBusModuleFeignClient", url = "${service.common.url}")
public interface ComCfgBusModuleFeign {
    @GetMapping("api/bus-module/get-all")
    ResponseData<List<ComCfgBusModuleDto>> getAll();
}
