package com.naas.admin_service.features.auth.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UpdateBranchResponseDto {
    private boolean success;
    private String message;
}
