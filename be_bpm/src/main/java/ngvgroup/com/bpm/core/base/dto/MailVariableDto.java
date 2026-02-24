package ngvgroup.com.bpm.core.base.dto;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import lombok.Builder;

import java.util.List;
import java.util.Map;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MailVariableDto {
    private String emailTemplateCode;
    private List<String> userNameTo;
    private List<String> userNameCc;
    private Map<String, String> paramEmail;
}
