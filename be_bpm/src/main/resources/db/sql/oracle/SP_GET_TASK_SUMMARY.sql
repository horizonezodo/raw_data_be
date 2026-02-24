create or replace NONEDITIONABLE PROCEDURE "SP_GET_TASK_SUMMARY" (
    p_filter_date IN VARCHAR2,
    p_org_code IN VARCHAR2,
    cur_assigned OUT SYS_REFCURSOR,
    cur_unassigned OUT SYS_REFCURSOR
)
AS
BEGIN
    -- Gộp task đã assign (accepted_by hoặc assign_to đơn), nhóm theo assignee
OPEN cur_assigned FOR
SELECT
    NVL(accepted_by, assign_to) AS assignee,
    NULL AS rule_code,
    SUM(CASE WHEN business_status = 'UNASSIGNED' THEN 1 ELSE 0 END) AS not_started,
    SUM(CASE WHEN business_status = 'ACTIVE' THEN 1 ELSE 0 END) AS in_progress,
    SUM(CASE WHEN business_status = 'COMPLETE' THEN 1 ELSE 0 END) AS completed
FROM BPM_TXN_TASK_INBOX
WHERE created_date >= TO_DATE(p_filter_date, 'YYYY-MM-DD')
  AND (p_org_code IS NULL OR p_org_code = '' OR org_code = p_org_code)
  AND (accepted_by IS NOT NULL OR (accepted_by IS NULL AND INSTR(assign_to, ',') = 0))
GROUP BY NVL(accepted_by, assign_to)
HAVING SUM(CASE WHEN business_status = 'UNASSIGNED' THEN 1 ELSE 0 END) > 0
    OR SUM(CASE WHEN business_status = 'ACTIVE' THEN 1 ELSE 0 END) > 0
    OR SUM(CASE WHEN business_status = 'COMPLETE' THEN 1 ELSE 0 END) > 0
ORDER BY assignee;

-- Task chưa assign (assign_to nhiều giá trị)
OPEN cur_unassigned FOR
SELECT
    ccr.rule_name AS assignee,
    ccr.rule_code,
    SUM(CASE WHEN cti.business_status = 'UNASSIGNED' THEN 1 ELSE 0 END) AS not_started,
    0 AS in_progress,
    0 AS completed
FROM BPM_TXN_TASK_INBOX cti
         JOIN COM_CFG_RULE ccr ON ccr.rule_code = cti.rule_code
WHERE cti.created_date >= TO_DATE(p_filter_date, 'YYYY-MM-DD')
  AND (p_org_code IS NULL OR p_org_code = '' OR cti.org_code = p_org_code)
  AND cti.accepted_by IS NULL
  AND INSTR(cti.assign_to, ',') > 0
GROUP BY ccr.rule_code, ccr.rule_name
HAVING SUM(CASE WHEN cti.business_status = 'UNASSIGNED' THEN 1 ELSE 0 END) > 0
ORDER BY ccr.rule_code;
END;
