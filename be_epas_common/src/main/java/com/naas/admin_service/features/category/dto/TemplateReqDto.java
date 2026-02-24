package com.naas.admin_service.features.category.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.*;

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

  @NotBlank
  private Boolean fileMappingChanged;

}
