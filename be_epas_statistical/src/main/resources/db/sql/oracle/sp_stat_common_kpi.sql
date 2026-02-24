CREATE OR REPLACE EDITIONABLE PROCEDURE SP_STAT_COMMON_KPI (
    p_template_code       IN  VARCHAR2,
    p_rev_no              IN  NUMBER,
    p_org_code            IN  VARCHAR2,
    p_stat_instance_code  IN  VARCHAR2,
    p_report_data_date    IN  DATE,
    p_agg_run_no          IN  NUMBER,
    p_created_by          IN  VARCHAR2
) AS
    CURSOR c_kpi IS
        SELECT  k.TEMPLATE_KPI_CODE,
                k.TEMPLATE_KPI_NAME,
                k.KPI_CODE,
                COALESCE(k.KPI_NAME, base_kpi.KPI_NAME) AS KPI_NAME,
                k.AREA_ID,
                k.SORT_NUMBER,
                NVL(base_kpi.EXPRESSION_SQL, k.EXPRESSION_SQL) AS EXPR_SQL,
                s.TABLE_DATA
        FROM    CTG_CFG_STAT_TEMPLATE_KPI k
                JOIN CTG_CFG_STAT_TEMPLATE_SHEET s
                    ON s.AREA_ID = k.AREA_ID
                   AND s.TEMPLATE_CODE = k.TEMPLATE_CODE
                LEFT JOIN CTG_CFG_STAT_KPI base_kpi
                    ON base_kpi.KPI_CODE = k.KPI_CODE
        WHERE   k.TEMPLATE_CODE = p_template_code
        ORDER BY k.SORT_NUMBER, k.TEMPLATE_KPI_CODE;

    v_expr_sql          CLOB;
    v_insert_sql        CLOB;
    v_update_result_sql CLOB;
    v_table_name        VARCHAR2(200);
    v_created_by        VARCHAR2(100);
    v_kpi_value         VARCHAR2(2000);

    v_org_code  VARCHAR2(8);
    v_bas_day   VARCHAR2(8);
    v_bas_ym    VARCHAR2(6);
    v_bas_ymq   VARCHAR2(6);
    v_bas_yr    VARCHAR2(4);
BEGIN
    -- ORG_CODE wildcard
    IF p_org_code = '%' THEN
        v_org_code := 'ORG_CODE';
    ELSE
        v_org_code := p_org_code;
    END IF;

    v_created_by := NVL(p_created_by, USER);

    v_bas_day := TO_CHAR(p_report_data_date, 'YYYYMMDD');
    v_bas_ym  := TO_CHAR(p_report_data_date, 'YYYYMM');
    v_bas_ymq := TO_CHAR(p_report_data_date, 'YYYYMM');
    v_bas_yr  := TO_CHAR(p_report_data_date, 'YYYY');

    FOR r IN c_kpi LOOP
        v_table_name := r.TABLE_DATA;
        v_expr_sql   := TRIM(RTRIM(r.EXPR_SQL, ';'));

        IF v_expr_sql IS NULL THEN
            RAISE_APPLICATION_ERROR(
                -20001,
                'EXPRESSION_SQL is NULL, TEMPLATE_KPI_CODE=' || r.TEMPLATE_KPI_CODE
            );
        END IF;

        -- Replace dynamic variables
        v_expr_sql := REPLACE(v_expr_sql, '@ORG_CODE@', v_org_code);
        v_expr_sql := REPLACE(v_expr_sql, '@BAS_DAY@',  v_bas_day);
        v_expr_sql := REPLACE(v_expr_sql, '@BAS_YM@',   v_bas_ym);
        v_expr_sql := REPLACE(v_expr_sql, '@BAS_YMQ@',  v_bas_ymq);
        v_expr_sql := REPLACE(v_expr_sql, '@BAS_YR@',   v_bas_yr);

        -- Không cho phép bind
        IF INSTR(v_expr_sql, ':') > 0 THEN
            RAISE_APPLICATION_ERROR(
                -20003,
                'EXPRESSION_SQL chứa bind ":", TEMPLATE_KPI_CODE='
                || r.TEMPLATE_KPI_CODE
            );
        END IF;

        -- Execute KPI expression
        BEGIN
            EXECUTE IMMEDIATE v_expr_sql INTO v_kpi_value;
        EXCEPTION
            WHEN NO_DATA_FOUND THEN
                v_kpi_value := NULL;
            WHEN TOO_MANY_ROWS THEN
                RAISE_APPLICATION_ERROR(
                    -20002,
                    'EXPRESSION_SQL trả >1 dòng, TEMPLATE_KPI_CODE='
                    || r.TEMPLATE_KPI_CODE
                );
        END;

        -- Deactivate old KPI result
        v_update_result_sql :=
            'UPDATE ' || v_table_name || '
             SET IS_ACTIVE = 0
             WHERE KPI_CODE = :1
               AND STAT_INSTANCE_CODE = :2
               AND NVL(IS_ACTIVE,1) = 1';

        EXECUTE IMMEDIATE v_update_result_sql
            USING r.TEMPLATE_KPI_CODE, p_stat_instance_code;

        -- Insert new KPI result
        v_insert_sql :=
            'INSERT INTO ' || v_table_name || ' (
                CREATED_DATE, CREATED_BY, APPROVED_DATE, APPROVED_BY,
                IS_VOID, SORT_NUMBER, CURRENCY_TYPE, REV_NO,
                ORG_CODE, STAT_INSTANCE_CODE, REPORT_DATA_DATE,
                KPI_CODE, KPI_NAME, KPI_VALUE, IS_ACTIVE
             ) VALUES (
                SYSTIMESTAMP, :1, SYSTIMESTAMP, :2,
                0, :3, ''VND'', :4,
                :5, :6, :7,
                :8, :9, :10, 1
             )';

        EXECUTE IMMEDIATE v_insert_sql
            USING v_created_by,
                  v_created_by,
                  NVL(r.SORT_NUMBER, 0),
                  p_rev_no,
                  p_org_code,
                  p_stat_instance_code,
                  p_report_data_date,
                  r.TEMPLATE_KPI_CODE,
                  r.KPI_NAME,
                  v_kpi_value;

        -- Insert to RPT_TXN_STAT_TEMPLATE_KPI
        IF r.KPI_CODE IS NOT NULL THEN
            UPDATE RPT_TXN_STAT_TEMPLATE_KPI
            SET IS_ACTIVE = 0
            WHERE KPI_CODE = r.KPI_CODE
              AND TEMPLATE_CODE = p_template_code
              AND STAT_INSTANCE_CODE = p_stat_instance_code
              AND NVL(IS_ACTIVE,1) = 1;

            INSERT INTO RPT_TXN_STAT_TEMPLATE_KPI (
                CREATED_DATE, CREATED_BY, APPROVED_DATE, APPROVED_BY,
                MODIFIED_DATE, MODIFIED_BY,
                RECORD_STATUS, IS_DELETE, IS_ACTIVE,
                ORG_CODE, TEMPLATE_CODE, STAT_INSTANCE_CODE,
                AGGREGATION_RUN_NO, REV_NO,
                KPI_CODE, KPI_NAME, REPORT_DATA_DATE,
                CURRENCY_CODE, KPI_VALUE
            )
            VALUES (
                SYSTIMESTAMP, v_created_by,
                SYSTIMESTAMP, v_created_by,
                SYSTIMESTAMP, v_created_by,
                'approval', 0, 1,
                p_org_code, p_template_code, p_stat_instance_code,
                p_agg_run_no, p_rev_no,
                r.KPI_CODE, r.KPI_NAME, p_report_data_date,
                'VND', v_kpi_value
            );
        END IF;
    END LOOP;

EXCEPTION
    WHEN OTHERS THEN
        RAISE_APPLICATION_ERROR(
            -20099,
            'Lỗi SP_STAT_COMMON_KPI: ' || SQLERRM
        );
END SP_STAT_COMMON_KPI;
/
