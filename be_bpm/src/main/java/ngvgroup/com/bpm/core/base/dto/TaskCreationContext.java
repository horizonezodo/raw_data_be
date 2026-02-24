package ngvgroup.com.bpm.core.base.dto;

import ngvgroup.com.bpm.core.base.model.BpmTxnProcessInstance;

import java.sql.Timestamp;

public record TaskCreationContext(
                Timestamp createdTime,
                Timestamp transactionTime,
                String orgCode,
                String orgName,
                String processInstanceCode,
                String createdBy,
                String approvedBy,
                String prevActionBy,
                String candidateGroup,
                String referenceCode,
                BpmTxnProcessInstance processInstance) {
}
