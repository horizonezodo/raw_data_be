package ngvgroup.com.rpt.features.report.feign;

import com.ngvgroup.bpm.core.common.dto.ResponseData;
import ngvgroup.com.rpt.features.report.dto.ComCfgParameterDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "ParameterFeignClient", url = "${service.common.url}")
public interface ParameterFeignClient {
    @GetMapping("/api/com-cfg-parameter/param-code/{paramCode}")
    ResponseData<ComCfgParameterDto> getParameterByCode(@PathVariable("paramCode") String paramCode);
}