package ngvgroup.com.rpt.features.comcfg.feign;


import com.ngvgroup.bpm.core.common.dto.ResponseData;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;



@FeignClient(name = "ComCfgTemplateFeignClient", url = "${service.common.url}")
public interface ComCfgTemplateFeign {
    @GetMapping("api/template/get-all")
    ResponseData<Object> getAll(
            @RequestParam("keyword") String keyword,
            @RequestParam("page") int page,
            @RequestParam("size") int size
            );
}
