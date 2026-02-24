package ngvgroup.com.bpm.features.com_txn_task_inbox.repository;

import com.ngvgroup.bpm.core.persistence.repository.BaseRepository;
import jakarta.transaction.Transactional;
import ngvgroup.com.bpm.core.base.model.BpmTxnTaskInbox;
import ngvgroup.com.bpm.features.com_txn_task_inbox.dto.ActHiTaskIntResponse;
import ngvgroup.com.bpm.features.com_txn_task_inbox.dto.ComTxnTaskInboxSearch;
import ngvgroup.com.bpm.features.com_txn_task_inbox.dto.InboundTransactionsDto;
import ngvgroup.com.bpm.features.com_txn_task_inbox.dto.OutboundTransactionsDto;
import ngvgroup.com.bpm.features.com_txn_task_inbox.dto.TxnTaskInboxDto;

import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface ComTxnTaskInboxRepository extends BaseRepository<BpmTxnTaskInbox> {

       @Query(value = """
                         SELECT art.ID_ as id, art.TASK_DEF_KEY_ as taskDefKey, art.CREATE_TIME_ as createTime, art.LAST_UPDATED_ as lastUpdated
                         FROM  ACT_HI_PROCINST ap
                         INNER JOIN ACT_RU_TASK art ON ap.PROC_INST_ID_ = art.PROC_INST_ID_
                         WHERE ap.BUSINESS_KEY_ = :processInstanceCode
                     """, nativeQuery = true)
       TxnTaskInboxDto findByProcessInstanceCode(@Param("processInstanceCode") String processInstanceCode);

       @Modifying
       @Transactional
       @Query(value = """
                         UPDATE BPM_TXN_TASK_INBOX ctc
                         SET ctc.BUSINESS_STATUS = 'CANCEL'
                         WHERE ctc.BUSINESS_STATUS = 'ACTIVE'
                           AND EXISTS (
                             SELECT 1
                             FROM ACT_HI_PROCINST ahp
                             WHERE ahp.BUSINESS_KEY_ = ctc.PROCESS_INSTANCE_CODE
                               AND ahp.PROC_INST_ID_ = :processInstanceId
                           )
                     """, nativeQuery = true)
       void cancelTask(@Param("processInstanceId") String processInstanceId);

       @Query(value = """
                         SELECT CTPI.CUSTOMER_CODE , NULL AS CUSTOMER_NAME , ctpi.PROCESS_INSTANCE_CODE , ctti.ORG_NAME , art.NAME_ AS TASK_NAME,
                                ctti.TASK_START_TIME , ctti.ADDL_STR_FLD1 AS USER_BEFORE, art.ID_ AS TASK_ID, art.PROC_INST_ID_ AS PROC_INST_ID
                         FROM COM_TXN_PROCESS_INSTANCE ctpi
                         INNER JOIN BPM_TXN_TASK_INBOX ctti ON CTPI.PROCESS_INSTANCE_CODE = CTTI.PROCESS_INSTANCE_CODE
                         INNER JOIN ACT_RU_TASK art ON ctti.TASK_ID = art.ID_
                         WHERE (:processTypeCode IS NULL OR :processTypeCode = '' OR CTPI.PROCESS_TYPE_CODE = :processTypeCode)
                         AND (:processInstanceCode IS NULL OR :processInstanceCode = '' OR CTPI.PROCESS_INSTANCE_CODE = :processInstanceCode)
                         AND (:customerCode IS NULL OR :customerCode = '' OR CTPI.CUSTOMER_CODE = :customerCode)
                         AND (:orgCode IS NULL OR :orgCode = '' OR CTPI.ORG_CODE = :orgCode)
                         AND (:fromDate IS NULL OR :fromDate = '' OR ctti.TASK_START_TIME >= TO_TIMESTAMP(:fromDate, 'YYYY-MM-DD HH24:MI:SS'))
                         AND (:toDate IS NULL OR :toDate = '' OR ctti.TASK_START_TIME <= TO_TIMESTAMP(:toDate, 'YYYY-MM-DD HH24:MI:SS'))
                         ORDER BY ctti.TASK_START_TIME DESC
                     """, nativeQuery = true)
       Page<InboundTransactionsDto> listInboundTrans(@Param("processTypeCode") String processTypeCode,
                     @Param("processInstanceCode") String processInstanceCode,
                     @Param("customerCode") String customerCode,
                     @Param("orgCode") String orgCode, @Param("fromDate") String fromDate,
                     @Param("toDate") String toDate,
                     Pageable pageable);

       @Query(value = """
                         SELECT CTPI.CUSTOMER_CODE , NULL AS CUSTOMER_NAME, ctpi.PROCESS_INSTANCE_CODE , ctti.ORG_NAME ,
                                aht.NAME_ AS TASK_NAME, ctti.TASK_START_TIME, aht.END_TIME_ AS END_TIME, ctti.ADDL_STR_FLD3 AS USER_BEFORE,
                                aht.ID_ AS TASK_ID, aht.PROC_INST_ID_ AS PROC_INST_ID
                         FROM COM_TXN_PROCESS_INSTANCE ctpi
                         INNER JOIN BPM_TXN_TASK_INBOX ctti ON CTPI.PROCESS_INSTANCE_CODE = CTTI.PROCESS_INSTANCE_CODE
                         INNER JOIN ACT_HI_TASKINST aht ON ctti.TASK_ID = aht.ID_
                         WHERE (:processTypeCode IS NULL OR :processTypeCode = '' OR CTPI.PROCESS_TYPE_CODE = :processTypeCode)
                         AND (:processInstanceCode IS NULL OR :processTypeCode = '' OR CTPI.PROCESS_INSTANCE_CODE = :processInstanceCode)
                         AND (:customerCode IS NULL OR :processTypeCode = '' OR CTPI.CUSTOMER_CODE = :customerCode)
                         AND (:orgCode IS NULL OR :processTypeCode = '' OR CTPI.ORG_CODE = :orgCode)
                         AND (:fromDate IS NULL OR :processTypeCode = '' OR aht.END_TIME_ >= TO_TIMESTAMP(:fromDate, 'YYYY-MM-DD HH24:MI:SS'))
                         AND (:toDate IS NULL OR :processTypeCode = '' OR aht.END_TIME_ <= TO_TIMESTAMP(:toDate, 'YYYY-MM-DD HH24:MI:SS'))
                         ORDER BY ctti.TASK_START_TIME DESC
                     """, nativeQuery = true)
       Page<OutboundTransactionsDto> listOutboundTrans(@Param("processTypeCode") String processTypeCode,
                     @Param("processInstanceCode") String processInstanceCode,
                     @Param("customerCode") String customerCode,
                     @Param("orgCode") String orgCode, @Param("fromDate") String fromDate,
                     @Param("toDate") String toDate,
                     Pageable pageable);

       @Query(value = """
                         SELECT PROCESS_INSTANCE_CODE, TASK_DEFINE_NAME, ACCEPTED_BY
                         FROM BPM_TXN_TASK_INBOX
                         WHERE ACCEPTED_BY = :username
                         AND (:orgCode IS NULL OR :orgCode = '' OR ORG_CODE = :orgCode)
                         AND BUSINESS_STATUS <> 'COMPLETE'
                         ORDER BY PROCESS_INSTANCE_CODE DESC
                     """, nativeQuery = true)
       Page<Object[]> findTasksByAcceptedBy(@Param("username") String username, @Param("orgCode") String orgCode,
                     Pageable pageable);

       @Query(value = """
                         SELECT ctti.PROCESS_INSTANCE_CODE as processInstanceCode,
                                pt.PROCESS_TYPE_NAME as processTypeName,
                                ctti.TASK_DEFINE_NAME as taskDefineName,
                                ctti.SLA_MAX_DURATION as slaMaxDuration,
                                ctti.ACCEPTED_DATE as acceptedDate,
                                ctti.SLA_TASK_DEADLINE as slaTaskDeadline,
                                ctti.ASSIGN_TO as assignTo,
                                ctti.BUSINESS_STATUS as businessStatus
                         FROM BPM_TXN_TASK_INBOX ctti
                         LEFT JOIN COM_CFG_PROCESS_TYPE pt
                                ON pt.PROCESS_TYPE_CODE = ctti.PROCESS_TYPE_CODE
                         WHERE (ctti.ACCEPTED_BY = :currentUser OR 
                                (ctti.ACCEPTED_BY IS NULL AND ctti.ASSIGN_TO = :currentUser))
                           AND ctti.BUSINESS_STATUS IN :statusList
                           AND (:ruleCode IS NULL OR :ruleCode = '' OR ctti.RULE_CODE = :ruleCode)
                           AND (:filterDate IS NULL OR ctti.CREATED_DATE >= TO_DATE(:filterDate, 'YYYY-MM-DD'))
                           AND (:orgCode IS NULL OR :orgCode = '' OR ctti.ORG_CODE = :orgCode)
                           AND (:slaOverdueDays IS NULL OR ctti.SLA_TASK_DEADLINE <= (SYSTIMESTAMP - NUMTODSINTERVAL(:slaOverdueDays, 'DAY')))
                         ORDER BY ctti.PROCESS_INSTANCE_CODE DESC
                     """, nativeQuery = true)
       List<Map<String, Object>> findTasksByUserStatusRuleDateOrgWithStatus(
                     @Param("currentUser") String currentUser,
                     @Param("statusList") List<String> statusList,
                     @Param("ruleCode") String ruleCode,
                     @Param("filterDate") String filterDate,
                     @Param("orgCode") String orgCode,
                     @Param("slaOverdueDays") Integer slaOverdueDays);

       @Query(value = """
                         SELECT ctti.PROCESS_INSTANCE_CODE as processInstanceCode,
                                pt.PROCESS_TYPE_NAME as processTypeName,
                                ctti.TASK_DEFINE_NAME as taskDefineName,
                                ctti.SLA_MAX_DURATION as slaMaxDuration,
                                ctti.ACCEPTED_DATE as acceptedDate,
                                ctti.SLA_TASK_DEADLINE as slaTaskDeadline,
                                ctti.ASSIGN_TO as assignTo,
                                ctti.BUSINESS_STATUS as businessStatus
                         FROM BPM_TXN_TASK_INBOX ctti
                         LEFT JOIN COM_CFG_PROCESS_TYPE pt
                                ON pt.PROCESS_TYPE_CODE = ctti.PROCESS_TYPE_CODE
                         WHERE (ctti.ACCEPTED_BY = :currentUser OR
                                (ctti.ACCEPTED_BY IS NULL AND ctti.ASSIGN_TO = :currentUser))
                           AND (:ruleCode IS NULL OR :ruleCode = '' OR ctti.RULE_CODE = :ruleCode)
                           AND (:filterDate IS NULL OR ctti.CREATED_DATE >= TO_DATE(:filterDate, 'YYYY-MM-DD'))
                           AND (:orgCode IS NULL OR :orgCode = '' OR ctti.ORG_CODE = :orgCode)
                           AND (:slaOverdueDays IS NULL OR ctti.SLA_TASK_DEADLINE <= (SYSTIMESTAMP - NUMTODSINTERVAL(:slaOverdueDays, 'DAY')))
                         ORDER BY ctti.PROCESS_INSTANCE_CODE DESC
                     """, nativeQuery = true)
       List<Map<String, Object>> findTasksByUserStatusRuleDateOrgWithoutStatus(
                     @Param("currentUser") String currentUser,
                     @Param("ruleCode") String ruleCode,
                     @Param("filterDate") String filterDate,
                     @Param("orgCode") String orgCode,
                     @Param("slaOverdueDays") Integer slaOverdueDays);

              @Query(value = """
                         select CTTI.TASK_ID, AHP.PROC_INST_ID_, CTTI.PROCESS_INSTANCE_CODE, CTTI.TASK_DEFINE_NAME,
                                CTTI.CREATED_DATE, CTTI.TASK_UPDATE_TIME, CTTI.ASSIGN_TO, CTTI.BUSINESS_STATUS, CTTI.RULE_CODE, CTTI.IS_SUSPEND
                         FROM BPM_TXN_TASK_INBOX CTTI
                         JOIN ACT_HI_PROCINST AHP ON CTTI.PROCESS_INSTANCE_CODE = AHP.BUSINESS_KEY_
                         WHERE CTTI.IS_DELETE = 0 AND (:filter is null or lower(CTTI.TASK_ID) like :filter or lower(AHP.PROC_INST_ID_) like :filter
                         or lower(CTTI.PROCESS_INSTANCE_CODE) like :filter or lower(CTTI.TASK_DEFINE_NAME) like :filter
                         or to_char(CTTI.CREATED_DATE) like :filter or to_char(CTTI.TASK_UPDATE_TIME) like :filter
                         or lower(CTTI.ASSIGN_TO) like :filter or lower(CTTI.BUSINESS_STATUS) like :filter
                         or (CTTI.IS_SUSPEND = 0 AND (CTTI.BUSINESS_STATUS = 'COMPLETED' and 'hoàn thành' like :filter ) or (CTTI.BUSINESS_STATUS = 'ACTIVE' and 'đang thực hiện' like :filter )
                         or (CTTI.BUSINESS_STATUS = 'UNASSIGNED' and 'chờ thực hiện' like :filter ))
                         or (CTTI.IS_SUSPEND = 1 and 'tạm dừng' like :filter))
                     """, nativeQuery = true)
       Page<ActHiTaskIntResponse> searchTaskList(String filter, Pageable pageable);

       @Query(value = """
               SELECT
            CTTI.TASK_ID,
            AHP.PROC_INST_ID_,
            CTTI.PROCESS_INSTANCE_CODE,
            CTTI.TASK_DEFINE_NAME,
            CTTI.CREATED_DATE,
            CTTI.TASK_UPDATE_TIME,
            CTTI.ASSIGN_TO,
            CTTI.BUSINESS_STATUS,
            CTTI.RULE_CODE,
            CTTI.IS_SUSPEND
        FROM BPM_TXN_TASK_INBOX CTTI
        JOIN ACT_HI_PROCINST AHP
            ON CTTI.PROCESS_INSTANCE_CODE = AHP.BUSINESS_KEY_
        WHERE CTTI.IS_DELETE = 0
          AND (:#{#search.processInstanceCode} IS NULL
               OR LOWER(CTTI.PROCESS_INSTANCE_CODE) = :#{#search.processInstanceCode})
          AND (:#{#search.procInstId} IS NULL
               OR LOWER(AHP.PROC_INST_ID_) = :#{#search.procInstId})
          AND (:#{#search.taskId} IS NULL
               OR LOWER(CTTI.TASK_ID) = :#{#search.taskId})
          AND (:#{#search.createdDateStart} IS NULL
               OR CTTI.CREATED_DATE >= TO_TIMESTAMP(:#{#search.createdDateStart}, 'YYYY-MM-DD HH24:MI:SS'))
          AND (:#{#search.createdDateEnd} IS NULL
               OR CTTI.CREATED_DATE <= TO_TIMESTAMP(:#{#search.createdDateEnd}, 'YYYY-MM-DD HH24:MI:SS'))
          AND (:#{#search.taskUpdateTimeStart} IS NULL
               OR CTTI.TASK_UPDATE_TIME >= TO_TIMESTAMP(:#{#search.taskUpdateTimeStart}, 'YYYY-MM-DD HH24:MI:SS'))
          AND (:#{#search.taskUpdateTimeEnd} IS NULL
               OR CTTI.TASK_UPDATE_TIME <= TO_TIMESTAMP(:#{#search.taskUpdateTimeEnd}, 'YYYY-MM-DD HH24:MI:SS'))
                """ , nativeQuery = true)
       Page<ActHiTaskIntResponse> searchTaskListAdvance(@Param("search") ComTxnTaskInboxSearch search, Pageable pageable);

       Optional<BpmTxnTaskInbox> findByTaskIdAndIsDelete(String taskId, int isDelete);
}
