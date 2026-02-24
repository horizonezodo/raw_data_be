package com.naas.category_service.dto.CtgInfPosition;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ExportCtgInfPositionDTO {
    private String positionCode;
    private String positionName;
    private String status;
}
