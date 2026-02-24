package com.naas.category_service.dto;

import lombok.*;

import java.util.List;

@Getter
@Setter
@Data
@NoArgsConstructor
@AllArgsConstructor
public class CommonDto {
    private String commonCode;
    private String commonName;
    private String parentCode;
    private List<CommonDto> children;
}
