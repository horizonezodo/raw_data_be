package ngvgroup.com.crm.features.common.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class TemplateReqDto {
  @NotBlank
  private String templateCode;

  @NotBlank
  private String fileName;

  private String description;

  @NotBlank
  private Boolean fileChanged;

}
