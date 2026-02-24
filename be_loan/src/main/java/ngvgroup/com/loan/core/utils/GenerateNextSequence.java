package ngvgroup.com.loan.core.utils;


import jakarta.persistence.Table;
import lombok.RequiredArgsConstructor;
import ngvgroup.com.loan.feature.interest_process.model.txn.LnmTxnIntRate;
import org.springframework.stereotype.Component;

import java.util.Date;

@Component
@RequiredArgsConstructor
public class GenerateNextSequence {
    private final SequenceUtil sequenceUtil;

    public String generateRegistration(String prefix) {

        String currentDate = DateUtils.formatDateToSequence(new Date());

        return sequenceUtil.getNextSequence(
                prefix,
                LnmTxnIntRate.class.getAnnotation(Table.class).name(),
                "PROCESS_INSTANCE_CODE",
                currentDate,
                1,
                5
        );
    }

}
