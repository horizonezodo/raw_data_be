package ngvgroup.com.fac.feature.sheet_import_export_process.service;

import ngvgroup.com.fac.feature.sheet_import_export_process.dto.SheetInfoDto;

public interface SheetValidationService {
    void validateSheetInfo(SheetInfoDto dto);
}
