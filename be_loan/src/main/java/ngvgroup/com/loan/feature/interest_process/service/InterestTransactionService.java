package ngvgroup.com.loan.feature.interest_process.service;

import ngvgroup.com.loan.core.utils.GenerateNextSequence;
import ngvgroup.com.loan.feature.interest_process.dto.InterestProfileDTO;

public interface InterestTransactionService {

    void createInterest(InterestProfileDTO profile, Long id);

    void updateInterest(InterestProfileDTO profile);

    void updateInterest(String processInstanceCode, InterestProfileDTO profile);

    InterestProfileDTO getDetail(String processInstanceCode);

    void cancelRequest(String processInstanceCode);

    void updateEndProcess(String processInstanceCode,String type);

    GenerateNextSequence getSequence();

}
