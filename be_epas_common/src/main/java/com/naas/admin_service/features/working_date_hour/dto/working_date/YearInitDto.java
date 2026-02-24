package com.naas.admin_service.features.working_date_hour.dto.working_date;

import lombok.*;

@Getter
@Setter
@Data
@NoArgsConstructor
@AllArgsConstructor
public class YearInitDto {
    private String orgCode;

    private int year;

    private boolean sat;

    private boolean sun;
}
