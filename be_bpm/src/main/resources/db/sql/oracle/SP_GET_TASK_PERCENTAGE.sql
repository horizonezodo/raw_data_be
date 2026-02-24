create or replace NONEDITIONABLE PROCEDURE "SP_GET_TASK_PERCENTAGE" (
    p_filter_date IN VARCHAR2,
    p_org_code IN VARCHAR2,
    not_started IN OUT NUMBER,
    in_progress IN OUT NUMBER,
    completed IN OUT NUMBER,
    overdue IN OUT NUMBER
)
AS
BEGIN
    -- Chưa thực hiện (Trong hạn)
SELECT COUNT(cti.task_id)
INTO not_started
FROM BPM_TXN_TASK_INBOX cti
WHERE cti.created_date >= TO_DATE(p_filter_date, 'YYYY-MM-DD')
  AND (p_org_code IS NULL OR p_org_code = '' OR cti.org_code = p_org_code)
  AND cti.business_status = 'UNASSIGNED'
  AND cti.sla_task_deadline >= SYSDATE;

-- Đang thực hiện (Trong hạn)
SELECT COUNT(cti.task_id)
INTO in_progress
FROM BPM_TXN_TASK_INBOX cti
WHERE cti.created_date >= TO_DATE(p_filter_date, 'YYYY-MM-DD')
  AND (p_org_code IS NULL OR p_org_code = '' OR cti.org_code = p_org_code)
  AND cti.business_status = 'ACTIVE'
  AND cti.sla_task_deadline >= SYSDATE;

-- Hoàn thành
SELECT COUNT(cti.task_id)
INTO completed
FROM BPM_TXN_TASK_INBOX cti
WHERE cti.created_date >= TO_DATE(p_filter_date, 'YYYY-MM-DD')
  AND (p_org_code IS NULL OR p_org_code = '' OR cti.org_code = p_org_code)
  AND cti.business_status = 'COMPLETE';

-- Quá hạn
SELECT COUNT(cti.task_id)
INTO overdue
FROM BPM_TXN_TASK_INBOX cti
WHERE cti.created_date >= TO_DATE(p_filter_date, 'YYYY-MM-DD')
  AND (p_org_code IS NULL OR p_org_code = '' OR cti.org_code = p_org_code)
  AND cti.sla_task_deadline < SYSDATE
  AND cti.business_status <> 'COMPLETE';
END;
