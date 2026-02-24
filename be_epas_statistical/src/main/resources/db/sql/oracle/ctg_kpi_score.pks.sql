CREATE OR REPLACE EDITIONABLE PACKAGE CTG_KPI_SCORE AS
  PROCEDURE compute_scores(
    p_ci_id                IN VARCHAR2,
    p_stat_score_type_code IN VARCHAR2,
    p_data_date_v          IN VARCHAR2,
    o_cur                  OUT SYS_REFCURSOR
  );

  PROCEDURE materialize_kpi_values(
    p_ci_id                IN VARCHAR2,
    p_stat_score_type_code IN VARCHAR2,
    p_data_date_v          IN VARCHAR2
  );

  PROCEDURE score_from_tmp;
END CTG_KPI_SCORE;
/
