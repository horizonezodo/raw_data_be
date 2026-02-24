package ngvgroup.com.fac.feature.double_entry_accounting.service;

import ngvgroup.com.fac.feature.double_entry_accounting.dto.DoubleEntryAccountingProcessDto;

public interface DoubleEntryTransactionService {
    void createDoubleAccountEntry(DoubleEntryAccountingProcessDto request);

    void updateDoubleEntry(DoubleEntryAccountingProcessDto dto,String processInstanceCode);

    DoubleEntryAccountingProcessDto getDetail(String processInstanceCode);

    void cancelTask(String processInstanceCode);

    void updateDoubleEntryAcctEndProcess(String processInstanceCode);

    void updateBusinessStatus(String processInstanceCode,String businessStatus);
}
