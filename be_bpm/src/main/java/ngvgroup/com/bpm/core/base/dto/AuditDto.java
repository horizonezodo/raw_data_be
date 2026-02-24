package ngvgroup.com.bpm.core.base.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AuditDto {
    private String fieldName;
    private String fieldCode;
    private Object oldValue;
    private Object newValue;

    // for repository
    public AuditDto(String fieldName, String fieldCode, String oldValue, String newValue) {
        this.fieldName = fieldName;
        this.fieldCode = fieldCode;
        this.oldValue = oldValue;
        this.newValue = newValue;
    }
}
