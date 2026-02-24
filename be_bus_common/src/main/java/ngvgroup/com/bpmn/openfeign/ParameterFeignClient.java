package ngvgroup.com.bpmn.openfeign;

import ngvgroup.com.bpmn.dto.ComCfgParameter.ComCfgParameterDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import com.ngvgroup.bpm.core.dto.ResponseData;

@FeignClient(name = "ParameterFeignClient", url = "${service.common.url}")
public interface ParameterFeignClient {
    @GetMapping("/api/com-cfg-parameter/param-code/{paramCode}")
    ResponseData<ComCfgParameterDto> getParameterByCode(@PathVariable("paramCode") String paramCode);
}