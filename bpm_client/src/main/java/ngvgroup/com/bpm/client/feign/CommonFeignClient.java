package ngvgroup.com.bpm.client.feign;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import com.ngvgroup.bpm.core.common.dto.ResponseData;

import ngvgroup.com.bpm.client.dto.shared.TemplateResDto;

@FeignClient(name = "common-service", url = "${service.common.url}", path = "/api")
public interface CommonFeignClient {

    @GetMapping("template/get-detail/{templateCode}")
    public ResponseData<TemplateResDto> getDetail(@PathVariable String templateCode);
}
