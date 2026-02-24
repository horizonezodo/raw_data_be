package ngvgroup.com.fac.feature.sheet_import_export_process.service;
import ngvgroup.com.fac.feature.sheet_import_export_process.dto.SheetInfoDto;

public interface SheetTransactionService {
    String generateSeq(String orgCode);

    void startProcess(SheetInfoDto request);

    void executeEditBusiness(SheetInfoDto req, String processInstanceCode);

    void cancelEditBusiness(String processInstanceCode);

    void completeTransaction(String processInstanceCode);

    void updateSheetInf(SheetInfoDto dto);
}
