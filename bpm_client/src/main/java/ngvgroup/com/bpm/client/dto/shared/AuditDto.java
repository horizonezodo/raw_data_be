package ngvgroup.com.bpm.client.dto.shared;

import lombok.Data;

@Data
public class AuditDto {
    private String fieldName;
    private String fieldCode;
    private Object oldValue;
    private Object newValue;
}
