package com.naas.admin_service.features.users.dto.ctgcfgresourcemaster;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.util.List;
@EqualsAndHashCode(callSuper = true)
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ExportExcelReq extends SearchDTO {
    private List<String> labels;

}
