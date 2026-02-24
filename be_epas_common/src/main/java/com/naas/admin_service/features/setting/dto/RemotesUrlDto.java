package com.naas.admin_service.features.setting.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class RemotesUrlDto {
    private Long id;

    @NotBlank(message = "remoteName không được để trống")
    private String remoteName;

    @NotBlank(message = "remotePath không được để trống")
    private String remotePath;

    @NotBlank(message = "remoteUrl không được để trống")
    private String remoteUrl;
}
