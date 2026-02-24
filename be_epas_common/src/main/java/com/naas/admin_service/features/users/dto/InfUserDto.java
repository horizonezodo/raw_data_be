package com.naas.admin_service.features.users.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class InfUserDto {
    private String id;
    private String username;
    private String firstName;
    private String lastName;
    private String email;
}
