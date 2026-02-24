package ngvgroup.com.fac.feature.double_entry_accounting.service;
import ngvgroup.com.fac.feature.double_entry_accounting.dto.DoubleEntryAccountingProcessDto;

public interface DoubleEntryValidationService {
    void validateDoubleEntry(DoubleEntryAccountingProcessDto dto);
}
