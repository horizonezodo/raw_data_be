package ngvgroup.com.bpm.client.dto.variable;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.util.List;
import java.util.Map;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class MailVariableDto {
    private String emailTemplateCode;
    private List<String> userNameTo;
    private List<String> userNameCc;
    private Map<String, String> paramEmail;
}
