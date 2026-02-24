package ngvgroup.com.bpm.features.rule.openfeign;

import com.ngvgroup.bpm.core.common.dto.ResponseData;
import ngvgroup.com.bpm.features.rule.dto.InfUserDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;

@FeignClient(name = "ComCfgRuleFeignClient", url = "${client.epas-common}")
public interface ComCfgRuleFeignClient {

    @PostMapping("/api/users/inf-list-username")
    ResponseData<List<InfUserDto>> getUserInf(@RequestBody List<String> listUsername);

    @PostMapping("/api/users/inf-list-user")
    ResponseData<List<InfUserDto>> getUserInf2(@RequestBody List<String> listUserId);

    @GetMapping("/api/users")
    ResponseData<List<InfUserDto>> listUser();
}
