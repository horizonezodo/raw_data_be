package ngvgroup.com.bpm.features.common.feign;

import com.ngvgroup.bpm.core.common.dto.ResponseData;
import ngvgroup.com.bpm.features.com_txn_task_inbox.dto.UserDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;

import java.util.List;

@FeignClient(name = "users", url = "${client.epas-common}")
public interface UsersFeignClient {
    @PostMapping("api/users/inf-list-username")
    ResponseData<List<UserDTO>> getAll(List<String> listUserName);
}
