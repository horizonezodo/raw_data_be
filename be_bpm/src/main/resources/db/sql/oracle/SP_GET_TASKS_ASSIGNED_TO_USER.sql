create or replace NONEDITIONABLE PROCEDURE "SP_GET_TASKS_ASSIGNED_TO_USER" (
    p_current_user IN VARCHAR2,
    p_org_code IN VARCHAR2,
    cur_result OUT SYS_REFCURSOR
) AS
    v_sql VARCHAR2(4000);
    v_where_clause VARCHAR2(2000);
BEGIN
    -- Build WHERE clause
    v_where_clause := '(ctti.ACCEPTED_BY = ''' || p_current_user || ''' OR ' ||
                     '(ctti.ACCEPTED_BY IS NULL AND ctti.ASSIGN_TO = ''' || p_current_user || ''') OR ' ||
                     '(ctti.ACCEPTED_BY IS NULL AND ctti.ASSIGN_TO LIKE ''%' || p_current_user || '%''))';

    -- Add org code filter if provided
    IF p_org_code IS NOT NULL AND p_org_code != '' THEN
        v_where_clause := v_where_clause || ' AND ctti.ORG_CODE = ''' || p_org_code || '''';
END IF;

    -- Add business status filter (exclude COMPLETE)
    v_where_clause := v_where_clause || ' AND ctti.BUSINESS_STATUS <> ''COMPLETE''';

    -- Build final SQL
    v_sql := 'SELECT ctti.PROCESS_INSTANCE_CODE,
                     ctti.TASK_DEFINE_NAME,
                     ctti.ACCEPTED_BY
              FROM BPM_TXN_TASK_INBOX ctti
              WHERE ' || v_where_clause || '
              ORDER BY ctti.PROCESS_INSTANCE_CODE DESC';

    -- Execute dynamic SQL
OPEN cur_result FOR v_sql;

EXCEPTION
    WHEN OTHERS THEN
        -- Log error and return empty result
        DBMS_OUTPUT.PUT_LINE('Error in SP_GET_TASKS_ASSIGNED_TO_USER: ' || SQLERRM);
OPEN cur_result FOR SELECT NULL AS PROCESS_INSTANCE_CODE FROM DUAL WHERE 1=0;
END SP_GET_TASKS_ASSIGNED_TO_USER;
