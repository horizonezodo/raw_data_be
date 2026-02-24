package ngvgroup.com.bpm.core.base.feign;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import com.ngvgroup.bpm.core.common.dto.ResponseData;

import ngvgroup.com.bpm.core.base.dto.ComCfgParameterDto;

@FeignClient(name = "ComCfgParameterFeignClient", url = "${client.epas-common}", path = "/api/com-cfg-parameter")
public interface ComCfgParameterFeignClient {
    @GetMapping("/param-code/{paramCode}")
    public ResponseEntity<ResponseData<ComCfgParameterDto>> getComCfgParameterByCode(@PathVariable String paramCode);
}
