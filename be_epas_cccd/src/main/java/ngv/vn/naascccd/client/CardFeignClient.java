package ngv.vn.naascccd.client;

import com.fasterxml.jackson.databind.node.ObjectNode;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;

@FeignClient(value = "${app.feign.config.name}", url = "${app.feign.config.url}")
public interface CardFeignClient {
    @PostMapping("/eid/v2/read")
    ObjectNode readCard(@RequestBody ObjectNode body, @RequestHeader("signature") String signature, @RequestHeader("appid") String appId);
    
    @PostMapping("/eid/v1/verify")
    ObjectNode verifyC06(@RequestBody ObjectNode body, @RequestHeader("signature") String signature, @RequestHeader("appid") String appId);
    
    @PostMapping("/faceid/v1/verifyByCardId")
    ObjectNode verifyFace(@RequestBody ObjectNode body, @RequestHeader("signature") String signature, @RequestHeader("appid") String appId,
                          @RequestHeader("readCardId") String readCardId);
    
    @GetMapping("/v1/getConfig")
    ObjectNode getConfig(@RequestParam String deviceId);
    
}
