package ngvgroup.com.bpmn.openfeign;

import ngvgroup.com.bpmn.dto.user.UserDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;

import com.ngvgroup.bpm.core.dto.ResponseData;

import java.util.List;

@FeignClient(name = "UsersFeignClient", url = "${service.common.url}")
public interface UsersFeignClient {
    @GetMapping("/api/users/inf-list-username")
    ResponseData<List<UserDTO>> getListUserInfo(List<String> usernames);
}
