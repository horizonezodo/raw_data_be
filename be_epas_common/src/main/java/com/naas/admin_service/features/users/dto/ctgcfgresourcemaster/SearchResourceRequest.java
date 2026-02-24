package com.naas.admin_service.features.users.dto.ctgcfgresourcemaster;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@EqualsAndHashCode(callSuper = true)
@Data
@AllArgsConstructor
@NoArgsConstructor
public class SearchResourceRequest extends SearchDTO {
    private String filedSearch;
}
