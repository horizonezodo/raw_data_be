package ngvgroup.com.bpmn.repository;

import ngvgroup.com.bpmn.dto.Process.ComTxnProcessDto;
import ngvgroup.com.bpmn.dto.Process.ProcessDTO;
import ngvgroup.com.bpmn.dto.Process.ProcessTypeDto;
import ngvgroup.com.bpmn.model.ComTxnProcessInstance;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ComTxnProcessInstanceRepository extends JpaRepository<ComTxnProcessInstance, Long> {
    @Query(value = """
            SELECT 
              pi.CUSTOMER_CODE as customerCode, 
              c.CUSTOMER_NAME as customerName, 
              pi.PROCESS_INSTANCE_CODE as processInstanceCode, 
              ti.ORG_NAME as orgName,
              art.PROC_INST_ID_ as procInstId,
              art.NAME_ as taskName,
              pt.PROCESS_TYPE_NAME as processTypeName, 
              pi.MODIFIED_DATE as modifiedDate, 
              pi.BUSINESS_STATUS as businessStatus,
              ti.TASK_ID as taskId
            FROM COM_TXN_PROCESS_INSTANCE pi
            INNER JOIN CRM_INF_CUSTOMER c ON pi.CUSTOMER_CODE = c.CUSTOMER_CODE
            INNER JOIN (
              SELECT ti1.*
              FROM COM_TXN_TASK_INBOX ti1
              INNER JOIN (
                SELECT PROCESS_INSTANCE_CODE, MAX(MODIFIED_DATE) AS MAX_DT
                FROM COM_TXN_TASK_INBOX
                GROUP BY PROCESS_INSTANCE_CODE
              ) latest ON ti1.PROCESS_INSTANCE_CODE = latest.PROCESS_INSTANCE_CODE
                 AND ti1.MODIFIED_DATE      = latest.MAX_DT
            ) ti ON ti.PROCESS_INSTANCE_CODE = pi.PROCESS_INSTANCE_CODE
            INNER JOIN ACT_RU_TASK art ON ti.TASK_ID = art.ID_
            INNER JOIN COM_CFG_PROCESS_TYPE pt ON pt.PROCESS_TYPE_CODE = pi.PROCESS_TYPE_CODE
            WHERE 
              (:processInstanceCode IS NULL OR pi.PROCESS_INSTANCE_CODE = :processInstanceCode)
              AND (:processTypeName   IS NULL OR pt.PROCESS_TYPE_NAME     = :processTypeName)
              AND (:customerCode      IS NULL OR pi.CUSTOMER_CODE          = :customerCode)
              AND (:orgName           IS NULL OR ti.ORG_NAME               = :orgName)
              AND (:fromDate          IS NULL OR pi.MODIFIED_DATE >= TO_DATE(:fromDate, 'YYYY-MM-DD'))
              AND (:toDate            IS NULL OR pi.MODIFIED_DATE <= TO_DATE(:toDate,   'YYYY-MM-DD'))
              AND (:filter              IS NULL OR pi.CUSTOMER_CODE          = :filter)
              AND (:filter            IS NULL OR c.CUSTOMER_NAME           = :filter)
              AND (:filter              IS NULL OR pt.PROCESS_TYPE_NAME     = :filter)
              AND (:filter              IS NULL OR ti.ORG_NAME               = :filter)
              AND (:filter            IS NULL OR art.NAME_                 = :filter)
            """, nativeQuery = true)
    Page<ProcessDTO> searchProcess(@Param("processInstanceCode") String processInstanceCode,
                                     @Param("processTypeName") String processTypeName,
                                     @Param("customerCode") String customerCode,
                                     @Param("orgName") String orgName,
                                     @Param("fromDate") String fromDate,
                                     @Param("toDate") String toDate,
                                     @Param("filter") String filter,
                                     Pageable pageable);


    @Query(value = "SELECT " +
            "pi.CUSTOMER_CODE AS customerCode, " +
            "COALESCE(cust.CUSTOMER_NAME, '') AS customerName, " +
            "pi.PROCESS_INSTANCE_CODE AS processInstanceCode, " +
            "inbox.ORG_NAME AS orgName, " +
            "task.NAME_ AS taskName, " +
            "pt.PROCESS_TYPE_NAME AS processTypeName, " +
            "pi.CREATED_BY AS createdBy," +
            "pi.SLA_MAX_DURATION AS slaMaxDuration," +
            "pi.CREATED_DATE AS createdDate," +
            "pi.SLA_PROCESS_DEADLINE AS slaProcessDeadline," +
            "pi.MODIFIED_DATE AS modifiedDate," +
            "pi.BUSINESS_STATUS," +
            "task.PROC_INST_ID_  AS procInstId," +
            "inbox.TASK_ID " +
            "FROM COM_TXN_PROCESS_INSTANCE pi " +
            "LEFT JOIN CRM_INF_CUSTOMER cust ON pi.CUSTOMER_CODE = cust.CUSTOMER_CODE " +
            " JOIN COM_TXN_TASK_INBOX inbox ON pi.PROCESS_INSTANCE_CODE = inbox.PROCESS_INSTANCE_CODE " +
            " JOIN ACT_HI_TASKINST task ON inbox.TASK_ID = task.ID_ " +
            " JOIN CTG_CFG_PROCESS_TYPE pt ON pi.PROCESS_TYPE_CODE = pt.PROCESS_TYPE_CODE " +
            "JOIN COM_CFG_RESOURCE_MAPPING mapping ON inbox.ORG_CODE = mapping.RESOURCE_CODE " +
            "    AND mapping.RESOURCE_TYPE_CODE = 'CM032.001' " +
            "    AND mapping.USER_ID = :userId " +
            "WHERE inbox.MODIFIED_DATE = ( " +
            "    SELECT MAX(inbox2.MODIFIED_DATE) " +
            "    FROM COM_TXN_TASK_INBOX inbox2 " +
            "    WHERE inbox2.PROCESS_INSTANCE_CODE = pi.PROCESS_INSTANCE_CODE " +
            ")", nativeQuery = true)
    Page<ComTxnProcessDto> getProcess(Pageable pageable,@Param("userId") String userId);

    @Query(value =
            "SELECT " +
                    "  pi.CUSTOMER_CODE AS customerCode, " +
                    "  COALESCE(cust.CUSTOMER_NAME, '') AS customerName, " +
                    "  pi.PROCESS_INSTANCE_CODE AS processInstanceCode, " +
                    "  inbox.ORG_NAME AS orgName, " +
                    "  task.NAME_ AS taskName, " +
                    "  pt.PROCESS_TYPE_NAME AS processTypeName, " +
                    "pi.CREATED_BY AS createdBy," +
                    "pi.SLA_MAX_DURATION AS slaMaxDuration," +
                    "pi.CREATED_DATE AS createdDate," +
                    "pi.SLA_PROCESS_DEADLINE AS slaProcessDeadline," +
                    "  pi.MODIFIED_DATE AS modifiedDate," +
                    "pi.DESCRIPTION AS description, " +
                    "pi.BUSINESS_STATUS AS businessStatus," +
                    "pi.SLA_RESULT AS slaResult, " +
                    "pi.SLA_WARNING_DURATION AS slaWarningDuration," +
                    "pi.SLA_WARNING_TYPE AS slaWarningType," +
                    "pi.SLA_WARNING_PERCENT AS slaWarningPercent," +
                    "task.PROC_INST_ID_  AS procInstId," +
                    "inbox.TASK_ID " +
                    "FROM COM_TXN_PROCESS_INSTANCE pi " +
                    "LEFT JOIN CRM_INF_CUSTOMER cust ON pi.CUSTOMER_CODE = cust.CUSTOMER_CODE " +
                    "JOIN COM_TXN_TASK_INBOX inbox ON pi.PROCESS_INSTANCE_CODE = inbox.PROCESS_INSTANCE_CODE " +
                    " JOIN ACT_HI_TASKINST task ON inbox.TASK_ID = task.ID_ " +
                    "JOIN CTG_CFG_PROCESS_TYPE pt ON pi.PROCESS_TYPE_CODE = pt.PROCESS_TYPE_CODE " +
                    "LEFT JOIN COM_CFG_RESOURCE_MAPPING mapping ON inbox.ORG_CODE = mapping.RESOURCE_CODE " +
                    "    AND mapping.RESOURCE_TYPE_CODE = 'CM032.001' " +
                    "    AND mapping.USER_ID = :userId " +

                    "LEFT JOIN COM_CFG_RESOURCE_MAPPING mapping1 ON pi.PROCESS_INSTANCE_CODE = mapping1.RESOURCE_CODE " +
                    "    AND mapping1.RESOURCE_TYPE_CODE = 'CM032.002' " +
                    "    AND mapping.USER_ID = :userId " +

                    "WHERE inbox.MODIFIED_DATE = ( " +
                    "  SELECT MAX(inbox2.MODIFIED_DATE) " +
                    "  FROM COM_TXN_TASK_INBOX inbox2 " +
                    "  WHERE inbox2.PROCESS_INSTANCE_CODE = pi.PROCESS_INSTANCE_CODE " +
                    ") " +
                    "AND ( " +
                    "  :keyword IS NULL OR " +
                    "  LOWER(pi.CUSTOMER_CODE) LIKE LOWER('%' || :keyword || '%') OR " +
                    "  LOWER(cust.CUSTOMER_NAME) LIKE LOWER('%' || :keyword || '%') OR " +
                    "  LOWER(pi.PROCESS_INSTANCE_CODE) LIKE LOWER('%' || :keyword || '%') OR " +
                    "  LOWER(inbox.ORG_NAME) LIKE LOWER('%' || :keyword || '%') OR " +
                    "  LOWER(task.NAME_) LIKE LOWER('%' || :keyword || '%') OR " +
                    "  LOWER(pt.PROCESS_TYPE_NAME) LIKE LOWER('%' || :keyword || '%') OR " +
                    "  LOWER(pi.CREATED_BY) LIKE LOWER('%' || :keyword || '%') OR " +
                    "  LOWER(TO_CHAR(pi.SLA_MAX_DURATION)) LIKE LOWER('%' || :keyword || '%') OR " +
                    "  LOWER(pi.DESCRIPTION) LIKE LOWER('%' || :keyword || '%') OR " +
                    "   (LOWER(:keyword) LIKE LOWER('đang thực hiện') AND pi.business_status = 'ACTIVE') OR " +
                    "   (LOWER(:keyword) LIKE LOWER('hoàn thành') AND pi.business_status = 'COMPLETE') OR " +
                    "   (LOWER(:keyword) LIKE LOWER('hủy') AND pi.business_status = 'CANCEL') OR " +
                    "  LOWER(TO_CHAR(pi.CREATED_DATE,'DD/MM/YYYY')) LIKE LOWER('%' || :keyword || '%') OR " +
                    "  LOWER(TO_CHAR(pi.SLA_PROCESS_DEADLINE,'DD/MM/YYYY')) LIKE LOWER('%' || :keyword || '%') OR " +
                    "LOWER(TO_CHAR(pi.MODIFIED_DATE, 'DD/MM/YYYY')) LIKE LOWER('%' || :keyword || '%')\n " +
                    ")"
            , nativeQuery = true)

    Page<ComTxnProcessDto> findProcess(@Param("keyword") String keyword,Pageable pageable,@Param("userId")String userId);

    @Query(value = "SELECT " +
            "pi.CUSTOMER_CODE AS customerCode, " +
            "COALESCE(cust.CUSTOMER_NAME, '') AS customerName, " +
            "pi.PROCESS_INSTANCE_CODE AS processInstanceCode, " +
            "inbox.ORG_NAME AS orgName, " +
            "task.NAME_ AS taskName, " +
            "pt.PROCESS_TYPE_NAME AS processTypeName, " +
            "pi.CREATED_BY AS createdBy, " +
            "pi.SLA_MAX_DURATION AS slaMaxDuration, " +
            "pi.CREATED_DATE AS createdDate, " +
            "pi.SLA_PROCESS_DEADLINE AS slaProcessDeadline, " +
            "pi.MODIFIED_DATE AS modifiedDate, " +
            "pi.BUSINESS_STATUS AS businessStatus, " +
            "pi.SLA_RESULT AS slaResult, " +
            "pi.SLA_WARNING_DURATION AS slaWarningDuration, " +
            "pi.SLA_WARNING_TYPE AS slaWarningType, " +
            "pi.SLA_WARNING_PERCENT AS slaWarningPercent, " +
            "task.PROC_INST_ID_ AS procInstId, " +
            "inbox.TASK_ID " +
            "FROM COM_TXN_PROCESS_INSTANCE pi " +
            "LEFT JOIN CRM_INF_CUSTOMER cust ON pi.CUSTOMER_CODE = cust.CUSTOMER_CODE " +
            "JOIN COM_TXN_TASK_INBOX inbox ON pi.PROCESS_INSTANCE_CODE = inbox.PROCESS_INSTANCE_CODE " +
            "JOIN ACT_HI_TASKINST task ON inbox.TASK_ID = task.ID_ " +
            "JOIN CTG_CFG_PROCESS_TYPE pt ON pi.PROCESS_TYPE_CODE = pt.PROCESS_TYPE_CODE " +
            "LEFT JOIN COM_CFG_RESOURCE_MAPPING mapping ON inbox.ORG_CODE = mapping.RESOURCE_CODE " +
            "  AND mapping.RESOURCE_TYPE_CODE = 'CM032.001' " +
            "  AND mapping.USER_ID = :userId " +
            "LEFT JOIN COM_CFG_RESOURCE_MAPPING mapping1 ON pi.PROCESS_INSTANCE_CODE = mapping1.RESOURCE_CODE " +
            "  AND mapping1.RESOURCE_TYPE_CODE = 'CM032.002' " +
            "  AND mapping1.USER_ID = :userId " +
            "WHERE inbox.MODIFIED_DATE = ( " +
            "    SELECT MAX(inbox2.MODIFIED_DATE) " +
            "    FROM COM_TXN_TASK_INBOX inbox2 " +
            "    WHERE inbox2.PROCESS_INSTANCE_CODE = pi.PROCESS_INSTANCE_CODE " +
            ") " +
            "AND (:customerCode IS NULL OR pi.CUSTOMER_CODE = :customerCode) " +
            "AND (:processInstanceCode IS NULL OR pi.PROCESS_INSTANCE_CODE = :processInstanceCode) " +
            "AND (:orgCode IS NULL OR inbox.ORG_CODE = :orgCode) " +
            "AND (:businessStatusListSize =0  OR pi.BUSINESS_STATUS IN (:businessStatusList)) " +
            "AND (:slaResult IS NULL OR pi.SLA_RESULT = :slaResult) " +
            "AND (:processTypeCodeListSize=0 OR pi.PROCESS_TYPE_CODE IN( :processTypeCodeList)) " +
            "AND (:fromDate IS NULL OR pi.MODIFIED_DATE >= TO_DATE(:fromDate, 'YYYY-MM-DD')) " +
            "AND (:toDate IS NULL OR pi.MODIFIED_DATE <= TO_DATE(:toDate, 'YYYY-MM-DD')) " +
            "AND ( " +
            "  :slaStatusColor IS NULL " +
            "  OR ( " +
            "    (:slaStatusColor = 'RED' AND pi.SLA_RESULT = 'BREACHED' AND pi.BUSINESS_STATUS IN ('COMPLETE', 'CANCEL')) " +
            "    OR (:slaStatusColor = 'GREEN' AND pi.SLA_RESULT = 'ACHIEVED' AND pi.BUSINESS_STATUS IN ('COMPLETE', 'CANCEL')) " +
            "    OR (pi.SLA_RESULT IS NULL AND ( " +
            "      (pi.SLA_WARNING_TYPE = 'FIXED' AND ( " +
            "        (:slaStatusColor = 'GREEN' AND (SYSDATE - pi.CREATED_DATE) < NUMTODSINTERVAL(pi.SLA_WARNING_DURATION, 'MINUTE')) " +
            "        OR (:slaStatusColor = 'YELLOW' AND (SYSDATE - pi.CREATED_DATE) BETWEEN NUMTODSINTERVAL(pi.SLA_WARNING_DURATION, 'MINUTE') AND NUMTODSINTERVAL(pi.SLA_MAX_DURATION, 'MINUTE')) " +
            "        OR (:slaStatusColor = 'RED' AND (SYSDATE - pi.CREATED_DATE) > NUMTODSINTERVAL(pi.SLA_MAX_DURATION, 'MINUTE')) " +
            "      )) " +
            "      OR (pi.SLA_WARNING_TYPE = 'PERCENT' AND ( " +
            "        (:slaStatusColor = 'GREEN' AND (SYSDATE - pi.CREATED_DATE) < NUMTODSINTERVAL(pi.SLA_MAX_DURATION * pi.SLA_WARNING_PERCENT, 'MINUTE')) " +
            "        OR (:slaStatusColor = 'YELLOW' AND (SYSDATE - pi.CREATED_DATE) BETWEEN NUMTODSINTERVAL(pi.SLA_MAX_DURATION * pi.SLA_WARNING_PERCENT, 'MINUTE') AND NUMTODSINTERVAL(pi.SLA_MAX_DURATION, 'MINUTE')) " +
            "        OR (:slaStatusColor = 'RED' AND (SYSDATE - pi.CREATED_DATE) > NUMTODSINTERVAL(pi.SLA_MAX_DURATION, 'MINUTE')) " +
            "      )) " +
            "    )) " +
            "  ) " +
            ")",
            nativeQuery = true)

    Page<ComTxnProcessDto> searchAdvance(
            @Param("customerCode") String customerCode,
            @Param("processInstanceCode") String processInstanceCode,
            @Param("orgCode") String orgCode,
            @Param("businessStatusList") List<String> businessStatusList,
            @Param("slaResult") String slaResult,
            @Param("processTypeCodeList") List<String> processTypeCodeList,
            @Param("fromDate") String fromDate,
            @Param("toDate") String toDate,
            @Param("slaStatusColor") String slaStatusColor,
            @Param("businessStatusListSize") int businessStatusListSize,
            @Param("processTypeCodeListSize") int processTypeCodeListSize,
            @Param("userId") String userId,
            Pageable pageable);


    @Query(value = "SELECT " +
            "pi.CUSTOMER_CODE AS customerCode, " +
            "COALESCE(cust.CUSTOMER_NAME, '') AS customerName, " +
            "pi.PROCESS_INSTANCE_CODE AS processInstanceCode, " +
            "inbox.ORG_NAME AS orgName, " +
            "task.NAME_ AS taskName, " +
            "pt.PROCESS_TYPE_NAME AS processTypeName, " +
            "pi.CREATED_BY AS createdBy, " +
            "pi.SLA_MAX_DURATION AS slaMaxDuration, " +
            "pi.CREATED_DATE AS createdDate, " +
            "pi.SLA_PROCESS_DEADLINE AS slaProcessDeadline, " +
            "pi.MODIFIED_DATE AS modifiedDate, " +
            "pi.BUSINESS_STATUS AS businessStatus, " +
            "pi.SLA_RESULT AS slaResult, " +
            "pi.SLA_WARNING_DURATION AS slaWarningDuration, " +
            "pi.SLA_WARNING_TYPE AS slaWarningType, " +
            "pi.SLA_WARNING_PERCENT AS slaWarningPercent, " +
            "task.PROC_INST_ID_ AS procInstId, " +
            "inbox.TASK_ID " +
            "FROM COM_TXN_PROCESS_INSTANCE pi " +
            "LEFT JOIN CRM_INF_CUSTOMER cust ON pi.CUSTOMER_CODE = cust.CUSTOMER_CODE " +
            "JOIN COM_TXN_TASK_INBOX inbox ON pi.PROCESS_INSTANCE_CODE = inbox.PROCESS_INSTANCE_CODE " +
            "JOIN ACT_HI_TASKINST task ON inbox.TASK_ID = task.ID_ " +
            "JOIN CTG_CFG_PROCESS_TYPE pt ON pi.PROCESS_TYPE_CODE = pt.PROCESS_TYPE_CODE " +
            "LEFT JOIN COM_CFG_RESOURCE_MAPPING mapping ON inbox.ORG_CODE = mapping.RESOURCE_CODE " +
            "  AND mapping.RESOURCE_TYPE_CODE = 'CM032.001' " +
            "  AND mapping.USER_ID = :userId " +
            "LEFT JOIN COM_CFG_RESOURCE_MAPPING mapping1 ON pi.PROCESS_INSTANCE_CODE = mapping1.RESOURCE_CODE " +
            "  AND mapping1.RESOURCE_TYPE_CODE = 'CM032.002' " +
            "  AND mapping1.USER_ID = :userId " +
            "WHERE inbox.MODIFIED_DATE = ( " +
            "    SELECT MAX(inbox2.MODIFIED_DATE) " +
            "    FROM COM_TXN_TASK_INBOX inbox2 " +
            "    WHERE inbox2.PROCESS_INSTANCE_CODE = pi.PROCESS_INSTANCE_CODE " +
            ") " +
            "AND (:customerCode IS NULL OR pi.CUSTOMER_CODE = :customerCode) " +
            "AND (:processInstanceCode IS NULL OR pi.PROCESS_INSTANCE_CODE = :processInstanceCode) " +
            "AND (:orgCode IS NULL OR inbox.ORG_CODE = :orgCode) " +
            "AND (:businessStatusListSize =0  OR pi.BUSINESS_STATUS IN (:businessStatusList)) " +
            "AND (:slaResult IS NULL OR pi.SLA_RESULT = :slaResult) " +
            "AND (:processTypeCodeListSize=0 OR pi.PROCESS_TYPE_CODE IN( :processTypeCodeList)) " +
            "AND (:fromDate IS NULL OR pi.MODIFIED_DATE >= TO_DATE(:fromDate, 'YYYY-MM-DD')) " +
            "AND (:toDate IS NULL OR pi.MODIFIED_DATE <= TO_DATE(:toDate, 'YYYY-MM-DD')) " +
            "AND ( " +
            "  :slaStatusColor IS NULL " +
            "  OR ( " +
            "    (:slaStatusColor = 'RED' AND pi.SLA_RESULT = 'BREACHED' AND pi.BUSINESS_STATUS IN ('COMPLETE', 'CANCEL')) " +
            "    OR (:slaStatusColor = 'GREEN' AND pi.SLA_RESULT = 'ACHIEVED' AND pi.BUSINESS_STATUS IN ('COMPLETE', 'CANCEL')) " +
            "    OR (pi.SLA_RESULT IS NULL AND ( " +
            "      (pi.SLA_WARNING_TYPE = 'FIXED' AND ( " +
            "        (:slaStatusColor = 'GREEN' AND (SYSDATE - pi.CREATED_DATE) < NUMTODSINTERVAL(pi.SLA_WARNING_DURATION, 'MINUTE')) " +
            "        OR (:slaStatusColor = 'YELLOW' AND (SYSDATE - pi.CREATED_DATE) BETWEEN NUMTODSINTERVAL(pi.SLA_WARNING_DURATION, 'MINUTE') AND NUMTODSINTERVAL(pi.SLA_MAX_DURATION, 'MINUTE')) " +
            "        OR (:slaStatusColor = 'RED' AND (SYSDATE - pi.CREATED_DATE) > NUMTODSINTERVAL(pi.SLA_MAX_DURATION, 'MINUTE')) " +
            "      )) " +
            "      OR (pi.SLA_WARNING_TYPE = 'PERCENT' AND ( " +
            "        (:slaStatusColor = 'GREEN' AND (SYSDATE - pi.CREATED_DATE) < NUMTODSINTERVAL(pi.SLA_MAX_DURATION * pi.SLA_WARNING_PERCENT, 'MINUTE')) " +
            "        OR (:slaStatusColor = 'YELLOW' AND (SYSDATE - pi.CREATED_DATE) BETWEEN NUMTODSINTERVAL(pi.SLA_MAX_DURATION * pi.SLA_WARNING_PERCENT, 'MINUTE') AND NUMTODSINTERVAL(pi.SLA_MAX_DURATION, 'MINUTE')) " +
            "        OR (:slaStatusColor = 'RED' AND (SYSDATE - pi.CREATED_DATE) > NUMTODSINTERVAL(pi.SLA_MAX_DURATION, 'MINUTE')) " +
            "      )) " +
            "    )) " +
            "  ) " +
            ")",
            nativeQuery = true)
    List<ComTxnProcessDto> exportToExcel( @Param("customerCode") String customerCode,
                                          @Param("processInstanceCode") String processInstanceCode,
                                          @Param("orgCode") String orgCode,
                                          @Param("businessStatusList") List<String> businessStatusList,
                                          @Param("slaResult") String slaResult,
                                          @Param("processTypeCodeList") List<String> processTypeCodeList,
                                          @Param("fromDate") String fromDate,
                                          @Param("toDate") String toDate,
                                          @Param("slaStatusColor") String slaStatusColor,
                                          @Param("businessStatusListSize") int businessStatusListSize,
                                          @Param("processTypeCodeListSize") int processTypeCodeListSize,
                                          @Param("userId") String userId
    );






    @Query(value = "SELECT p.PROCESS_TYPE_CODE AS processTypeCode,p.PROCESS_TYPE_NAME AS processTypeName " +
            "FROM CTG_CFG_PROCESS_TYPE p ",nativeQuery = true)

    List<ProcessTypeDto> getProcessTypes();



    @Query(value = "SELECT DISTINCT STATE_ FROM ACT_HI_PROCINST ",nativeQuery = true)
    List<String> getStates();

}
