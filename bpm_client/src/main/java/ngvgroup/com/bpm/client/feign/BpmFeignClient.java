package ngvgroup.com.bpm.client.feign;

import ngvgroup.com.bpm.client.dto.request.DraftTaskBpmData;
import ngvgroup.com.bpm.client.dto.shared.ComCfgProcessFileDto;
import ngvgroup.com.bpm.client.dto.shared.FileDto;
import ngvgroup.com.bpm.client.dto.shared.ProcessFileDto;
import ngvgroup.com.bpm.client.dto.response.StartResponse;
import ngvgroup.com.bpm.client.dto.response.TaskViewBpmData;
import ngvgroup.com.bpm.client.dto.camunda.ProcessInstanceDto;
import ngvgroup.com.bpm.client.dto.camunda.CamundaVariableDto;
import ngvgroup.com.bpm.client.dto.camunda.CompleteTaskRequestDto;
import ngvgroup.com.bpm.client.dto.camunda.StartProcessRequestDto;

import java.util.List;
import java.util.Map;

import ngvgroup.com.bpm.client.dto.variable.TransactionHistoryDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import com.ngvgroup.bpm.core.common.dto.ResponseData;

@FeignClient(name = "bpm-engine-service", url = "${service.bpm.url}", path = "/api")
public interface BpmFeignClient {

	@PostMapping("/bpm/process/start/{key}")
	ResponseData<ProcessInstanceDto> startProcess(
			@PathVariable("key") String processDefinitionKey,
			@RequestBody StartProcessRequestDto requestBody);

	@PostMapping("/engine-rest/task/{taskId}/complete")
	void completeTask(
			@PathVariable("taskId") String taskId,
			@RequestBody CompleteTaskRequestDto requestBody);

	@GetMapping("/bpm/detail/{taskId}")
	ResponseData<TaskViewBpmData> getDetail(@PathVariable("taskId") String taskId);

	@GetMapping("/bpm/detail-reference-code/{referenceCode}")
	ResponseData<List<ProcessFileDto>> getProcessFilesFromReferenceCode(
			@PathVariable("referenceCode") String referenceCode,
			@RequestParam("processTypeCode") String processTypeCode);

	@PostMapping("/bpm/save-draft")
	ResponseData<Void> saveDraft(@RequestBody DraftTaskBpmData request);

	@PostMapping("/bpm/claim-task/{taskId}")
	ResponseData<Void> claimTaskWrapper(@PathVariable("taskId") String taskId);

	@GetMapping("/bpm/file-detail/{fileId}")
	ResponseData<FileDto> getFileDetail(@PathVariable String fileId);

	@GetMapping("/engine-rest/task/{taskId}/variables?deserializeValues=false")
	Map<String, CamundaVariableDto> getTaskVariablesFromCamunda(@PathVariable("taskId") String taskId);

	@GetMapping("/process-file/get-details/{processTypeCode}")
	ResponseData<List<ComCfgProcessFileDto>> getProcessFiles(@PathVariable String processTypeCode);

	@GetMapping("/bpm/task-definition-key/{taskId}")
	ResponseData<String> getTaskDefinitionKeyFromCamunda(@PathVariable String taskId);

	@GetMapping("/bpm/transaction-history/{customerCode}")
	ResponseData<List<TransactionHistoryDto>> getTransactionHistory(
			@PathVariable String customerCode,
			@RequestParam List<String> processTypeCode);

	@PostMapping("/bpm/process/rollback")
	ResponseData<Void> rollback(@RequestBody StartResponse response);
}
