package ngvgroup.com.bpm.client.feign;

import ngvgroup.com.bpm.client.dto.shared.ReportReqDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import jakarta.validation.Valid;

import com.ngvgroup.bpm.core.common.dto.ResponseData;

import ngvgroup.com.bpm.client.dto.shared.TemplateResDto;

@FeignClient(name = "common-service", url = "${service.common.url}", path = "/api")
public interface CommonFeignClient {

    @GetMapping("template/get-detail/{templateCode}")
    ResponseData<TemplateResDto> getDetail(@PathVariable String templateCode);

    @PostMapping("template/generate-report")
    ResponseEntity<byte[]> generateReport(@Valid @RequestBody ReportReqDto request);
}
