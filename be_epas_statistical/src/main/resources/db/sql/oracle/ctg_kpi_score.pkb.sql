CREATE OR REPLACE EDITIONABLE PACKAGE BODY CTG_KPI_SCORE AS

  /* =========================================================
     LOGGING
     ========================================================= */
  PROCEDURE log(p_msg IN VARCHAR2) IS
  BEGIN
    DBMS_OUTPUT.PUT_LINE(
      TO_CHAR(SYSTIMESTAMP,'YYYY-MM-DD HH24:MI:SS.FF3') || ' | ' || p_msg
    );
  EXCEPTION
    WHEN OTHERS THEN NULL;
  END;

  PROCEDURE log_sql(p_tag IN VARCHAR2, p_sql IN CLOB) IS
  BEGIN
    DBMS_OUTPUT.PUT_LINE(
      TO_CHAR(SYSTIMESTAMP,'YYYY-MM-DD HH24:MI:SS.FF3') || ' | ' ||
      p_tag || ' SQL> ' ||
      DBMS_LOB.SUBSTR(p_sql, 3900, 1)
    );
  EXCEPTION
    WHEN OTHERS THEN NULL;
  END;

  /* =========================================================
     HELPERS
     ========================================================= */
  FUNCTION normalize_sql(
    p_sql          IN CLOB,
    p_data_date_v  IN VARCHAR2
  ) RETURN CLOB IS
    l_sql CLOB := p_sql;
    l_d   DATE := TO_DATE(p_data_date_v,'YYYY-MM-DD');
    v_day VARCHAR2(8) := TO_CHAR(l_d,'YYYYMMDD');
    v_ym  VARCHAR2(6) := TO_CHAR(l_d,'YYYYMM');
    v_yr  VARCHAR2(4) := TO_CHAR(l_d,'YYYY');
    v_qmm VARCHAR2(2);
  BEGIN
    IF l_sql IS NULL THEN
      RAISE_APPLICATION_ERROR(-20010,'EXPRESSION_SQL null');
    END IF;

    v_qmm := TO_CHAR(ADD_MONTHS(TRUNC(l_d, 'Q'), 2), 'MM');

    IF REGEXP_LIKE(l_sql,';\s*$','i') THEN
      l_sql := REGEXP_REPLACE(l_sql,';\s*$','',1,1,'i');
    END IF;

    l_sql := REPLACE(l_sql, '@BAS_DAY@', ''''||v_day||'''');
    l_sql := REPLACE(l_sql, '@BAS_YM@',  ''''||v_ym ||'''');
    l_sql := REPLACE(l_sql, '@BAS_YMQ@', ''''||(v_yr||v_qmm)||'''');
    l_sql := REPLACE(l_sql, '@BAS_YR@',  ''''||v_yr ||'''');

    RETURN l_sql;
  END;

  FUNCTION build_score_update_sql(
    p_kpi_code IN VARCHAR2
  ) RETURN CLOB IS
    l_sql         CLOB := 'UPDATE TMP_KPI_VAL t SET ';
    l_case_score  CLOB := 'SCORE_VALUE = CASE ';
    l_case_result CLOB := 'RESULT_NAME = CASE ';
    l_has_when    BOOLEAN := FALSE;
    l_def_score   NUMBER := NULL;
    l_def_result  VARCHAR2(128) := NULL;
    l_cond        CLOB;
    l_then_result VARCHAR2(4000);
  BEGIN
    FOR r IN (
      SELECT SORT_NUMBER, CONDITION_EXPRESSION, SCORE_VALUE, RESULT_NAME
      FROM CTG_CFG_STAT_SCORE_KPI_RESULT
      WHERE KPI_CODE = p_kpi_code
        AND NVL(IS_DELETE,0)=0
      ORDER BY SORT_NUMBER
    ) LOOP
      IF r.CONDITION_EXPRESSION IS NULL THEN
        l_def_score  := r.SCORE_VALUE;
        l_def_result := r.RESULT_NAME;
      ELSE
        l_has_when := TRUE;
        l_cond := REPLACE(r.CONDITION_EXPRESSION,'[GIA_TRI]','t.KPI_VALUE');

        l_case_score :=
          l_case_score || ' WHEN ('|| l_cond ||') THEN '||
          TO_CHAR(r.SCORE_VALUE);

        IF r.RESULT_NAME IS NULL THEN
          l_then_result := 'RESULT_NAME';
        ELSE
          l_then_result := ''''||REPLACE(r.RESULT_NAME,'''','''''')||'''';
        END IF;

        l_case_result :=
          l_case_result || ' WHEN ('|| l_cond ||') THEN '|| l_then_result;
      END IF;
    END LOOP;

    IF l_has_when THEN
      l_case_score :=
        l_case_score || ' ELSE '||
        NVL(TO_CHAR(l_def_score),'SCORE_VALUE')||' END';

      IF l_def_result IS NULL THEN
        l_case_result := l_case_result || ' ELSE RESULT_NAME END';
      ELSE
        l_case_result :=
          l_case_result || ' ELSE '||
          ''''||REPLACE(l_def_result,'''','''''')||''' END';
      END IF;

      l_sql := l_sql || l_case_score || ', ' || l_case_result ||
               ' WHERE KPI_CODE = :KPI_CODE';
    ELSE
      IF l_def_score IS NOT NULL OR l_def_result IS NOT NULL THEN
        l_sql :=
          l_sql ||
          'SCORE_VALUE = '||
          NVL(TO_CHAR(l_def_score),'SCORE_VALUE')||', '||
          'RESULT_NAME = '||
          CASE
            WHEN l_def_result IS NULL THEN 'RESULT_NAME'
            ELSE ''''||REPLACE(l_def_result,'''','''''')||''''
          END ||
          ' WHERE KPI_CODE = :KPI_CODE';
      ELSE
        l_sql :=
          'UPDATE TMP_KPI_VAL t SET '||
          'SCORE_VALUE = SCORE_VALUE, RESULT_NAME = RESULT_NAME '||
          'WHERE KPI_CODE = :KPI_CODE AND 1=0';
      END IF;
    END IF;

    RETURN l_sql;
  END;

  /* =========================================================
     AGGREGATES
     ========================================================= */
  PROCEDURE fill_aggregates IS
  BEGIN
    UPDATE TMP_KPI_VAL t
    SET group_score_value = (
      SELECT SUM(NVL(gk.weight_score,0) * NVL(t2.score_value,0) / 100)
      FROM TMP_KPI_VAL t2
      JOIN CTG_CFG_STAT_SCORE_GROUP_KPI gk
        ON gk.kpi_code = t2.kpi_code
       AND NVL(gk.IS_DELETE,0)=0
      WHERE t2.ci_br_id = t.ci_br_id
        AND gk.stat_score_group_code = t.stat_score_group_code
    );

    UPDATE TMP_KPI_VAL
    SET group_raw_score =
        NVL(group_weight_score,0) * NVL(group_score_value,0) / 100;

    UPDATE TMP_KPI_VAL t
    SET ci_br_score_value = (
      SELECT SUM(NVL(x.group_raw_score,0))
      FROM (
        SELECT DISTINCT ci_br_id,
                        stat_score_group_code,
                        group_raw_score
        FROM TMP_KPI_VAL
        WHERE ci_br_id = t.ci_br_id
      ) x
    );
  END;

  /* =========================================================
     BENCHMARK
     ========================================================= */
  FUNCTION build_benchmark_update_sql RETURN CLOB IS
    l_sql      CLOB := 'UPDATE TMP_KPI_VAL t SET ';
    l_val      CLOB := 'BENCHMARK_VALUE = CASE ';
    l_desc     CLOB := 'BENCHMARK_DESC  = CASE ';
    l_has_when BOOLEAN := FALSE;
    l_def_val  VARCHAR2(128) := NULL;
    l_def_desc VARCHAR2(4000) := NULL;
    l_cond     CLOB;
  BEGIN
    FOR r IN (
      SELECT CONDITION_EXPRESSION, BENCHMARK_VALUE, BENCHMARK_DESC
      FROM CTG_CFG_STAT_SCORE_BENCHMARK
      WHERE NVL(IS_DELETE,0)=0
    ) LOOP
      IF r.CONDITION_EXPRESSION IS NULL THEN
        l_def_val  := r.BENCHMARK_VALUE;
        l_def_desc := r.BENCHMARK_DESC;
      ELSE
        l_has_when := TRUE;
        l_cond := REPLACE(r.CONDITION_EXPRESSION,'[GIA_TRI]',
                          't.CI_BR_SCORE_VALUE');

        l_val :=
          l_val || ' WHEN ('|| l_cond ||') THEN '||
          ''''||REPLACE(r.BENCHMARK_VALUE,'''','''''')||'''';

        l_desc :=
          l_desc || ' WHEN ('|| l_cond ||') THEN '||
          CASE
            WHEN r.BENCHMARK_DESC IS NULL THEN 'NULL'
            ELSE ''''||REPLACE(r.BENCHMARK_DESC,'''','''''')||''''
          END;
      END IF;
    END LOOP;

    IF l_has_when THEN
      l_val :=
        l_val || ' ELSE '||
        CASE
          WHEN l_def_val IS NULL THEN 'BENCHMARK_VALUE'
          ELSE ''''||REPLACE(l_def_val,'''','''''')||''''
        END || ' END';

      l_desc :=
        l_desc || ' ELSE '||
        CASE
          WHEN l_def_desc IS NULL THEN 'BENCHMARK_DESC'
          ELSE ''''||REPLACE(l_def_desc,'''','''''')||''''
        END || ' END';

      RETURN l_sql || l_val || ', ' || l_desc;
    ELSE
      RETURN l_sql ||
             'BENCHMARK_VALUE = BENCHMARK_VALUE, '||
             'BENCHMARK_DESC  = BENCHMARK_DESC';
    END IF;
  END;

  PROCEDURE apply_benchmark IS
    l_upd CLOB;
  BEGIN
    l_upd := build_benchmark_update_sql;
    log_sql('UPDATE BENCHMARK', l_upd);
    EXECUTE IMMEDIATE l_upd;
  END;

  /* =========================================================
     MAIN PROCEDURES
     ========================================================= */
  PROCEDURE materialize_kpi_values(
    p_ci_id                IN VARCHAR2,
    p_stat_score_type_code IN VARCHAR2,
    p_data_date_v          IN VARCHAR2
  ) IS
    l_rows NUMBER;
    l_sql  CLOB;
  BEGIN
    log('START materialize_kpi_values');

    EXECUTE IMMEDIATE 'TRUNCATE TABLE TMP_KPI_VAL';

    FOR k IN (
      SELECT k.KPI_CODE, k.EXPRESSION_SQL
      FROM CTG_CFG_STAT_KPI k
      JOIN CTG_CFG_STAT_SCORE_GROUP_KPI gk
        ON gk.KPI_CODE = k.KPI_CODE
      JOIN CTG_CFG_STAT_SCORE_GROUP sg
        ON sg.STAT_SCORE_GROUP_CODE = gk.STAT_SCORE_GROUP_CODE
      WHERE sg.STAT_SCORE_TYPE_CODE = p_stat_score_type_code
        AND NVL(k.IS_DELETE,0)=0
        AND NVL(gk.IS_DELETE,0)=0
        AND NVL(sg.IS_DELETE,0)=0
      GROUP BY k.KPI_CODE, k.EXPRESSION_SQL
      ORDER BY k.KPI_CODE
    ) LOOP
      DECLARE
        l_sel CLOB := normalize_sql(k.EXPRESSION_SQL, p_data_date_v);
        l_ins CLOB;
      BEGIN
        l_ins :=
        'INSERT INTO TMP_KPI_VAL
           (CI_BR_ID, KPI_CODE, KPI_VALUE,
            STAT_SCORE_GROUP_CODE, GROUP_WEIGHT_SCORE, KPI_WEIGHT_SCORE)
         SELECT
           b.CI_BR_ID,
           :KPI_CODE,
           x.KPI_VALUE,
           gk.STAT_SCORE_GROUP_CODE,
           g.WEIGHT_SCORE,
           gk.WEIGHT_SCORE
         FROM ('|| l_sel ||') x
         JOIN DIM_CI_BR_D b
           ON b.CI_BR_ID = x.CI_BR_ID
          AND b.CI_ID    = :CI_ID
          AND NVL(b.IS_DELETE,0)=0
         JOIN CTG_CFG_STAT_SCORE_GROUP_KPI gk
           ON gk.KPI_CODE = :KPI_CODE
          AND NVL(gk.IS_DELETE,0)=0
         JOIN CTG_CFG_STAT_SCORE_GROUP g
           ON g.STAT_SCORE_GROUP_CODE = gk.STAT_SCORE_GROUP_CODE
          AND g.STAT_SCORE_TYPE_CODE  = :P_TYPE
          AND NVL(g.IS_DELETE,0)=0';

        EXECUTE IMMEDIATE l_ins
          USING k.KPI_CODE,
                p_ci_id,
                k.KPI_CODE,
                p_stat_score_type_code;

        l_rows := SQL%ROWCOUNT;
        log('KPI '||k.KPI_CODE||' | inserted rows='||l_rows);
      END;
    END LOOP;

    log('END materialize_kpi_values');
  END;

  PROCEDURE score_from_tmp IS
    l_upd CLOB;
  BEGIN
    FOR k IN (SELECT DISTINCT KPI_CODE FROM TMP_KPI_VAL) LOOP
      l_upd := build_score_update_sql(k.KPI_CODE);
      EXECUTE IMMEDIATE l_upd USING k.KPI_CODE;
    END LOOP;
  END;

  PROCEDURE compute_scores(
    p_ci_id                IN VARCHAR2,
    p_stat_score_type_code IN VARCHAR2,
    p_data_date_v          IN VARCHAR2,
    o_cur                  OUT SYS_REFCURSOR
  ) IS
  BEGIN
    IF p_data_date_v IS NULL
       OR NOT REGEXP_LIKE(p_data_date_v,'^\d{4}-\d{2}-\d{2}$') THEN
      RAISE_APPLICATION_ERROR(-20020,
        'p_data_date_v phai co dinh dang YYYY-MM-DD');
    END IF;

    materialize_kpi_values(p_ci_id, p_stat_score_type_code, p_data_date_v);
    score_from_tmp;
    fill_aggregates;
    apply_benchmark;

    OPEN o_cur FOR
      SELECT *
      FROM TMP_KPI_VAL;
  END;

END CTG_KPI_SCORE;
/
