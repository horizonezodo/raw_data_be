package ngvgroup.com.bpm.client.template;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import ngvgroup.com.bpm.client.dto.request.StartRequest;
import ngvgroup.com.bpm.client.dto.response.StartResponse;
import ngvgroup.com.bpm.client.utils.JsonUtil;
import org.springframework.stereotype.Service;
import org.springframework.util.MultiValueMap;
import org.springframework.web.multipart.MultipartFile;

import java.util.Map;

/**
 * Service for orchestrating Process Start operations.
 * <p>
 * This service uses {@link ProcessStarterRegistry} to dynamically lookup the
 * appropriate
 * handler based on processDefinitionKey, then delegates the operation to the
 * handler.
 *
 * @see ngvgroup.com.bpm.client.annotation.ProcessStarterSubscription
 * @see ProcessStarterRegistry
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class ProcessStarterService {

    private final ProcessStarterRegistry processStarterRegistry;

    /**
     * Starts a process by looking up the handler via processDefinitionKey from the
     * request.
     *
     * @param dto          the start request containing BPM data and business data
     * @param multiFileMap uploaded files
     * @return StartResponse containing processInstanceCode and processInstanceId
     */
    @SuppressWarnings("unchecked")
    public <T> StartResponse startProcess(StartRequest<T> dto,
            MultiValueMap<String, MultipartFile> multiFileMap) {
        String processDefinitionKey = dto.getBpmData().getProcessDefinitionKey();
        log.debug("Starting process for processDefinitionKey: {}", processDefinitionKey);

        AbstractProcessStarter<T> handler = processStarterRegistry.getHandlerOrThrow(processDefinitionKey);

        // Fix type mismatch: Convert Map to BusinessDto if needed
        if (dto.getBusinessData() instanceof Map) {
            Class<T> businessClass = handler.getBusinessClass();
            if (businessClass != null) {
                T convertedData = JsonUtil.convertValue(dto.getBusinessData(),
                        businessClass);
                dto.setBusinessData(convertedData);
            }
        }

        StartResponse response = handler.startProcess((StartRequest<T>) dto, multiFileMap);

        log.info("Process started successfully: processDefinitionKey={}, processInstanceCode={}",
                processDefinitionKey, response.getProcessInstanceCode());

        return response;
    }

    /**
     * Checks if a handler exists for the given process definition key.
     *
     * @param processDefinitionKey the process definition key
     * @return true if a handler is registered
     */
    public boolean hasHandler(String processDefinitionKey) {
        return processStarterRegistry.hasHandler(processDefinitionKey);
    }
}
