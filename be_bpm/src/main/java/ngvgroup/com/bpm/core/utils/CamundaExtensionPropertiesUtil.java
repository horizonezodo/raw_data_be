package ngvgroup.com.bpm.core.utils;

import java.util.HashMap;
import java.util.Map;

import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.engine.delegate.DelegateTask;
import org.camunda.bpm.model.bpmn.instance.BaseElement;
import org.camunda.bpm.model.bpmn.instance.ExtensionElements;
import org.camunda.bpm.model.bpmn.instance.UserTask;
import org.camunda.bpm.model.bpmn.instance.camunda.CamundaProperties;
import org.camunda.bpm.model.bpmn.instance.camunda.CamundaProperty;
import org.springframework.stereotype.Component;

import lombok.extern.slf4j.Slf4j;
import ngvgroup.com.bpm.core.contants.VariableConstants;

@Component
@Slf4j
public class CamundaExtensionPropertiesUtil {

    public Map<String, String> getProperties(DelegateTask task) {
        Map<String, String> extensionProperty = new HashMap<>();
        try {
            UserTask userTask = task.getBpmnModelElementInstance();
            if (userTask != null) {
                // 1. Logic specific to UserTask (Form Key)
                extensionProperty.put(VariableConstants.FORM_KEY, userTask.getCamundaFormKey());
                
                // 2. Reuse common logic for Extension Properties
                extensionProperty.putAll(extractExtensionProperties(userTask));
            }
        } catch (Exception e) {
            log.warn("Error retrieving extension property '{}' for task {}: {}", 
                     VariableConstants.FORM_KEY, task.getId(), e.getMessage());
        }
        return extensionProperty;
    }

    public Map<String, String> getProperties(DelegateExecution execution) {
        try {
            // Reuse common logic
            return extractExtensionProperties(execution.getBpmnModelElementInstance());
        } catch (Exception e) {
            // Returning empty map on error (consistent with previous logic)
            return new HashMap<>();
        }
    }

    /**
     * Shared method to extract Camunda Extension Properties from any BPMN BaseElement.
     * Reduces duplication between DelegateTask and DelegateExecution logic.
     */
    private Map<String, String> extractExtensionProperties(BaseElement element) {
        Map<String, String> propertiesMap = new HashMap<>();

        if (element == null) return propertiesMap;

        ExtensionElements extensionElements = element.getExtensionElements();
        if (extensionElements == null) return propertiesMap;

        CamundaProperties properties = extensionElements.getElementsQuery()
                .filterByType(CamundaProperties.class)
                .singleResult();

        if (properties != null) {
            for (CamundaProperty prop : properties.getCamundaProperties()) {
                propertiesMap.put(prop.getCamundaName(), prop.getCamundaValue());
            }
        }
        return propertiesMap;
    }
}