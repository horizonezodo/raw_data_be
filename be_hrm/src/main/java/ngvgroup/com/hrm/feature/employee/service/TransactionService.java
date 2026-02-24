package ngvgroup.com.hrm.feature.employee.service;

import ngvgroup.com.hrm.feature.employee.dto.HrmProfileDto;
import ngvgroup.com.hrm.feature.employee.utils.GenerateNextSequence;

public interface TransactionService {
    GenerateNextSequence getSequence();

    void createEmployee(String processTypeCode, HrmProfileDto profile);

    void updateEmployee(String processInstanceCode, HrmProfileDto profile);

    HrmProfileDto getProfile(String processInstanceCode);

    void cancelRequest(String processInstanceCode);

    void updateEndProcess(String processInstanceCode);
}
