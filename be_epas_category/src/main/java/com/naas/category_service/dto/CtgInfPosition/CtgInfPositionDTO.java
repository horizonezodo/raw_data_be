package com.naas.category_service.dto.CtgInfPosition;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CtgInfPositionDTO {
    private Long id;
    private String orgCode;
    private String positionCode;
    private String positionName;
    private int isDelete;
    private String description;
    private String status;
}
