create or replace NONEDITIONABLE PROCEDURE "SP_GET_TASK_STATUS" (
    p_filter_date IN VARCHAR2,
    p_org_code IN VARCHAR2,
    not_started OUT NUMBER,
    in_progress OUT NUMBER,
    completed OUT NUMBER,
    overdue OUT NUMBER
)
AS
BEGIN
SELECT COUNT(cti.task_id)
INTO not_started
FROM BPM_TXN_TASK_INBOX cti
WHERE cti.created_date >= TO_DATE(p_filter_date, 'YYYY-MM-DD')
  AND (p_org_code IS NULL OR p_org_code = '' OR cti.org_code = p_org_code)
  AND cti.accepted_by IS NULL
  AND cti.business_status = 'UNASSIGNED';

SELECT COUNT(cti.task_id)
INTO in_progress
FROM BPM_TXN_TASK_INBOX cti
WHERE cti.created_date >= TO_DATE(p_filter_date, 'YYYY-MM-DD')
  AND (p_org_code IS NULL OR p_org_code = '' OR cti.org_code = p_org_code)
  AND cti.accepted_by IS NOT NULL
  AND cti.business_status = 'ACTIVE';

SELECT COUNT(cti.task_id)
INTO completed
FROM BPM_TXN_TASK_INBOX cti
WHERE cti.created_date >= TO_DATE(p_filter_date, 'YYYY-MM-DD')
  AND (p_org_code IS NULL OR p_org_code = '' OR cti.org_code = p_org_code)
  AND cti.business_status = 'COMPLETE';

SELECT COUNT(cti.task_id)
INTO overdue
FROM BPM_TXN_TASK_INBOX cti
WHERE cti.created_date >= TO_DATE(p_filter_date, 'YYYY-MM-DD')
  AND (p_org_code IS NULL OR p_org_code = '' OR cti.org_code = p_org_code)
  AND cti.sla_task_deadline < SYSDATE
  AND cti.business_status <> 'COMPLETE';
END;
