package com.naas.admin_service.features.users.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ExportExcelUserDto {
    private String username;
    private String lastName;
    private String email;
    private String enabled;
    private String fullName;
}
